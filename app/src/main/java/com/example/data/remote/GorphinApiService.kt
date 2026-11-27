package com.example.data.remote

import com.example.data.model.AuthLoginRequest
import com.example.data.model.AuthRegisterRequest
import com.example.data.model.AuthResponse
import com.example.data.model.ChatDetailResponse
import com.example.data.model.ChatsResponse
import com.example.data.model.CheckoutRequest
import com.example.data.model.CheckoutResponse
import com.example.data.model.FlexibleIdAdapter
import com.example.data.model.LoadupCheckoutRequest
import com.example.data.model.LoadupStatusResponse
import com.example.data.model.MeResponse
import com.example.data.model.PlansResponse
import com.example.data.model.SettingsPatchRequest
import com.example.data.model.SettingsResponse
import com.example.data.model.SourceItem
import com.example.data.model.StreamEvent
import com.example.data.model.UsageQuota
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import java.io.IOException
import java.util.concurrent.TimeUnit

const val GORPHIN_API_BASE = "https://cloud-worker.gorphinai.pp.ua"

interface GorphinApiService {
    @POST("/api/auth/login")
    suspend fun login(@Body body: AuthLoginRequest): Response<AuthResponse>

    @POST("/api/auth/register")
    suspend fun register(@Body body: AuthRegisterRequest): Response<AuthResponse>

    @GET("/api/auth/me")
    suspend fun getMe(@Header("X-Session-Token") token: String): Response<MeResponse>

    @POST("/api/auth/logout")
    suspend fun logout(@Header("X-Session-Token") token: String): Response<Unit>

    @GET("/api/plans")
    suspend fun getPlans(): Response<PlansResponse>

    @GET("/api/chats")
    suspend fun getChats(@Header("X-Session-Token") token: String): Response<ChatsResponse>

    @GET("/api/chats/{id}")
    suspend fun getChatDetail(
        @Header("X-Session-Token") token: String,
        @Path("id") chatId: String
    ): Response<ChatDetailResponse>

    @DELETE("/api/chats/{id}")
    suspend fun deleteChat(
        @Header("X-Session-Token") token: String,
        @Path("id") chatId: String
    ): Response<Unit>

    @PATCH("/api/settings")
    suspend fun updateSettings(
        @Header("X-Session-Token") token: String,
        @Body body: SettingsPatchRequest
    ): Response<SettingsResponse>

    @POST("/api/billing/checkout")
    suspend fun createCheckout(
        @Header("X-Session-Token") token: String,
        @Body body: CheckoutRequest
    ): Response<CheckoutResponse>

    @GET("/api/billing/loadup-status")
    suspend fun getLoadupStatus(
        @Header("X-Session-Token") token: String
    ): Response<LoadupStatusResponse>

    @POST("/api/billing/loadup-checkout")
    suspend fun createLoadupCheckout(
        @Header("X-Session-Token") token: String,
        @Body body: LoadupCheckoutRequest
    ): Response<CheckoutResponse>
}

object GorphinNetworkClient {
    val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(25, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(FlexibleIdAdapter)
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    val apiService: GorphinApiService by lazy {
        Retrofit.Builder()
            .baseUrl("$GORPHIN_API_BASE/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GorphinApiService::class.java)
    }

    fun streamChat(
        token: String,
        chatId: String?,
        content: String,
        thinkMode: String,
        webSearch: Boolean
    ): Flow<StreamEvent> = flow {
        val payload = JSONObject().apply {
            if (chatId.isNullOrBlank()) {
                put("chat_id", JSONObject.NULL)
            } else {
                val asLong = chatId.toLongOrNull()
                if (asLong != null) put("chat_id", asLong) else put("chat_id", chatId)
            }
            put("content", content)
            put("think_mode", thinkMode)
            put("web_search", webSearch)
        }

        val request = Request.Builder()
            .url("$GORPHIN_API_BASE/api/chat/stream")
            .header("Content-Type", "application/json")
            .header("Accept", "text/event-stream")
            .header("X-Session-Token", token)
            .post(payload.toString().toRequestBody("application/json; charset=utf-8".toMediaType()))
            .build()

        val call = okHttpClient.newCall(request)
        try {
            call.execute().use { response ->
                if (!response.isSuccessful) {
                    val errText = response.body?.string().orEmpty()
                    val detail = extractErrorDetail(errText) ?: "HTTP ${response.code}"
                    emit(StreamEvent.Error(detail))
                    return@flow
                }
                val source = response.body?.source() ?: run {
                    emit(StreamEvent.Error("Empty response body"))
                    return@flow
                }

                while (currentCoroutineContext().isActive && !source.exhausted()) {
                    val line = source.readUtf8Line() ?: break
                    if (!line.startsWith("data: ")) continue
                    val dataStr = line.removePrefix("data: ").trim()
                    if (dataStr == "[DONE]") {
                        emit(StreamEvent.Done)
                        break
                    }
                    if (dataStr.isEmpty()) continue
                    val evt = runCatching { JSONObject(dataStr) }.getOrNull() ?: continue

                    if (evt.has("error") && !evt.isNull("error")) {
                        emit(StreamEvent.Error(evt.optString("error", "Stream error")))
                        return@flow
                    }

                    when (evt.optString("status")) {
                        "chat_meta" -> {
                            val id = evt.opt("chat_id")?.toString().orEmpty()
                            if (id.isNotBlank() && id != "null") {
                                emit(StreamEvent.ChatMeta(id))
                            }
                        }
                        "searching" -> emit(StreamEvent.Searching)
                        "sources" -> {
                            val list = mutableListOf<SourceItem>()
                            val arr = evt.optJSONArray("sources")
                            if (arr != null) {
                                for (i in 0 until arr.length()) {
                                    val obj = arr.optJSONObject(i) ?: continue
                                    val url = obj.optString("url", "")
                                    if (url.isNotBlank()) {
                                        list.add(
                                            SourceItem(
                                                title = obj.optString("title", url),
                                                url = url
                                            )
                                        )
                                    }
                                }
                            }
                            emit(StreamEvent.Sources(list))
                        }
                        "thinking" -> emit(StreamEvent.ThinkingStarted)
                        "usage" -> {
                            val u = evt.optJSONObject("usage")
                            if (u != null) {
                                emit(
                                    StreamEvent.UsageUpdate(
                                        UsageQuota(
                                            messages24h = u.optInt("messages_24h", 0),
                                            messages24hLimit = u.optInt("messages_24h_limit", 15),
                                            messages4h = u.optInt("messages_4h", 0),
                                            messages4hLimit = u.optInt("messages_4h_limit", 5),
                                            think24h = u.optInt("think_24h", 0),
                                            think24hLimit = u.optInt("think_24h_limit", 2),
                                            web24h = u.optInt("web_24h", 0),
                                            web24hLimit = u.optInt("web_24h_limit", 3)
                                        )
                                    )
                                )
                            }
                        }
                    }

                    if (evt.has("think") && !evt.isNull("think")) {
                        val delta = evt.optString("think", "")
                        if (delta.isNotEmpty()) emit(StreamEvent.ThinkDelta(delta))
                    }
                    if (evt.has("token") && !evt.isNull("token")) {
                        val delta = evt.optString("token", "")
                        if (delta.isNotEmpty()) emit(StreamEvent.TokenDelta(delta))
                    }
                }
                emit(StreamEvent.Done)
            }
        } catch (e: IOException) {
            if (currentCoroutineContext().isActive) {
                emit(StreamEvent.Error(e.localizedMessage ?: "Connection interrupted"))
            }
        }
    }.flowOn(Dispatchers.IO)

    fun extractErrorDetail(raw: String?): String? {
        if (raw.isNullOrBlank()) return null
        return runCatching {
            val json = JSONObject(raw)
            json.optString("detail").takeIf { it.isNotBlank() }
                ?: json.optString("error").takeIf { it.isNotBlank() }
        }.getOrNull()
    }
}
