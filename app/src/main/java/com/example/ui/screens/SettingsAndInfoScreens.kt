package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.components.GorphinLogoMark
import com.example.ui.components.atmosphericHeroGlow
import com.example.ui.theme.CobaltContainer
import com.example.ui.theme.CobaltOnContainer
import com.example.ui.theme.CobaltPrimary
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.StatusError
import com.example.ui.theme.StatusErrorContainer
import com.example.ui.theme.StatusErrorText
import com.example.ui.theme.StatusOk
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.GorphinUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: GorphinUiState,
    onBack: () -> Unit,
    onSave: (lang: String, username: String) -> Unit,
    onLogout: () -> Unit,
    onNavigate: (AppScreen) -> Unit
) {
    BackHandler(onBack = onBack)
    val s = state.strings
    var selectedLang by rememberSaveable(state.uiLang) { mutableStateOf(state.uiLang) }
    var username by rememberSaveable(state.user?.username) {
        mutableStateOf(state.user?.username ?: "")
    }

    Scaffold(
        containerColor = ObsidianBg,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ObsidianSurface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("settings_back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_navigate_back)
                        )
                    }
                },
                title = { Text(s.settingsTitle, style = MaterialTheme.typography.titleLarge) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 600.dp)
                    .navigationBarsPadding(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Surface(
                        shape = MaterialTheme.shapes.large,
                        color = ObsidianSurface,
                        border = BorderStroke(1.dp, ObsidianBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text(
                                text = s.interfaceLang,
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            val langs = listOf(
                                "en" to "English",
                                "uk" to "Українська",
                                "ru" to "Русский"
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                langs.forEach { (code, title) ->
                                    val chosen = selectedLang == code
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (chosen) CobaltContainer else ObsidianBg,
                                        border = BorderStroke(
                                            1.dp,
                                            if (chosen) CobaltPrimary else ObsidianBorder
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable { selectedLang = code }
                                            .testTag("settings_lang_$code")
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(horizontal = 14.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = title,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = if (chosen) FontWeight.SemiBold else FontWeight.Normal,
                                                color = if (chosen) CobaltOnContainer else MaterialTheme.colorScheme.onSurface
                                            )
                                            if (chosen) {
                                                Icon(
                                                    imageVector = Icons.Filled.CheckCircle,
                                                    contentDescription = null,
                                                    tint = CobaltOnContainer,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            OutlinedTextField(
                                value = username,
                                onValueChange = { if (it.length <= 40) username = it },
                                label = { Text(s.usernameLabel) },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("settings_username_input")
                            )

                            if (state.settingsSaved) {
                                Text(
                                    text = s.savedMsg,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = StatusOk
                                )
                            }
                            if (!state.settingsError.isNullOrBlank()) {
                                Text(
                                    text = state.settingsError,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = StatusErrorText
                                )
                            }

                            Button(
                                onClick = { onSave(selectedLang, username) },
                                enabled = !state.isSavingSettings,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("settings_save_button")
                            ) {
                                if (state.isSavingSettings) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(18.dp),
                                        strokeWidth = 2.dp,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                } else {
                                    Text(s.saveBtn)
                                }
                            }
                        }
                    }
                }

                item {
                    Surface(
                        shape = MaterialTheme.shapes.large,
                        color = ObsidianSurface,
                        border = BorderStroke(1.dp, ObsidianBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            SettingsNavRow(
                                icon = Icons.Filled.Info,
                                label = s.aboutMenu,
                                onClick = { onNavigate(AppScreen.ABOUT) },
                                modifier = Modifier.testTag("settings_about_row")
                            )
                            HorizontalDivider(color = ObsidianBorder)
                            SettingsNavRow(
                                icon = Icons.Filled.PrivacyTip,
                                label = s.privacyMenu,
                                onClick = { onNavigate(AppScreen.PRIVACY) },
                                modifier = Modifier.testTag("settings_privacy_row")
                            )
                            HorizontalDivider(color = ObsidianBorder)
                            SettingsNavRow(
                                icon = Icons.Filled.Gavel,
                                label = s.termsMenu,
                                onClick = { onNavigate(AppScreen.TERMS) },
                                modifier = Modifier.testTag("settings_terms_row")
                            )
                        }
                    }
                }

                if (state.isLoggedIn) {
                    item {
                        Button(
                            onClick = onLogout,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = StatusErrorContainer,
                                contentColor = StatusErrorText
                            ),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, StatusError.copy(alpha = 0.4f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("settings_logout_button")
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(s.logoutBtn)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsNavRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CobaltOnContainer,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    state: GorphinUiState,
    onBack: () -> Unit,
    onNavigate: (AppScreen) -> Unit
) {
    BackHandler(onBack = onBack)
    val s = state.strings

    Scaffold(
        containerColor = ObsidianBg,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ObsidianSurface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("about_back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_navigate_back)
                        )
                    }
                },
                title = { Text(s.aboutMenu, style = MaterialTheme.typography.titleLarge) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .atmosphericHeroGlow()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 680.dp)
                    .navigationBarsPadding(),
                contentPadding = PaddingValues(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        GorphinLogoMark(size = 64.dp)
                        Text(
                            text = "Text-Based AI Assistant for Coding, Reasoning & Productivity",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "GorphinAI 1.0 is a text-only conversational AI workspace developed by the Ukrainian team Gorphin. It helps developers, students, and researchers write code, debug software, analyze documentation, and organize technical knowledge in English, Ukrainian, and Russian.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                item {
                    FeatureInfoCard(
                        icon = Icons.Filled.Psychology,
                        title = "Step-by-Step Reasoning",
                        description = "Dedicated <think> mode for software architecture, algorithm design, debugging, and structured problem solving."
                    )
                }
                item {
                    FeatureInfoCard(
                        icon = Icons.Filled.Language,
                        title = "Web & GitHub Context",
                        description = "Fetch and summarize public documentation URLs and GitHub source files directly inside your text chat."
                    )
                }
                item {
                    FeatureInfoCard(
                        icon = Icons.Filled.Code,
                        title = "Simple Authentication & Plans",
                        description = "Sign in with Email, Google, or GitHub. Start free with daily quotas or upgrade anytime."
                    )
                }
                item {
                    Surface(
                        shape = MaterialTheme.shapes.large,
                        color = ObsidianSurface,
                        border = BorderStroke(1.dp, ObsidianBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.VerifiedUser,
                                    contentDescription = null,
                                    tint = CobaltOnContainer,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "Application Purpose & Google API Compliance",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Text(
                                text = "Text-Only Assistant: GorphinAI is strictly a text-based AI chat and coding assistant. It does not generate images, video, or audio, and does not use Google APIs for harmful or illegal content.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Google OAuth Usage: Google Sign-In is used exclusively to authenticate users via their email and basic profile name.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Button(
                                    onClick = { onNavigate(AppScreen.PRIVACY) },
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(s.privacyMenu)
                                }
                                Button(
                                    onClick = { onNavigate(AppScreen.TERMS) },
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(s.termsMenu)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FeatureInfoCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = ObsidianSurface,
        border = BorderStroke(1.dp, ObsidianBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CobaltOnContainer,
                modifier = Modifier.size(22.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(title, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)
                Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LegalDocumentScreen(
    isPrivacy: Boolean,
    onBack: () -> Unit
) {
    BackHandler(onBack = onBack)
    val title = if (isPrivacy) "Privacy Policy" else "Terms of Service"

    val sections = if (isPrivacy) {
        listOf(
            "Effective Date: November 25, 2026" to
                "GorphinAI (\"we\", \"our\", \"us\"), developed by the Gorphin Team, is committed to protecting your privacy when using https://gorphinai.pp.ua and the GorphinAI mobile application.",
            "1. Information We Collect" to
                "• Account & Authentication Data: Email address, display name/username, hashed password or OAuth verification status.\n• Google User Data Accessed: Only minimal scopes (openid, email, profile) to authenticate your GorphinAI account.\n• Chat & Usage Data: Text prompts, AI responses, chat titles, and daily quota counters.\n• Billing Data: Subscription tier, period, and PayGate.to order IDs (we do not store credit card numbers).",
            "2. How We Use Your Information" to
                "To authenticate your identity, deliver AI text responses, save chat history, enforce plan quotas, and process subscription upgrades.",
            "3. Google API Services User Data Policy (Limited Use)" to
                "We only use Google user data (email and basic profile name) to authenticate you and display your username inside GorphinAI. We do not sell Google user data, use it for advertising, transfer it to third parties, or train general-purpose AI/ML models on it.",
            "4. Data Sharing & Infrastructure" to
                "Data is shared only with essential infrastructure providers: Database Hosting (Turso / SQLite), Payment Processing (PayGate.to), and Email Delivery.",
            "5. Data Retention & Deletion" to
                "You may delete individual chats at any time inside the app, or request full account deletion by emailing support@gorphinai.pp.ua.",
            "6. Contact" to
                "Questions? Contact the Gorphin Team at support@gorphinai.pp.ua."
        )
    } else {
        listOf(
            "Last updated: November 25, 2026" to
                "By accessing or using GorphinAI, you agree to be bound by these Terms of Service.",
            "1. Use of Service" to
                "You agree to use GorphinAI in compliance with applicable laws and not to abuse, reverse-engineer, or attempt to bypass rate limits or security controls.",
            "2. Accounts" to
                "You are responsible for maintaining the confidentiality of your account credentials and for all activities that occur under your account.",
            "3. Subscriptions & Payments" to
                "Paid plans (Basic, Plus, Pro) are billed according to the selected monthly or yearly period via PayGate.to. Plan quotas reset according to the schedule shown in the application.",
            "4. Disclaimer" to
                "AI-generated responses may contain inaccuracies. The service is provided \"as is\" without warranties of any kind.",
            "5. Contact" to
                "Questions about these Terms can be sent to support@gorphinai.pp.ua."
        )
    }

    Scaffold(
        containerColor = ObsidianBg,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ObsidianSurface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("legal_back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_navigate_back)
                        )
                    }
                },
                title = { Text(title, style = MaterialTheme.typography.titleLarge) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 680.dp)
                    .navigationBarsPadding(),
                contentPadding = PaddingValues(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                sections.forEach { (heading, body) ->
                    item {
                        Surface(
                            shape = MaterialTheme.shapes.medium,
                            color = ObsidianSurface,
                            border = BorderStroke(1.dp, ObsidianBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = heading,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = body,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
