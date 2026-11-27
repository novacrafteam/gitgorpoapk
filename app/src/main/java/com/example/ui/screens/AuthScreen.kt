package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
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
import com.example.ui.theme.ObsidianSurfaceElevated
import com.example.ui.theme.StatusError
import com.example.ui.theme.StatusErrorContainer
import com.example.ui.theme.StatusErrorText
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.GorphinUiState

@Composable
fun AuthScreen(
    state: GorphinUiState,
    onToggleRegister: (Boolean) -> Unit,
    onSelectLanguage: (String) -> Unit,
    onSubmitAuth: (email: String, password: String, username: String) -> Unit,
    onLaunchOAuth: (String) -> Unit,
    onNavigate: (AppScreen) -> Unit
) {
    val s = state.strings
    var username by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var showPassword by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBg)
            .atmosphericHeroGlow()
            .safeDrawingPadding()
            .imePadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 460.dp)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top bar with Language Pills
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GorphinLogoMark(size = 44.dp)
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "GorphinAI",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = CobaltContainer
                            ) {
                                Text(
                                    text = "1.0",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CobaltOnContainer,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = s.brandSubtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Compact Language Selector Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("ru" to "RU", "uk" to "UA", "en" to "EN").forEach { (code, label) ->
                    val selected = state.uiLang == code
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (selected) CobaltContainer else ObsidianSurface,
                        border = BorderStroke(
                            1.dp,
                            if (selected) CobaltPrimary else ObsidianBorder
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onSelectLanguage(code) }
                            .testTag("lang_chip_$code")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelLarge,
                                color = if (selected) CobaltOnContainer else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Main Auth Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                color = ObsidianSurface,
                border = BorderStroke(1.dp, ObsidianBorder),
                tonalElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Login / Register Tabs
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = ObsidianBg
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            AuthSegmentTab(
                                label = s.loginTab,
                                selected = !state.isRegisterMode,
                                onClick = { onToggleRegister(false) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("auth_tab_login")
                            )
                            AuthSegmentTab(
                                label = s.registerTab,
                                selected = state.isRegisterMode,
                                onClick = { onToggleRegister(true) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("auth_tab_register")
                            )
                        }
                    }

                    AnimatedVisibility(visible = state.isRegisterMode) {
                        OutlinedTextField(
                            value = username,
                            onValueChange = { if (it.length <= 40) username = it },
                            label = { Text(s.usernameLabel) },
                            placeholder = { Text("Alex") },
                            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("auth_username_input")
                        )
                    }

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(s.emailLabel) },
                        placeholder = { Text("you@example.com") },
                        leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_email_input")
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(s.passwordLabel) },
                        placeholder = { Text(s.passwordPlaceholder) },
                        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                        trailingIcon = {
                            IconButton(
                                onClick = { showPassword = !showPassword },
                                modifier = Modifier.testTag("toggle_password_visibility")
                            ) {
                                Icon(
                                    imageVector = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = stringResource(R.string.cd_toggle_password)
                                )
                            }
                        },
                        singleLine = true,
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { onSubmitAuth(email, password, username) }
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_password_input")
                    )

                    if (!state.authError.isNullOrBlank()) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = StatusErrorContainer,
                            border = BorderStroke(1.dp, StatusError.copy(alpha = 0.45f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = state.authError,
                                style = MaterialTheme.typography.bodySmall,
                                color = StatusErrorText,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }

                    Button(
                        onClick = { onSubmitAuth(email, password, username) },
                        enabled = !state.isAuthLoading,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("auth_submit_button")
                    ) {
                        if (state.isAuthLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text(
                                text = if (state.isRegisterMode) s.registerBtn else s.loginBtn,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f), color = ObsidianBorder)
                        Text(
                            text = s.orContinueWith,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        HorizontalDivider(modifier = Modifier.weight(1f), color = ObsidianBorder)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onLaunchOAuth("google") },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, ObsidianBorder),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("oauth_google_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Bolt,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text("Google")
                        }
                        OutlinedButton(
                            onClick = { onLaunchOAuth("github") },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, ObsidianBorder),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("oauth_github_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Code,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text("GitHub")
                        }
                    }
                    Text(
                        text = s.oauthTip,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // LoadUP Pre-Order Banner
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = CobaltContainer.copy(alpha = 0.55f),
                border = BorderStroke(1.dp, CobaltPrimary.copy(alpha = 0.45f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .clickable { onNavigate(AppScreen.LOADUP) }
                    .testTag("auth_loadup_banner")
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.RocketLaunch,
                        contentDescription = null,
                        tint = CobaltOnContainer,
                        modifier = Modifier.size(22.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "GorphinAI LoadUP",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = s.loadupBadge,
                            style = MaterialTheme.typography.bodySmall,
                            color = CobaltOnContainer
                        )
                    }
                }
            }

            // Footer Legal & About links
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(
                    onClick = { onNavigate(AppScreen.ABOUT) },
                    modifier = Modifier.testTag("auth_about_link")
                ) {
                    Text(s.aboutMenu, style = MaterialTheme.typography.labelMedium)
                }
                TextButton(
                    onClick = { onNavigate(AppScreen.PRIVACY) },
                    modifier = Modifier.testTag("auth_privacy_link")
                ) {
                    Text(s.privacyMenu, style = MaterialTheme.typography.labelMedium)
                }
                TextButton(
                    onClick = { onNavigate(AppScreen.TERMS) },
                    modifier = Modifier.testTag("auth_terms_link")
                ) {
                    Text(s.termsMenu, style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}

@Composable
private fun AuthSegmentTab(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(9.dp),
        color = if (selected) ObsidianSurfaceElevated else ObsidianBg,
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(9.dp))
            .clickable(onClick = onClick)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                color = if (selected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
