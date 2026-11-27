package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.model.ChatMessageUi
import com.example.data.model.ChatSummary
import com.example.data.model.ThinkMode
import com.example.i18n.AppStrings
import com.example.ui.components.FormattedMessageContent
import com.example.ui.components.GorphinLogoMark
import com.example.ui.components.QuotaSummaryCard
import com.example.ui.components.SearchingStatusChip
import com.example.ui.components.SourcesRow
import com.example.ui.components.ThinkingAccordion
import com.example.ui.components.atmosphericHeroGlow
import com.example.ui.theme.CobaltContainer
import com.example.ui.theme.CobaltOnContainer
import com.example.ui.theme.CobaltPrimary
import com.example.ui.theme.CobaltPrimaryDark
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSidebar
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceElevated
import com.example.ui.theme.StatusError
import com.example.ui.theme.StatusErrorContainer
import com.example.ui.theme.StatusErrorText
import com.example.ui.theme.StatusWarn
import com.example.ui.theme.StatusWarnContainer
import com.example.ui.theme.StatusWarnText
import com.example.ui.theme.ThinkViolet
import com.example.ui.theme.ThinkVioletContainer
import com.example.ui.theme.ThinkVioletText
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.GorphinUiState
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(
    state: GorphinUiState,
    onNewChat: () -> Unit,
    onSelectChat: (ChatSummary) -> Unit,
    onDeleteChat: (String) -> Unit,
    onSendMessage: (String) -> Unit,
    onStopStreaming: () -> Unit,
    onCycleThinkMode: () -> Unit,
    onToggleWebSearch: () -> Unit,
    onRenewPlan: (String) -> Unit,
    onOpenUrl: (String) -> Unit,
    onNavigate: (AppScreen) -> Unit
) {
    var pendingDeleteChat by remember { mutableStateOf<ChatSummary?>(null) }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isExpanded = maxWidth >= 840.dp
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        val drawerContent: @Composable () -> Unit = {
            ChatSidebarContent(
                state = state,
                showCloseButton = !isExpanded,
                onCloseDrawer = { scope.launch { drawerState.close() } },
                onNewChat = {
                    onNewChat()
                    if (!isExpanded) scope.launch { drawerState.close() }
                },
                onSelectChat = { chat ->
                    onSelectChat(chat)
                    if (!isExpanded) scope.launch { drawerState.close() }
                },
                onRequestDeleteChat = { pendingDeleteChat = it },
                onNavigate = { dest ->
                    if (!isExpanded) scope.launch { drawerState.close() }
                    onNavigate(dest)
                }
            )
        }

        if (isExpanded) {
            PermanentNavigationDrawer(
                drawerContent = {
                    PermanentDrawerSheet(
                        modifier = Modifier.width(300.dp),
                        drawerContainerColor = ObsidianSidebar
                    ) {
                        drawerContent()
                    }
                }
            ) {
                ChatWorkspaceBody(
                    state = state,
                    showMenuButton = false,
                    onOpenMenu = {},
                    onSendMessage = onSendMessage,
                    onStopStreaming = onStopStreaming,
                    onCycleThinkMode = onCycleThinkMode,
                    onToggleWebSearch = onToggleWebSearch,
                    onRenewPlan = onRenewPlan,
                    onOpenUrl = onOpenUrl,
                    onNavigate = onNavigate
                )
            }
        } else {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet(
                        modifier = Modifier.widthIn(max = 310.dp),
                        drawerContainerColor = ObsidianSidebar
                    ) {
                        drawerContent()
                    }
                }
            ) {
                ChatWorkspaceBody(
                    state = state,
                    showMenuButton = true,
                    onOpenMenu = { scope.launch { drawerState.open() } },
                    onSendMessage = onSendMessage,
                    onStopStreaming = onStopStreaming,
                    onCycleThinkMode = onCycleThinkMode,
                    onToggleWebSearch = onToggleWebSearch,
                    onRenewPlan = onRenewPlan,
                    onOpenUrl = onOpenUrl,
                    onNavigate = onNavigate
                )
            }
        }
    }

    pendingDeleteChat?.let { target ->
        val s = state.strings
        AlertDialog(
            onDismissRequest = { pendingDeleteChat = null },
            containerColor = ObsidianSurface,
            title = { Text(s.deleteConfirmTitle) },
            text = { Text(s.deleteConfirmBody, color = MaterialTheme.colorScheme.onSurfaceVariant) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteChat(target.id.value)
                        pendingDeleteChat = null
                    },
                    modifier = Modifier.testTag("confirm_delete_chat")
                ) {
                    Text(s.delete, color = StatusErrorText)
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingDeleteChat = null }) {
                    Text(s.cancel)
                }
            }
        )
    }
}

@Composable
private fun ChatSidebarContent(
    state: GorphinUiState,
    showCloseButton: Boolean,
    onCloseDrawer: () -> Unit,
    onNewChat: () -> Unit,
    onSelectChat: (ChatSummary) -> Unit,
    onRequestDeleteChat: (ChatSummary) -> Unit,
    onNavigate: (AppScreen) -> Unit
) {
    val s = state.strings
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(ObsidianSidebar)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Brand Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                GorphinLogoMark(size = 38.dp)
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "GorphinAI",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = CobaltContainer
                        ) {
                            Text(
                                text = "1.0",
                                style = MaterialTheme.typography.labelSmall,
                                color = CobaltOnContainer,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                            )
                        }
                    }
                    Text(
                        text = s.brandShort,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            if (showCloseButton) {
                IconButton(onClick = onCloseDrawer) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(R.string.cd_close_drawer)
                    )
                }
            }
        }

        // New Chat CTA
        Button(
            onClick = onNewChat,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("new_chat_button")
        ) {
            Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
            Text(s.newChat, style = MaterialTheme.typography.labelLarge)
        }

        HorizontalDivider(color = ObsidianBorder)

        // Chat History List
        if (state.chats.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = s.emptyChatsHint,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(state.chats, key = { it.id.value }) { chat ->
                    val active = chat.id.value == state.activeChatId
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (active) ObsidianSurfaceElevated else Color.Transparent,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onSelectChat(chat) }
                            .testTag("chat_item_${chat.id.value}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 12.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = chat.title,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (active) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (active) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { onRequestDeleteChat(chat) },
                                modifier = Modifier.testTag("delete_chat_${chat.id.value}")
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.DeleteOutline,
                                    contentDescription = stringResource(R.string.cd_delete_chat),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // LoadUP & About quick links
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = CobaltContainer.copy(alpha = 0.5f),
                border = BorderStroke(1.dp, CobaltPrimary.copy(alpha = 0.35f)),
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onNavigate(AppScreen.LOADUP) }
                    .testTag("drawer_loadup_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.RocketLaunch,
                        contentDescription = null,
                        tint = CobaltOnContainer,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        text = "LoadUP",
                        style = MaterialTheme.typography.labelMedium,
                        color = CobaltOnContainer
                    )
                }
            }
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = ObsidianSurface,
                border = BorderStroke(1.dp, ObsidianBorder),
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onNavigate(AppScreen.ABOUT) }
                    .testTag("drawer_about_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        text = s.aboutMenu,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        // Quota Summary Card
        val user = state.user
        QuotaSummaryCard(
            planKey = user?.plan ?: "free",
            planName = user?.planName ?: user?.plan ?: "Free",
            usage = user?.usage,
            strings = s,
            onUpgradeClick = { onNavigate(AppScreen.PLANS) }
        )

        // User Profile Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .clickable { onNavigate(AppScreen.SETTINGS) }
                .padding(horizontal = 6.dp, vertical = 4.dp)
                .testTag("drawer_settings_row"),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user?.username ?: "User",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = user?.email ?: "",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(
                onClick = { onNavigate(AppScreen.SETTINGS) },
                modifier = Modifier.testTag("drawer_settings_button")
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = stringResource(R.string.cd_settings),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatWorkspaceBody(
    state: GorphinUiState,
    showMenuButton: Boolean,
    onOpenMenu: () -> Unit,
    onSendMessage: (String) -> Unit,
    onStopStreaming: () -> Unit,
    onCycleThinkMode: () -> Unit,
    onToggleWebSearch: () -> Unit,
    onRenewPlan: (String) -> Unit,
    onOpenUrl: (String) -> Unit,
    onNavigate: (AppScreen) -> Unit
) {
    val s = state.strings
    var promptText by rememberSaveable { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(state.messages.size, state.messages.lastOrNull()?.content?.length, state.messages.lastOrNull()?.thinking?.length) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.lastIndex)
        }
    }

    Scaffold(
        containerColor = ObsidianBg,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ObsidianSurface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                navigationIcon = {
                    if (showMenuButton) {
                        IconButton(
                            onClick = onOpenMenu,
                            modifier = Modifier.testTag("mobile_menu_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = stringResource(R.string.cd_open_drawer)
                            )
                        }
                    }
                },
                title = {
                    Text(
                        text = state.activeChatTitle,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(
                        onClick = { onNavigate(AppScreen.PLANS) },
                        modifier = Modifier.testTag("top_plans_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.WorkspacePremium,
                            contentDescription = stringResource(R.string.cd_plans),
                            tint = CobaltOnContainer
                        )
                    }
                    IconButton(
                        onClick = { onNavigate(AppScreen.SETTINGS) },
                        modifier = Modifier.testTag("top_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = stringResource(R.string.cd_settings)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
        ) {
            // Renewal Banner if paid plan expires within 48 hours
            val user = state.user
            val nowSec = System.currentTimeMillis() / 1000L
            val secLeft = if (user != null && user.plan != "free" && user.planExpiresAt != null) {
                user.planExpiresAt - nowSec
            } else -1L

            if (secLeft in 1..(2 * 86400L) && user != null) {
                val hrs = ((secLeft + 3599) / 3600).toInt().coerceAtLeast(1)
                Surface(
                    color = StatusWarnContainer,
                    border = BorderStroke(1.dp, StatusWarn.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = s.renewPrompt(user.planName ?: user.plan, hrs),
                            style = MaterialTheme.typography.bodySmall,
                            color = StatusWarnText,
                            modifier = Modifier.weight(1f)
                        )
                        Button(
                            onClick = { onRenewPlan(user.plan) },
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("renew_banner_button")
                        ) {
                            Text(s.renewBtn, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }

            // Chat Log or Welcome Hero
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                when {
                    state.isLoadingChat -> {
                        CircularProgressIndicator(color = CobaltPrimary)
                    }
                    state.messages.isEmpty() -> {
                        WelcomeEmptyState(
                            strings = s,
                            onPromptClick = { prompt -> onSendMessage(prompt) }
                        )
                    }
                    else -> {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxSize()
                                .widthIn(max = 820.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            items(state.messages, key = { it.id }) { msg ->
                                ChatMessageBubble(
                                    message = msg,
                                    strings = s,
                                    onOpenUrl = onOpenUrl
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Composer
            ChatComposerBar(
                text = promptText,
                onTextChange = { promptText = it },
                thinkMode = state.thinkMode,
                webEnabled = state.webSearchEnabled,
                isStreaming = state.isStreaming,
                strings = s,
                onCycleThink = onCycleThinkMode,
                onToggleWeb = onToggleWebSearch,
                onSend = {
                    val toSend = promptText.trim()
                    if (toSend.isNotEmpty()) {
                        promptText = ""
                        onSendMessage(toSend)
                    }
                },
                onStop = onStopStreaming
            )
        }
    }
}

@Composable
private fun WelcomeEmptyState(
    strings: AppStrings,
    onPromptClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .atmosphericHeroGlow(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 560.dp)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            GorphinLogoMark(size = 64.dp)
            Text(
                text = strings.welcomeTitle,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            Text(
                text = strings.welcomeSub,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(6.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                strings.starterPrompts.forEachIndexed { idx, prompt ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = ObsidianSurface,
                        border = BorderStroke(1.dp, ObsidianBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onPromptClick(prompt) }
                            .testTag("starter_prompt_$idx")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AutoAwesome,
                                contentDescription = null,
                                tint = CobaltOnContainer,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = prompt,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatMessageBubble(
    message: ChatMessageUi,
    strings: AppStrings,
    onOpenUrl: (String) -> Unit
) {
    val alignment = if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = alignment
    ) {
        if (message.isUser) {
            Surface(
                shape = RoundedCornerShape(16.dp, 16.dp, 4.dp, 16.dp),
                color = CobaltPrimaryDark,
                modifier = Modifier.widthIn(max = 320.dp)
            ) {
                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                )
            }
        } else if (message.isError) {
            Surface(
                shape = RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp),
                color = StatusErrorContainer,
                border = BorderStroke(1.dp, StatusError.copy(alpha = 0.45f)),
                modifier = Modifier.fillMaxWidth(0.92f)
            ) {
                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = StatusErrorText,
                    modifier = Modifier.padding(14.dp)
                )
            }
        } else {
            Surface(
                shape = RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp),
                color = ObsidianSurface,
                border = BorderStroke(1.dp, ObsidianBorder),
                modifier = Modifier.fillMaxWidth(0.94f)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (message.isSearching) {
                        SearchingStatusChip(label = strings.searching)
                    }
                    if (message.sources.isNotEmpty()) {
                        SourcesRow(sources = message.sources, onOpenUrl = onOpenUrl)
                    }
                    if (message.thinking.isNotEmpty() || message.isThinkingLive) {
                        ThinkingAccordion(
                            thinking = message.thinking,
                            isLive = message.isThinkingLive,
                            strings = strings
                        )
                    }
                    if (message.content.isNotEmpty()) {
                        FormattedMessageContent(
                            text = message.content,
                            strings = strings
                        )
                    } else if (message.isStreaming && !message.isSearching && !message.isThinkingLive) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = CobaltPrimary
                            )
                            Text(
                                text = "...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatComposerBar(
    text: String,
    onTextChange: (String) -> Unit,
    thinkMode: ThinkMode,
    webEnabled: Boolean,
    isStreaming: Boolean,
    strings: AppStrings,
    onCycleThink: () -> Unit,
    onToggleWeb: () -> Unit,
    onSend: () -> Unit,
    onStop: () -> Unit
) {
    Surface(
        color = ObsidianBg,
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = ObsidianSurface,
                border = BorderStroke(1.dp, ObsidianBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 820.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 2.dp)
                    ) {
                        if (text.isEmpty()) {
                            Text(
                                text = strings.inputPlaceholder,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        BasicTextField(
                            value = text,
                            onValueChange = onTextChange,
                            textStyle = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            cursorBrush = SolidColor(CobaltPrimary),
                            maxLines = 5,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("chat_prompt_input")
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Think Mode Pill
                            val (thinkBg, thinkBorder, thinkFg, thinkLabel) = when (thinkMode) {
                                ThinkMode.AUTO -> QuadStyle(
                                    ThinkVioletContainer,
                                    ThinkViolet.copy(alpha = 0.55f),
                                    ThinkVioletText,
                                    strings.thinkAuto
                                )
                                ThinkMode.ON -> QuadStyle(
                                    CobaltContainer,
                                    CobaltPrimary,
                                    CobaltOnContainer,
                                    strings.thinkOn
                                )
                                ThinkMode.OFF -> QuadStyle(
                                    ObsidianSurfaceElevated,
                                    ObsidianBorder,
                                    MaterialTheme.colorScheme.onSurfaceVariant,
                                    strings.thinkOff
                                )
                            }
                            ComposerModePill(
                                icon = Icons.Filled.Psychology,
                                label = thinkLabel,
                                bg = thinkBg,
                                border = thinkBorder,
                                contentColor = thinkFg,
                                onClick = onCycleThink,
                                modifier = Modifier.testTag("think_mode_pill")
                            )

                            // Web Search Pill
                            ComposerModePill(
                                icon = Icons.Filled.Language,
                                label = if (webEnabled) strings.webOn else strings.webOff,
                                bg = if (webEnabled) CobaltContainer else ObsidianSurfaceElevated,
                                border = if (webEnabled) CobaltPrimary else ObsidianBorder,
                                contentColor = if (webEnabled) CobaltOnContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                                onClick = onToggleWeb,
                                modifier = Modifier.testTag("web_search_pill")
                            )
                        }

                        if (isStreaming) {
                            Button(
                                onClick = onStop,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = StatusErrorContainer,
                                    contentColor = StatusErrorText
                                ),
                                shape = CircleShape,
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier
                                    .size(44.dp)
                                    .testTag("stop_stream_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Stop,
                                    contentDescription = strings.stopBtn,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        } else {
                            Button(
                                onClick = onSend,
                                enabled = text.isNotBlank(),
                                shape = CircleShape,
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier
                                    .size(44.dp)
                                    .testTag("send_message_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = stringResource(R.string.cd_send_message),
                                    modifier = Modifier.size(19.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private data class QuadStyle(
    val bg: Color,
    val border: Color,
    val fg: Color,
    val label: String
)

@Composable
private fun ComposerModePill(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    bg: Color,
    border: Color,
    contentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = bg,
        border = BorderStroke(1.dp, border),
        modifier = modifier
            .height(36.dp)
            .clip(RoundedCornerShape(999.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(15.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = contentColor,
                maxLines = 1
            )
        }
    }
}
