package com.example.data.model

import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson

object FlexibleIdAdapter {
    @FromJson
    fun fromJson(reader: JsonReader): FlexibleId {
        return when (reader.peek()) {
            JsonReader.Token.STRING -> FlexibleId(reader.nextString())
            JsonReader.Token.NUMBER -> FlexibleId(reader.nextString())
            JsonReader.Token.NULL -> {
                reader.nextNull<Unit>()
                FlexibleId("")
            }
            else -> {
                reader.skipValue()
                FlexibleId("")
            }
        }
    }

    @ToJson
    fun toJson(writer: JsonWriter, value: FlexibleId?) {
        if (value == null || value.value.isEmpty()) {
            writer.nullValue()
        } else {
            writer.value(value.value)
        }
    }
}

data class FlexibleId(val value: String)

@JsonClass(generateAdapter = true)
data class AuthLoginRequest(
    val email: String,
    val password: String
)

@JsonClass(generateAdapter = true)
data class AuthRegisterRequest(
    val email: String,
    val username: String,
    val password: String,
    @Json(name = "ui_lang") val uiLang: String
)

@JsonClass(generateAdapter = true)
data class AuthResponse(
    val token: String,
    val user: UserProfile
)

@JsonClass(generateAdapter = true)
data class MeResponse(
    val user: UserProfile,
    val plans: Map<String, PlanInfo>? = null
)

@JsonClass(generateAdapter = true)
data class UserProfile(
    val username: String = "User",
    val email: String = "",
    val plan: String = "free",
    @Json(name = "plan_name") val planName: String? = null,
    @Json(name = "ui_lang") val uiLang: String? = null,
    @Json(name = "plan_expires_at") val planExpiresAt: Long? = null,
    val usage: UsageQuota? = null
)

@JsonClass(generateAdapter = true)
data class UsageQuota(
    @Json(name = "messages_24h") val messages24h: Int = 0,
    @Json(name = "messages_24h_limit") val messages24hLimit: Int = 15,
    @Json(name = "messages_4h") val messages4h: Int = 0,
    @Json(name = "messages_4h_limit") val messages4hLimit: Int = 5,
    @Json(name = "think_24h") val think24h: Int = 0,
    @Json(name = "think_24h_limit") val think24hLimit: Int = 2,
    @Json(name = "web_24h") val web24h: Int = 0,
    @Json(name = "web_24h_limit") val web24hLimit: Int = 3
)

@JsonClass(generateAdapter = true)
data class PlanInfo(
    val name: String = "",
    @Json(name = "price_monthly_usd") val priceMonthlyUsd: Double = 0.0,
    @Json(name = "price_yearly_usd") val priceYearlyUsd: Double = 0.0,
    @Json(name = "messages_per_day") val messagesPerDay: Int = 0,
    @Json(name = "messages_per_4h") val messagesPer4h: Int = 0,
    @Json(name = "think_per_day") val thinkPerDay: Int = 0,
    @Json(name = "max_think_tokens") val maxThinkTokens: Int = 0,
    @Json(name = "web_per_day") val webPerDay: Int = 0,
    @Json(name = "max_web_urls") val maxWebUrls: Int = 0,
    @Json(name = "direct_qa") val directQa: Boolean = false
)

@JsonClass(generateAdapter = true)
data class PlansResponse(
    val plans: Map<String, PlanInfo> = emptyMap()
)

@JsonClass(generateAdapter = true)
data class ChatSummary(
    val id: FlexibleId,
    val title: String = "GorphinAI"
)

@JsonClass(generateAdapter = true)
data class ChatsResponse(
    val chats: List<ChatSummary> = emptyList()
)

@JsonClass(generateAdapter = true)
data class SourceItem(
    val title: String? = null,
    val url: String = ""
)

@JsonClass(generateAdapter = true)
data class ChatMessageDto(
    val role: String = "assistant",
    val content: String = "",
    val thinking: String? = null,
    val sources: List<SourceItem>? = null
)

@JsonClass(generateAdapter = true)
data class ChatDetailResponse(
    val messages: List<ChatMessageDto> = emptyList()
)

@JsonClass(generateAdapter = true)
data class SettingsPatchRequest(
    @Json(name = "ui_lang") val uiLang: String,
    val username: String
)

@JsonClass(generateAdapter = true)
data class SettingsResponse(
    val user: UserProfile
)

@JsonClass(generateAdapter = true)
data class CheckoutRequest(
    val plan: String,
    @Json(name = "billing_period") val billingPeriod: String
)

@JsonClass(generateAdapter = true)
data class CheckoutResponse(
    @Json(name = "checkout_url") val checkoutUrl: String
)

@JsonClass(generateAdapter = true)
data class PendingLoadup(
    @Json(name = "tier_name") val tierName: String = "",
    val months: Int = 0,
    val plan: String = ""
)

@JsonClass(generateAdapter = true)
data class LoadupStatusResponse(
    val pending: PendingLoadup? = null
)

@JsonClass(generateAdapter = true)
data class LoadupCheckoutRequest(
    @Json(name = "tier_id") val tierId: String
)

// Domain UI models
enum class ThinkMode(val apiValue: String) {
    AUTO("auto"),
    ON("on"),
    OFF("off");

    fun next(): ThinkMode = when (this) {
        AUTO -> ON
        ON -> OFF
        OFF -> AUTO
    }

    companion object {
        fun fromValue(v: String?): ThinkMode = entries.firstOrNull { it.apiValue == v } ?: AUTO
    }
}

data class ChatMessageUi(
    val id: String,
    val isUser: Boolean,
    val content: String,
    val thinking: String = "",
    val sources: List<SourceItem> = emptyList(),
    val isSearching: Boolean = false,
    val isThinkingLive: Boolean = false,
    val isStreaming: Boolean = false,
    val isError: Boolean = false
)

sealed interface StreamEvent {
    data class ChatMeta(val chatId: String) : StreamEvent
    data object Searching : StreamEvent
    data class Sources(val sources: List<SourceItem>) : StreamEvent
    data object ThinkingStarted : StreamEvent
    data class ThinkDelta(val delta: String) : StreamEvent
    data class TokenDelta(val delta: String) : StreamEvent
    data class UsageUpdate(val usage: UsageQuota) : StreamEvent
    data class Error(val message: String) : StreamEvent
    data object Done : StreamEvent
}

data class LoadUpTier(
    val id: String,
    val title: String,
    val priceUsd: Int,
    val months: Int,
    val planLabel: String,
    val isFeatured: Boolean = false
)

val DEFAULT_LOADUP_TIERS = listOf(
    LoadUpTier("standart_plus", "Standard Plus", 8, 2, "Basic"),
    LoadUpTier("standart_pro", "Standard Pro", 16, 6, "Basic", isFeatured = true),
    LoadUpTier("professional_plus", "Professional Plus", 20, 2, "Plus"),
    LoadUpTier("professional_pro", "Professional Pro", 30, 2, "Pro"),
)
