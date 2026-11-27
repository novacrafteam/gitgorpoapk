package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.model.DEFAULT_LOADUP_TIERS
import com.example.data.model.PlanInfo
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
import com.example.ui.theme.StatusOkContainer
import com.example.ui.theme.StatusOkText
import com.example.ui.viewmodel.GorphinUiState
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlansScreen(
    state: GorphinUiState,
    onBack: () -> Unit,
    onSelectBillingPeriod: (String) -> Unit,
    onChoosePlan: (String) -> Unit
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
                    IconButton(onClick = onBack, modifier = Modifier.testTag("plans_back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_navigate_back)
                        )
                    }
                },
                title = { Text(s.tariffsTitle, style = MaterialTheme.typography.titleLarge) }
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
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = s.tariffsSubtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )

                        // Monthly / Yearly toggle
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = ObsidianSurface,
                            border = BorderStroke(1.dp, ObsidianBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(4.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                BillingToggleButton(
                                    label = s.monthly,
                                    selected = state.billingPeriod == "monthly",
                                    onClick = { onSelectBillingPeriod("monthly") },
                                    modifier = Modifier.testTag("billing_monthly_btn")
                                )
                                BillingToggleButton(
                                    label = s.yearly,
                                    selected = state.billingPeriod == "yearly",
                                    onClick = { onSelectBillingPeriod("yearly") },
                                    modifier = Modifier.testTag("billing_yearly_btn")
                                )
                            }
                        }
                    }
                }

                if (!state.plansError.isNullOrBlank()) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = StatusErrorContainer,
                            border = BorderStroke(1.dp, StatusError.copy(alpha = 0.45f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = state.plansError,
                                style = MaterialTheme.typography.bodySmall,
                                color = StatusErrorText,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }

                if (state.plans.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(36.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = CobaltPrimary)
                        }
                    }
                } else {
                    val orderedKeys = listOf("free", "basic", "plus", "pro")
                    orderedKeys.forEach { key ->
                        val plan = state.plans[key]
                        if (plan != null) {
                            item(key = key) {
                                PlanOptionCard(
                                    planKey = key,
                                    plan = plan,
                                    isCurrent = state.user?.plan == key,
                                    billingPeriod = state.billingPeriod,
                                    isCheckoutLoading = state.isCheckoutLoading,
                                    strings = s,
                                    onSelect = { onChoosePlan(key) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BillingToggleButton(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(9.dp),
        color = if (selected) CobaltPrimary else ObsidianSurface,
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(9.dp))
            .clickable(onClick = onClick)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun PlanOptionCard(
    planKey: String,
    plan: PlanInfo,
    isCurrent: Boolean,
    billingPeriod: String,
    isCheckoutLoading: Boolean,
    strings: com.example.i18n.AppStrings,
    onSelect: () -> Unit
) {
    val isPopular = planKey == "plus"
    val rawPrice = if (billingPeriod == "yearly") plan.priceYearlyUsd else plan.priceMonthlyUsd
    val priceDisplay = if (rawPrice % 1.0 == 0.0) rawPrice.roundToInt().toString() else String.format("%.2f", rawPrice)
    val suffix = when {
        planKey == "free" -> ""
        billingPeriod == "yearly" -> strings.perYr
        else -> strings.perMo
    }

    val features = buildList {
        add(strings.featMsg(plan.messagesPerDay, plan.messagesPer4h))
        add(strings.featThink(plan.thinkPerDay, plan.maxThinkTokens))
        add(strings.featWeb(plan.webPerDay, plan.maxWebUrls))
        if (plan.directQa) add(strings.featProExtra)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = ObsidianSurface,
        border = BorderStroke(
            width = if (isPopular) 1.5.dp else 1.dp,
            color = if (isPopular) CobaltPrimary else ObsidianBorder
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = plan.name.ifBlank { planKey.replaceFirstChar { it.uppercase() } },
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (isPopular) {
                    Surface(
                        shape = RoundedCornerShape(999.dp),
                        color = CobaltContainer
                    ) {
                        Text(
                            text = strings.popularBadge,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = CobaltOnContainer,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "$$priceDisplay",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (suffix.isNotEmpty()) {
                    Text(
                        text = suffix,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }

            Text(
                text = strings.planRoles[planKey].orEmpty(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                features.forEach { feat ->
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = StatusOk,
                            modifier = Modifier
                                .padding(top = 2.dp)
                                .size(16.dp)
                        )
                        Text(
                            text = feat,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(Modifier.height(4.dp))

            when {
                planKey == "free" -> {
                    OutlinedButton(
                        onClick = {},
                        enabled = false,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(if (isCurrent) strings.currentPlan else "Free")
                    }
                }
                isCurrent -> {
                    OutlinedButton(
                        onClick = onSelect,
                        enabled = !isCheckoutLoading,
                        border = BorderStroke(1.dp, CobaltPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("plan_renew_$planKey")
                    ) {
                        Text(strings.renewBtn, color = CobaltOnContainer)
                    }
                }
                else -> {
                    Button(
                        onClick = onSelect,
                        enabled = !isCheckoutLoading,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("plan_choose_$planKey")
                    ) {
                        Text(strings.choosePlan)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadUpScreen(
    state: GorphinUiState,
    onBack: () -> Unit,
    onPreorderTier: (String) -> Unit
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
                    IconButton(onClick = onBack, modifier = Modifier.testTag("loadup_back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_navigate_back)
                        )
                    }
                },
                title = { Text("GorphinAI LoadUP", style = MaterialTheme.typography.titleLarge) }
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
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(999.dp),
                            color = CobaltContainer
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.RocketLaunch,
                                    contentDescription = null,
                                    tint = CobaltOnContainer,
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = s.loadupBadge,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = CobaltOnContainer
                                )
                            }
                        }

                        Text(
                            text = s.loadupSubtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                state.pendingLoadup?.let { pending ->
                    item {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = StatusOkContainer,
                            border = BorderStroke(1.dp, StatusOk.copy(alpha = 0.45f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Pre-order active: ${pending.tierName} (${pending.months} mo ${pending.plan}) — activates Feb 1, 00:00 UTC",
                                style = MaterialTheme.typography.bodySmall,
                                color = StatusOkText,
                                modifier = Modifier.padding(14.dp)
                            )
                        }
                    }
                }

                if (!state.loadupError.isNullOrBlank()) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = StatusErrorContainer,
                            border = BorderStroke(1.dp, StatusError.copy(alpha = 0.45f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = state.loadupError,
                                style = MaterialTheme.typography.bodySmall,
                                color = StatusErrorText,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }

                DEFAULT_LOADUP_TIERS.forEach { tier ->
                    item(key = tier.id) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.large,
                            color = ObsidianSurface,
                            border = BorderStroke(
                                width = if (tier.isFeatured) 1.5.dp else 1.dp,
                                color = if (tier.isFeatured) CobaltPrimary else ObsidianBorder
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(18.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = tier.title,
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "$${tier.priceUsd}",
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = CobaltOnContainer
                                    )
                                }
                                Text(
                                    text = "${tier.months} months of ${tier.planLabel} plan • Activates Feb 1, 00:00 UTC",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(Modifier.height(4.dp))
                                Button(
                                    onClick = { onPreorderTier(tier.id) },
                                    enabled = !state.isCheckoutLoading,
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("loadup_buy_${tier.id}")
                                ) {
                                    Text("${s.loadupPreorderBtn} $${tier.priceUsd}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
