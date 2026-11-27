package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.SessionPreferences
import com.example.data.model.ChatMessageUi
import com.example.data.model.ChatSummary
import com.example.data.model.PendingLoadup
import com.example.data.model.PlanInfo
import com.example.data.model.StreamEvent
import com.example.data.model.ThinkMode
import com.example.data.model.UserProfile
import com.example.data.remote.GORPHIN_API_BASE
import com.example.data.repository.ApiResult
import com.example.data.repository.GorphinRepository
import com.example.i18n.AppStrings
import com.example.i18n.Localization
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.util.UUID

enum class AppScreen {
    SPLASH,
    AUTH,
    CHAT,
    PLANS,
    LOADUP,
    SETTINGS,
    ABOUT,
    PRIVACY,
    TERMS
}

sealed interface UiEffect {
    data class OpenExternalUrl(val url: String) : UiEffect
}

data class GorphinUiState(
    val screen: AppScreen = AppScreen.SPLASH,
    val previousScreen: AppScreen = AppScreen.AUTH,
    val uiLang: String = "ru",
    val authToken: String = "",
    val isRegisterMode: Boolean = false,
    val isAuthLoading: Boolean = false,
    val authError: String? = null,
    val user: UserProfile? = null,
    val plans: Map<String, PlanInfo> = emptyMap(),
    val billingPeriod: String = "monthly",
    val plansError: String? = null,
    val isCheckoutLoading: Boolean = false,
    val pendingLoadup: PendingLoadup? = null,
    val loadupError: String? = null,
    val chats: List<ChatSummary> = emptyList(),
    val activeChatId: String? = null,
    val activeChatTitle: String = "GorphinAI",
    val messages: List<ChatMessageUi> = emptyList(),
    val isLoadingChat: Boolean = false,
    val isStreaming: Boolean = false,
    val thinkMode: ThinkMode = ThinkMode.AUTO,
    val webSearchEnabled: Boolean = false,
    val isSavingSettings: Boolean = false,
    val settingsSaved: Boolean = false,
    val settingsError: String? = null
) {
    val strings: AppStrings get() = Localization.forLang(uiLang)
    val isLoggedIn: Boolean get() = authToken.isNotBlank() && user != null
}

class GorphinViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = GorphinRepository(prefs = SessionPreferences(application))

    private val _uiState = MutableStateFlow(GorphinUiState())
    val uiState: StateFlow<GorphinUiState> = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<UiEffect>()
    val effects: SharedFlow<UiEffect> = _effects.asSharedFlow()

    private var streamJob: Job? = null

    init {
        bootstrap()
    }

    private fun bootstrap() {
        viewModelScope.launch {
            val stored = repository.getStoredSession()
            _uiState.update {
                it.copy(
                    uiLang = stored.uiLang,
                    authToken = stored.token,
                    thinkMode = ThinkMode.fromValue(stored.thinkMode),
                    webSearchEnabled = stored.webSearch
                )
            }
            if (stored.token.isBlank()) {
                _uiState.update { it.copy(screen = AppScreen.AUTH) }
                fetchPlansInBackground()
            } else {
                enterSessionWithToken(stored.token)
            }
        }
    }

    fun handleOAuthToken(token: String) {
        if (token.isBlank()) return
        viewModelScope.launch {
            repository.saveToken(token)
            _uiState.update { it.copy(authToken = token, isAuthLoading = true, authError = null) }
            enterSessionWithToken(token)
        }
    }

    private suspend fun enterSessionWithToken(token: String, preloadedUser: UserProfile? = null) {
        val resolvedUser: UserProfile?
        var resolvedPlans = _uiState.value.plans

        if (preloadedUser != null) {
            resolvedUser = preloadedUser
        } else {
            when (val me = repository.getMe(token)) {
                is ApiResult.Success -> {
                    resolvedUser = me.data.user
                    if (!me.data.plans.isNullOrEmpty()) {
                        resolvedPlans = me.data.plans
                    }
                }
                is ApiResult.Error -> {
                    if (me.isUnauthorized) {
                        repository.clearToken()
                    }
                    _uiState.update {
                        it.copy(
                            authToken = if (me.isUnauthorized) "" else it.authToken,
                            user = null,
                            isAuthLoading = false,
                            screen = AppScreen.AUTH,
                            authError = if (me.isUnauthorized) null else me.message
                        )
                    }
                    return
                }
            }
        }

        val resolvedLang = resolvedUser.uiLang?.takeIf { it.isNotBlank() } ?: _uiState.value.uiLang
        repository.saveLanguage(resolvedLang)

        _uiState.update {
            it.copy(
                authToken = token,
                user = resolvedUser,
                uiLang = resolvedLang,
                plans = resolvedPlans,
                isAuthLoading = false,
                authError = null,
                screen = AppScreen.CHAT,
                previousScreen = AppScreen.CHAT
            )
        }

        if (resolvedPlans.isEmpty()) {
            fetchPlansInBackground()
        }
        refreshChats()
        refreshLoadupStatus()
    }

    private fun fetchPlansInBackground() {
        viewModelScope.launch {
            val res = repository.getPlans()
            if (res is ApiResult.Success) {
                _uiState.update { it.copy(plans = res.data) }
            }
        }
    }

    fun setAuthMode(register: Boolean) {
        _uiState.update { it.copy(isRegisterMode = register, authError = null) }
    }

    fun setLanguage(lang: String) {
        viewModelScope.launch {
            repository.saveLanguage(lang)
            _uiState.update { it.copy(uiLang = lang, settingsSaved = false) }
        }
    }

    fun submitAuth(email: String, password: String, username: String) {
        val state = _uiState.value
        val cleanEmail = email.trim()
        if (cleanEmail.isBlank() || password.length < 6) {
            _uiState.update {
                it.copy(authError = state.strings.passwordPlaceholder)
            }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isAuthLoading = true, authError = null) }
            val result = if (state.isRegisterMode) {
                repository.register(cleanEmail, username, password, state.uiLang)
            } else {
                repository.login(cleanEmail, password)
            }
            when (result) {
                is ApiResult.Success -> enterSessionWithToken(result.data.token, result.data.user)
                is ApiResult.Error -> _uiState.update {
                    it.copy(isAuthLoading = false, authError = result.message)
                }
            }
        }
    }

    fun launchOAuth(provider: String) {
        viewModelScope.launch {
            val callback = URLEncoder.encode("gorphinai://oauth", "UTF-8")
            val url = "$GORPHIN_API_BASE/api/auth/oauth/$provider?frontend=$callback"
            _effects.emit(UiEffect.OpenExternalUrl(url))
        }
    }

    fun openExternalUrl(url: String) {
        viewModelScope.launch {
            _effects.emit(UiEffect.OpenExternalUrl(url))
        }
    }

    fun navigateTo(target: AppScreen) {
        val current = _uiState.value.screen
        val backTarget = when (current) {
            AppScreen.CHAT, AppScreen.AUTH -> current
            else -> _uiState.value.previousScreen
        }
        _uiState.update {
            it.copy(
                screen = target,
                previousScreen = backTarget,
                plansError = null,
                loadupError = null,
                settingsSaved = false,
                settingsError = null
            )
        }
        if (target == AppScreen.PLANS && _uiState.value.plans.isEmpty()) {
            fetchPlansInBackground()
        }
        if (target == AppScreen.LOADUP) {
            refreshLoadupStatus()
        }
    }

    fun navigateBack() {
        val state = _uiState.value
        val fallback = if (state.isLoggedIn) AppScreen.CHAT else AppScreen.AUTH
        val dest = if (state.previousScreen == state.screen) fallback else state.previousScreen
        _uiState.update { it.copy(screen = dest) }
    }

    fun refreshChats() {
        val token = _uiState.value.authToken
        if (token.isBlank()) return
        viewModelScope.launch {
            when (val res = repository.getChats(token)) {
                is ApiResult.Success -> _uiState.update { it.copy(chats = res.data) }
                is ApiResult.Error -> if (res.isUnauthorized) forceSignOut()
            }
        }
    }

    fun startNewChat() {
        if (_uiState.value.isStreaming) stopStreaming()
        _uiState.update {
            it.copy(
                activeChatId = null,
                activeChatTitle = "GorphinAI",
                messages = emptyList(),
                screen = AppScreen.CHAT
            )
        }
    }

    fun selectChat(chat: ChatSummary) {
        if (_uiState.value.isStreaming) stopStreaming()
        val token = _uiState.value.authToken
        if (token.isBlank()) return
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    activeChatId = chat.id.value,
                    activeChatTitle = chat.title,
                    isLoadingChat = true,
                    screen = AppScreen.CHAT
                )
            }
            when (val res = repository.getChatMessages(token, chat.id.value)) {
                is ApiResult.Success -> {
                    val mapped = res.data.mapIndexed { idx, dto ->
                        ChatMessageUi(
                            id = "${chat.id.value}_$idx",
                            isUser = dto.role == "user",
                            content = dto.content,
                            thinking = dto.thinking.orEmpty(),
                            sources = dto.sources.orEmpty()
                        )
                    }
                    _uiState.update { it.copy(messages = mapped, isLoadingChat = false) }
                }
                is ApiResult.Error -> {
                    if (res.isUnauthorized) forceSignOut()
                    else _uiState.update { it.copy(isLoadingChat = false) }
                }
            }
        }
    }

    fun deleteChat(chatId: String) {
        val token = _uiState.value.authToken
        if (token.isBlank()) return
        viewModelScope.launch {
            when (val res = repository.deleteChat(token, chatId)) {
                is ApiResult.Success -> {
                    val wasActive = _uiState.value.activeChatId == chatId
                    _uiState.update { state ->
                        state.copy(
                            chats = state.chats.filterNot { it.id.value == chatId },
                            activeChatId = if (wasActive) null else state.activeChatId,
                            activeChatTitle = if (wasActive) "GorphinAI" else state.activeChatTitle,
                            messages = if (wasActive) emptyList() else state.messages
                        )
                    }
                }
                is ApiResult.Error -> if (res.isUnauthorized) forceSignOut()
            }
        }
    }

    fun cycleThinkMode() {
        val next = _uiState.value.thinkMode.next()
        _uiState.update { it.copy(thinkMode = next) }
        viewModelScope.launch { repository.saveThinkMode(next.apiValue) }
    }

    fun toggleWebSearch() {
        val next = !_uiState.value.webSearchEnabled
        _uiState.update { it.copy(webSearchEnabled = next) }
        viewModelScope.launch { repository.saveWebSearch(next) }
    }

    fun stopStreaming() {
        streamJob?.cancel()
        streamJob = null
        _uiState.update { state ->
            state.copy(
                isStreaming = false,
                messages = state.messages.map {
                    if (it.isStreaming) it.copy(
                        isStreaming = false,
                        isSearching = false,
                        isThinkingLive = false
                    ) else it
                }
            )
        }
    }

    fun sendMessage(rawText: String) {
        val text = rawText.trim()
        val state = _uiState.value
        if (text.isEmpty() || state.isStreaming || state.authToken.isBlank()) return

        val userMsg = ChatMessageUi(
            id = "u_${UUID.randomUUID()}",
            isUser = true,
            content = text
        )
        val botId = "b_${UUID.randomUUID()}"
        val botMsg = ChatMessageUi(
            id = botId,
            isUser = false,
            content = "",
            isStreaming = true
        )
        val wasNewChat = state.activeChatId == null

        _uiState.update {
            it.copy(
                messages = it.messages + userMsg + botMsg,
                isStreaming = true
            )
        }

        streamJob = viewModelScope.launch {
            repository.streamChat(
                token = state.authToken,
                chatId = state.activeChatId,
                content = text,
                thinkMode = state.thinkMode.apiValue,
                webSearch = state.webSearchEnabled
            ).collect { event ->
                when (event) {
                    is StreamEvent.ChatMeta -> {
                        _uiState.update { cur ->
                            cur.copy(
                                activeChatId = event.chatId,
                                activeChatTitle = if (wasNewChat) text.take(55) else cur.activeChatTitle
                            )
                        }
                    }
                    StreamEvent.Searching -> updateBotMessage(botId) {
                        it.copy(isSearching = true)
                    }
                    is StreamEvent.Sources -> updateBotMessage(botId) {
                        it.copy(isSearching = false, sources = event.sources)
                    }
                    StreamEvent.ThinkingStarted -> updateBotMessage(botId) {
                        it.copy(isSearching = false, isThinkingLive = true)
                    }
                    is StreamEvent.ThinkDelta -> updateBotMessage(botId) {
                        it.copy(
                            isSearching = false,
                            isThinkingLive = true,
                            thinking = it.thinking + event.delta
                        )
                    }
                    is StreamEvent.TokenDelta -> updateBotMessage(botId) {
                        it.copy(
                            isSearching = false,
                            isThinkingLive = false,
                            content = it.content + event.delta
                        )
                    }
                    is StreamEvent.UsageUpdate -> {
                        _uiState.update { cur ->
                            cur.copy(user = cur.user?.copy(usage = event.usage))
                        }
                    }
                    is StreamEvent.Error -> {
                        updateBotMessage(botId) {
                            it.copy(
                                content = event.message,
                                isSearching = false,
                                isThinkingLive = false,
                                isStreaming = false,
                                isError = true
                            )
                        }
                        _uiState.update { it.copy(isStreaming = false) }
                    }
                    StreamEvent.Done -> {
                        updateBotMessage(botId) {
                            it.copy(
                                content = it.content.trim(),
                                isSearching = false,
                                isThinkingLive = false,
                                isStreaming = false
                            )
                        }
                        _uiState.update { it.copy(isStreaming = false) }
                        refreshChats()
                    }
                }
            }
        }
    }

    private fun updateBotMessage(botId: String, transform: (ChatMessageUi) -> ChatMessageUi) {
        _uiState.update { state ->
            state.copy(
                messages = state.messages.map { msg ->
                    if (msg.id == botId) transform(msg) else msg
                }
            )
        }
    }

    fun setBillingPeriod(period: String) {
        _uiState.update { it.copy(billingPeriod = period) }
    }

    fun startPlanCheckout(planKey: String, overridePeriod: String? = null) {
        val state = _uiState.value
        if (state.authToken.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isCheckoutLoading = true, plansError = null) }
            when (val res = repository.createCheckout(
                token = state.authToken,
                plan = planKey,
                period = overridePeriod ?: state.billingPeriod
            )) {
                is ApiResult.Success -> {
                    _uiState.update { it.copy(isCheckoutLoading = false) }
                    _effects.emit(UiEffect.OpenExternalUrl(res.data))
                }
                is ApiResult.Error -> {
                    _uiState.update { it.copy(isCheckoutLoading = false, plansError = res.message) }
                    if (res.isUnauthorized) forceSignOut()
                }
            }
        }
    }

    fun refreshLoadupStatus() {
        val token = _uiState.value.authToken
        if (token.isBlank()) return
        viewModelScope.launch {
            when (val res = repository.getLoadupStatus(token)) {
                is ApiResult.Success -> _uiState.update { it.copy(pendingLoadup = res.data) }
                is ApiResult.Error -> Unit
            }
        }
    }

    fun startLoadupCheckout(tierId: String) {
        val state = _uiState.value
        if (state.authToken.isBlank()) {
            _uiState.update { it.copy(loadupError = state.strings.loadupLoginHint) }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isCheckoutLoading = true, loadupError = null) }
            when (val res = repository.createLoadupCheckout(state.authToken, tierId)) {
                is ApiResult.Success -> {
                    _uiState.update { it.copy(isCheckoutLoading = false) }
                    _effects.emit(UiEffect.OpenExternalUrl(res.data))
                }
                is ApiResult.Error -> {
                    _uiState.update { it.copy(isCheckoutLoading = false, loadupError = res.message) }
                    if (res.isUnauthorized) forceSignOut()
                }
            }
        }
    }

    fun saveSettings(newLang: String, newUsername: String) {
        val token = _uiState.value.authToken
        if (token.isBlank()) {
            setLanguage(newLang)
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isSavingSettings = true, settingsSaved = false, settingsError = null) }
            when (val res = repository.updateSettings(token, newLang, newUsername.trim())) {
                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            user = res.data,
                            uiLang = res.data.uiLang ?: newLang,
                            isSavingSettings = false,
                            settingsSaved = true
                        )
                    }
                }
                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(isSavingSettings = false, settingsError = res.message)
                    }
                    if (res.isUnauthorized) forceSignOut()
                }
            }
        }
    }

    fun logout() {
        val token = _uiState.value.authToken
        stopStreaming()
        viewModelScope.launch {
            if (token.isNotBlank()) repository.logout(token)
            forceSignOut()
        }
    }

    private fun forceSignOut() {
        streamJob?.cancel()
        _uiState.update {
            GorphinUiState(
                screen = AppScreen.AUTH,
                previousScreen = AppScreen.AUTH,
                uiLang = it.uiLang,
                plans = it.plans
            )
        }
    }
}
