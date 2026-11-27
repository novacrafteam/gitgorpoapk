package com.example.data.repository

import com.example.data.local.SessionPreferences
import com.example.data.local.StoredSession
import com.example.data.model.AuthLoginRequest
import com.example.data.model.AuthRegisterRequest
import com.example.data.model.AuthResponse
import com.example.data.model.ChatMessageDto
import com.example.data.model.ChatSummary
import com.example.data.model.CheckoutRequest
import com.example.data.model.LoadupCheckoutRequest
import com.example.data.model.MeResponse
import com.example.data.model.PendingLoadup
import com.example.data.model.PlanInfo
import com.example.data.model.SettingsPatchRequest
import com.example.data.model.StreamEvent
import com.example.data.model.UserProfile
import com.example.data.remote.GorphinApiService
import com.example.data.remote.GorphinNetworkClient
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class UnauthorizedException : Exception("Session expired")

sealed interface ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>
    data class Error(val message: String, val isUnauthorized: Boolean = false) : ApiResult<Nothing>
}

class GorphinRepository(
    private val api: GorphinApiService = GorphinNetworkClient.apiService,
    private val prefs: SessionPreferences
) {
    suspend fun getStoredSession(): StoredSession = prefs.getSessionSnapshot()
    suspend fun saveToken(token: String) = prefs.saveToken(token)
    suspend fun clearToken() = prefs.clearToken()
    suspend fun saveLanguage(lang: String) = prefs.saveLanguage(lang)
    suspend fun saveThinkMode(mode: String) = prefs.saveThinkMode(mode)
    suspend fun saveWebSearch(enabled: Boolean) = prefs.saveWebSearch(enabled)

    private fun <T> mapResponse(res: Response<T>): ApiResult<T> {
        if (res.isSuccessful) {
            val body = res.body()
            if (body != null) return ApiResult.Success(body)
        }
        val rawErr = res.errorBody()?.string()
        val detail = GorphinNetworkClient.extractErrorDetail(rawErr) ?: "HTTP ${res.code()}"
        return ApiResult.Error(detail, isUnauthorized = res.code() == 401)
    }

    suspend fun login(email: String, password: String): ApiResult<AuthResponse> {
        return try {
            val res = api.login(AuthLoginRequest(email.trim(), password))
            val mapped = mapResponse(res)
            if (mapped is ApiResult.Success) {
                prefs.saveToken(mapped.data.token)
            }
            mapped
        } catch (e: Exception) {
            ApiResult.Error(e.localizedMessage ?: "Network error")
        }
    }

    suspend fun register(
        email: String,
        username: String,
        password: String,
        uiLang: String
    ): ApiResult<AuthResponse> {
        return try {
            val cleanName = username.trim().ifEmpty { email.substringBefore("@") }
            val res = api.register(AuthRegisterRequest(email.trim(), cleanName, password, uiLang))
            val mapped = mapResponse(res)
            if (mapped is ApiResult.Success) {
                prefs.saveToken(mapped.data.token)
            }
            mapped
        } catch (e: Exception) {
            ApiResult.Error(e.localizedMessage ?: "Network error")
        }
    }

    suspend fun getMe(token: String): ApiResult<MeResponse> {
        return try {
            mapResponse(api.getMe(token))
        } catch (e: Exception) {
            ApiResult.Error(e.localizedMessage ?: "Network error")
        }
    }

    suspend fun logout(token: String) {
        runCatching { api.logout(token) }
        prefs.clearToken()
    }

    suspend fun getPlans(): ApiResult<Map<String, PlanInfo>> {
        return try {
            when (val r = mapResponse(api.getPlans())) {
                is ApiResult.Success -> ApiResult.Success(r.data.plans)
                is ApiResult.Error -> r
            }
        } catch (e: Exception) {
            ApiResult.Error(e.localizedMessage ?: "Network error")
        }
    }

    suspend fun getChats(token: String): ApiResult<List<ChatSummary>> {
        return try {
            when (val r = mapResponse(api.getChats(token))) {
                is ApiResult.Success -> ApiResult.Success(r.data.chats)
                is ApiResult.Error -> r
            }
        } catch (e: Exception) {
            ApiResult.Error(e.localizedMessage ?: "Network error")
        }
    }

    suspend fun getChatMessages(token: String, chatId: String): ApiResult<List<ChatMessageDto>> {
        return try {
            when (val r = mapResponse(api.getChatDetail(token, chatId))) {
                is ApiResult.Success -> ApiResult.Success(r.data.messages)
                is ApiResult.Error -> r
            }
        } catch (e: Exception) {
            ApiResult.Error(e.localizedMessage ?: "Network error")
        }
    }

    suspend fun deleteChat(token: String, chatId: String): ApiResult<Unit> {
        return try {
            val res = api.deleteChat(token, chatId)
            if (res.isSuccessful) ApiResult.Success(Unit)
            else {
                val detail = GorphinNetworkClient.extractErrorDetail(res.errorBody()?.string())
                    ?: "HTTP ${res.code()}"
                ApiResult.Error(detail, isUnauthorized = res.code() == 401)
            }
        } catch (e: Exception) {
            ApiResult.Error(e.localizedMessage ?: "Network error")
        }
    }

    suspend fun updateSettings(token: String, uiLang: String, username: String): ApiResult<UserProfile> {
        return try {
            when (val r = mapResponse(api.updateSettings(token, SettingsPatchRequest(uiLang, username)))) {
                is ApiResult.Success -> {
                    prefs.saveLanguage(r.data.user.uiLang ?: uiLang)
                    ApiResult.Success(r.data.user)
                }
                is ApiResult.Error -> r
            }
        } catch (e: Exception) {
            ApiResult.Error(e.localizedMessage ?: "Network error")
        }
    }

    suspend fun createCheckout(token: String, plan: String, period: String): ApiResult<String> {
        return try {
            when (val r = mapResponse(api.createCheckout(token, CheckoutRequest(plan, period)))) {
                is ApiResult.Success -> ApiResult.Success(r.data.checkoutUrl)
                is ApiResult.Error -> r
            }
        } catch (e: Exception) {
            ApiResult.Error(e.localizedMessage ?: "Network error")
        }
    }

    suspend fun getLoadupStatus(token: String): ApiResult<PendingLoadup?> {
        return try {
            when (val r = mapResponse(api.getLoadupStatus(token))) {
                is ApiResult.Success -> ApiResult.Success(r.data.pending)
                is ApiResult.Error -> r
            }
        } catch (e: Exception) {
            ApiResult.Error(e.localizedMessage ?: "Network error")
        }
    }

    suspend fun createLoadupCheckout(token: String, tierId: String): ApiResult<String> {
        return try {
            when (val r = mapResponse(api.createLoadupCheckout(token, LoadupCheckoutRequest(tierId)))) {
                is ApiResult.Success -> ApiResult.Success(r.data.checkoutUrl)
                is ApiResult.Error -> r
            }
        } catch (e: Exception) {
            ApiResult.Error(e.localizedMessage ?: "Network error")
        }
    }

    fun streamChat(
        token: String,
        chatId: String?,
        content: String,
        thinkMode: String,
        webSearch: Boolean
    ): Flow<StreamEvent> = GorphinNetworkClient.streamChat(
        token = token,
        chatId = chatId,
        content = content,
        thinkMode = thinkMode,
        webSearch = webSearch
    )
}
