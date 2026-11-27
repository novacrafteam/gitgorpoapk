package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.SourceItem
import com.example.data.model.UsageQuota
import com.example.i18n.AppStrings
import com.example.ui.theme.CobaltContainer
import com.example.ui.theme.CobaltOnContainer
import com.example.ui.theme.CobaltPrimary
import com.example.ui.theme.CyanSecondary
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianCodeBg
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceElevated
import com.example.ui.theme.ProMagenta
import com.example.ui.theme.ProPurple
import com.example.ui.theme.StatusOk
import com.example.ui.theme.StatusOkContainer
import com.example.ui.theme.StatusOkText
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.ThinkViolet
import com.example.ui.theme.ThinkVioletContainer
import com.example.ui.theme.ThinkVioletText
import kotlinx.coroutines.delay

@Composable
fun GorphinLogoMark(
    size: Dp = 40.dp,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.ic_gorphin_logo_1795789320497),
        contentDescription = stringResource(R.string.cd_app_logo),
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(size * 0.28f))
            .border(1.dp, CobaltPrimary.copy(alpha = 0.45f), RoundedCornerShape(size * 0.28f))
    )
}

fun Modifier.atmosphericHeroGlow(): Modifier = this.drawBehind {
    drawRect(
        brush = Brush.radialGradient(
            colors = listOf(
                CobaltPrimary.copy(alpha = 0.16f),
                CyanSecondary.copy(alpha = 0.05f),
                Color.Transparent
            ),
            center = Offset(size.width * 0.5f, size.height * 0.15f),
            radius = size.maxDimension * 0.7f
        )
    )
}

@Composable
fun PlanBadge(
    planKey: String,
    planLabel: String,
    modifier: Modifier = Modifier
) {
    val key = planKey.lowercase()
    val shape = RoundedCornerShape(999.dp)
    if (key == "pro") {
        Box(
            modifier = modifier
                .clip(shape)
                .background(Brush.linearGradient(listOf(ProPurple, ProMagenta)))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text(
                text = planLabel.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    } else {
        val (bg, fg) = when (key) {
            "basic" -> StatusOkContainer to StatusOkText
            "plus" -> CobaltContainer to CobaltOnContainer
            else -> ObsidianSurfaceElevated to TextSecondary
        }
        Box(
            modifier = modifier
                .clip(shape)
                .background(bg)
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text(
                text = planLabel.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = fg
            )
        }
    }
}

@Composable
fun QuotaSummaryCard(
    planKey: String,
    planName: String,
    usage: UsageQuota?,
    strings: AppStrings,
    onUpgradeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = ObsidianSurface,
        border = BorderStroke(1.dp, ObsidianBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PlanBadge(planKey = planKey, planLabel = planName)
                TextButton(
                    onClick = onUpgradeClick,
                    modifier = Modifier.testTag("drawer_plans_button")
                ) {
                    Text(
                        text = strings.upgradeBtn,
                        style = MaterialTheme.typography.labelMedium,
                        color = CobaltOnContainer,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            val u = usage ?: UsageQuota()
            QuotaMetricRow(
                label = strings.qMessages,
                value = "${u.messages24h}/${u.messages24hLimit} · ${u.messages4h}/${u.messages4hLimit}",
                progress = (u.messages24h.toFloat() / u.messages24hLimit.coerceAtLeast(1)).coerceIn(0f, 1f),
                barColor = CobaltPrimary
            )
            QuotaMetricRow(
                label = strings.qThink,
                value = "${u.think24h}/${u.think24hLimit}",
                progress = (u.think24h.toFloat() / u.think24hLimit.coerceAtLeast(1)).coerceIn(0f, 1f),
                barColor = ThinkViolet
            )
            QuotaMetricRow(
                label = strings.qWeb,
                value = "${u.web24h}/${u.web24hLimit}",
                progress = (u.web24h.toFloat() / u.web24hLimit.coerceAtLeast(1)).coerceIn(0f, 1f),
                barColor = CyanSecondary
            )
        }
    }
}

@Composable
private fun QuotaMetricRow(
    label: String,
    value: String,
    progress: Float,
    barColor: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(CircleShape),
            color = barColor,
            trackColor = ObsidianSurfaceElevated,
        )
    }
}

@Composable
fun SearchingStatusChip(
    label: String,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "search_pulse")
    val alpha by transition.animateFloat(
        initialValue = 0.55f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(750), RepeatMode.Reverse),
        label = "search_alpha"
    )
    Surface(
        modifier = modifier.alpha(alpha),
        shape = RoundedCornerShape(10.dp),
        color = CobaltPrimary.copy(alpha = 0.14f),
        border = BorderStroke(1.dp, CobaltPrimary.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Language,
                contentDescription = null,
                tint = CobaltOnContainer,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = CobaltOnContainer
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SourcesRow(
    sources: List<SourceItem>,
    onOpenUrl: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (sources.isEmpty()) return
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        sources.forEach { src ->
            Surface(
                shape = RoundedCornerShape(999.dp),
                color = ObsidianSurfaceElevated,
                border = BorderStroke(1.dp, ObsidianBorder),
                modifier = Modifier.clickable { onOpenUrl(src.url) }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Link,
                        contentDescription = null,
                        tint = CobaltOnContainer,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = src.title?.takeIf { it.isNotBlank() } ?: src.url,
                        style = MaterialTheme.typography.labelSmall,
                        color = CobaltOnContainer,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun ThinkingAccordion(
    thinking: String,
    isLive: Boolean,
    strings: AppStrings,
    modifier: Modifier = Modifier
) {
    var expanded by remember(isLive) { mutableStateOf(isLive) }
    val pulse = rememberInfiniteTransition(label = "think_pulse")
    val liveAlpha by pulse.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(700), RepeatMode.Reverse),
        label = "think_alpha"
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = ThinkVioletContainer.copy(alpha = 0.55f),
        border = BorderStroke(1.dp, ThinkViolet.copy(alpha = 0.35f))
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.alpha(if (isLive) liveAlpha else 1f)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Psychology,
                        contentDescription = null,
                        tint = ThinkVioletText,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = if (isLive) strings.thinkingLive else strings.thinkingDone,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = ThinkVioletText
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = null,
                    tint = ThinkVioletText,
                    modifier = Modifier.size(18.dp)
                )
            }

            AnimatedVisibility(
                visible = expanded && thinking.isNotEmpty(),
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(ThinkViolet.copy(alpha = 0.25f))
                    )
                    Text(
                        text = thinking,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}

private sealed interface MessageSegment {
    data class PlainText(val text: String) : MessageSegment
    data class CodeBlock(val language: String, val code: String) : MessageSegment
}

private fun parseMessageSegments(raw: String): List<MessageSegment> {
    val regex = Regex("```([a-zA-Z0-9_+\\-#.]*)\\n([\\s\\S]*?)```")
    val segments = mutableListOf<MessageSegment>()
    var lastIndex = 0
    for (match in regex.findAll(raw)) {
        if (match.range.first > lastIndex) {
            val plain = raw.substring(lastIndex, match.range.first).trim('\n')
            if (plain.isNotEmpty()) segments.add(MessageSegment.PlainText(plain))
        }
        val lang = match.groupValues[1].ifBlank { "code" }
        val code = match.groupValues[2].trimEnd('\n')
        segments.add(MessageSegment.CodeBlock(lang, code))
        lastIndex = match.range.last + 1
    }
    if (lastIndex < raw.length) {
        val tail = raw.substring(lastIndex).trimStart('\n')
        if (tail.isNotEmpty()) segments.add(MessageSegment.PlainText(tail))
    }
    if (segments.isEmpty() && raw.isNotEmpty()) {
        segments.add(MessageSegment.PlainText(raw))
    }
    return segments
}

@Composable
fun FormattedMessageContent(
    text: String,
    strings: AppStrings,
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    val segments = remember(text) { parseMessageSegments(text) }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        segments.forEach { seg ->
            when (seg) {
                is MessageSegment.PlainText -> {
                    Text(
                        text = seg.text,
                        style = MaterialTheme.typography.bodyLarge,
                        color = textColor
                    )
                }
                is MessageSegment.CodeBlock -> {
                    CodeBlockCard(
                        language = seg.language,
                        code = seg.code,
                        strings = strings
                    )
                }
            }
        }
    }
}

@Composable
fun CodeBlockCard(
    language: String,
    code: String,
    strings: AppStrings
) {
    val clipboard = LocalClipboardManager.current
    var copied by remember { mutableStateOf(false) }

    LaunchedEffect(copied) {
        if (copied) {
            delay(1400)
            copied = false
        }
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = ObsidianCodeBg,
        border = BorderStroke(1.dp, ObsidianBorder)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ObsidianSurfaceElevated.copy(alpha = 0.6f))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = language.lowercase(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = JetBrainsMonoFontFamily
                    ),
                    color = TextSecondary
                )
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable {
                            clipboard.setText(AnnotatedString(code))
                            copied = true
                        }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = if (copied) Icons.Filled.Check else Icons.Filled.ContentCopy,
                        contentDescription = stringResource(R.string.cd_copy_code),
                        tint = if (copied) StatusOk else CobaltOnContainer,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = if (copied) strings.copied else strings.copy,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (copied) StatusOk else CobaltOnContainer
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(12.dp)
            ) {
                Text(
                    text = code,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 12.5.sp,
                        lineHeight = 19.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
