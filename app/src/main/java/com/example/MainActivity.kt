package com.example

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.GorphinLogoMark
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.LegalDocumentScreen
import com.example.ui.screens.LoadUpScreen
import com.example.ui.screens.PlansScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.CobaltPrimary
import com.example.ui.theme.GorphinTheme
import com.example.ui.theme.ObsidianBg
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.GorphinViewModel
import com.example.ui.viewmodel.UiEffect

class MainActivity : ComponentActivity() {
    private val viewModel: GorphinViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )
        super.onCreate(savedInstanceState)
        extractOAuthTokenFromIntent(intent)

        setContent {
            GorphinTheme {
                GorphinAppRoot(
                    viewModel = viewModel,
                    onOpenExternalUrl = { url -> openExternalUrl(url) }
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        extractOAuthTokenFromIntent(intent)
    }

    private fun extractOAuthTokenFromIntent(intent: Intent?) {
        val data: Uri = intent?.data ?: return
        val fragment = data.fragment.orEmpty()
        val tokenFromFragment = if (fragment.startsWith("oauth_token=")) {
            Uri.decode(fragment.removePrefix("oauth_token="))
        } else null
        val tokenFromQuery = data.getQueryParameter("oauth_token") ?: data.getQueryParameter("token")
        val resolved = tokenFromFragment ?: tokenFromQuery
        if (!resolved.isNullOrBlank()) {
            viewModel.handleOAuthToken(resolved)
        }
    }

    private fun openExternalUrl(url: String) {
        runCatching {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        }
    }
}

@Composable
private fun GorphinAppRoot(
    viewModel: GorphinViewModel,
    onOpenExternalUrl: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is UiEffect.OpenExternalUrl -> onOpenExternalUrl(effect.url)
            }
        }
    }

    when (state.screen) {
        AppScreen.SPLASH -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ObsidianBg),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    GorphinLogoMark(size = 72.dp)
                    Text(
                        text = "GorphinAI",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    CircularProgressIndicator(
                        modifier = Modifier.size(28.dp),
                        strokeWidth = 2.5.dp,
                        color = CobaltPrimary
                    )
                }
            }
        }

        AppScreen.AUTH -> AuthScreen(
            state = state,
            onToggleRegister = viewModel::setAuthMode,
            onSelectLanguage = viewModel::setLanguage,
            onSubmitAuth = viewModel::submitAuth,
            onLaunchOAuth = viewModel::launchOAuth,
            onNavigate = viewModel::navigateTo
        )

        AppScreen.CHAT -> ChatScreen(
            state = state,
            onNewChat = viewModel::startNewChat,
            onSelectChat = viewModel::selectChat,
            onDeleteChat = viewModel::deleteChat,
            onSendMessage = viewModel::sendMessage,
            onStopStreaming = viewModel::stopStreaming,
            onCycleThinkMode = viewModel::cycleThinkMode,
            onToggleWebSearch = viewModel::toggleWebSearch,
            onRenewPlan = { planKey -> viewModel.startPlanCheckout(planKey, "monthly") },
            onOpenUrl = viewModel::openExternalUrl,
            onNavigate = viewModel::navigateTo
        )

        AppScreen.PLANS -> PlansScreen(
            state = state,
            onBack = viewModel::navigateBack,
            onSelectBillingPeriod = viewModel::setBillingPeriod,
            onChoosePlan = { key -> viewModel.startPlanCheckout(key) }
        )

        AppScreen.LOADUP -> LoadUpScreen(
            state = state,
            onBack = viewModel::navigateBack,
            onPreorderTier = viewModel::startLoadupCheckout
        )

        AppScreen.SETTINGS -> SettingsScreen(
            state = state,
            onBack = viewModel::navigateBack,
            onSave = viewModel::saveSettings,
            onLogout = viewModel::logout,
            onNavigate = viewModel::navigateTo
        )

        AppScreen.ABOUT -> AboutScreen(
            state = state,
            onBack = viewModel::navigateBack,
            onNavigate = viewModel::navigateTo
        )

        AppScreen.PRIVACY -> LegalDocumentScreen(
            isPrivacy = true,
            onBack = viewModel::navigateBack
        )

        AppScreen.TERMS -> LegalDocumentScreen(
            isPrivacy = false,
            onBack = viewModel::navigateBack
        )
    }
}
