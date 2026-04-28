package com.thrive.feature.challenge

import android.widget.Toast
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.thrive.domain.model.Challenge

// ── Exact colors from design ───────────────────────────────────────────────────
private val BgMain     = Color(0xFF0D0F14)
private val BgCard     = Color(0xFF161B22)
private val BgCardDark = Color(0xFF0A0D12)
private val GreenMain  = Color(0xFF4CAF50)
private val GreenDark  = Color(0xFF2E7D32)
private val OrangeFire = Color(0xFFFF6B35)
private val TextPrime  = Color(0xFFFFFFFF)
private val TextSec    = Color(0xFF888888)
private val TextMuted  = Color(0xFF444444)
private val BorderCard = Color(0xFF1E2530)
private val PurpleMain = Color(0xFF7C6FCD)
private val PurpleLight= Color(0xFFA78BFA)
private val PurpleDark = Color(0xFF534AB7)
private val PurpleBg   = Color(0xFF2D1F6E)
private val NodeLocked = Color(0xFF1A1A2E)
private val AmberBorder= Color(0xFF854F0B)

private enum class UiChallengeStatus { COMPLETED, ACTIVE, LOCKED }

private data class Section(val label: String, val title: String, val levels: List<Int>, val color: Color)

private val sections = listOf(
    Section("Section 1", "Beginner Focus",  listOf(0, 1, 2), PurpleMain),
    Section("Section 2", "Intermediate",    listOf(3, 4, 5, 6), GreenMain),
    Section("Section 3", "Expert",          listOf(7, 8, 9), OrangeFire)
)

@Composable
fun ChallengeScreen(
    onChallengeClick: (Challenge) -> Unit = {},
    vm: ChallengeViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()
    val context = LocalContext.current
    val today = java.time.LocalDate.now().toString()

    Box(modifier = Modifier.fillMaxSize().background(BgMain)) {
        StarField()

        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator(color = GreenMain)
            }
        } else {
            val challenges = state.challenges
            val alreadyCompletedToday = state.lastChallengeDate == today
            val activeIndex = challenges.indexOfFirst { !it.completed }
            val activeChallenge = if (activeIndex >= 0) challenges.getOrNull(activeIndex) else null
            val showComeAgainTomorrow = {
                Toast.makeText(context, "Come again tomorrow", Toast.LENGTH_SHORT).show()
            }

            LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 40.dp)) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Challenge Path", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextPrime)
                        Surface(shape = RoundedCornerShape(20.dp), color = PurpleBg, border = BorderStroke(0.5.dp, PurpleMain)) {
                            Text(
                                "${challenges.count { it.completed }} / ${challenges.size} done",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                                fontSize = 12.sp,
                                color = PurpleLight,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                sections.forEach { section ->
                    val sectionChallenges = section.levels.mapNotNull { challenges.getOrNull(it) }
                    item { SectionBanner(section) }

                    sectionChallenges.forEachIndexed { index, challenge ->
                        item {
                            val globalIndex = challenges.indexOf(challenge)
                            val isLeft = globalIndex % 2 == 0
                            val status = when {
                                challenge.completed -> UiChallengeStatus.COMPLETED
                                globalIndex == activeIndex && activeIndex >= 0 -> UiChallengeStatus.ACTIVE
                                else -> UiChallengeStatus.LOCKED
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                                    horizontalArrangement = if (isLeft) Arrangement.Start else Arrangement.End
                                ) {
                                    ChallengeNode(
                                        challenge = challenge,
                                        level = globalIndex + 1,
                                        isLeft = isLeft,
                                        accentColor = section.color,
                                        status = status,
                                        onClick = {
                                            when {
                                                status == UiChallengeStatus.LOCKED -> Unit
                                                status == UiChallengeStatus.ACTIVE && alreadyCompletedToday -> showComeAgainTomorrow()
                                                else -> onChallengeClick(challenge)
                                            }
                                        }
                                    )
                                }

                                val next = challenges.getOrNull(globalIndex + 1)
                                val nextStatus = when {
                                    next == null -> UiChallengeStatus.LOCKED
                                    next.completed -> UiChallengeStatus.COMPLETED
                                    challenges.indexOf(next) == activeIndex -> UiChallengeStatus.ACTIVE
                                    else -> UiChallengeStatus.LOCKED
                                }
                                if (index < sectionChallenges.size - 1 || next != null) {
                                    ConnectorLine(status, nextStatus)
                                }
                            }
                        }
                    }
                }

                activeChallenge?.let { active ->
                    item {
                        Spacer(Modifier.height(16.dp))
                        ContinueButton(active) {
                            if (alreadyCompletedToday) showComeAgainTomorrow()
                            else onChallengeClick(active)
                        }
                    }
                }

                item { Spacer(Modifier.height(32.dp)) }
            }
        }
    }
}

@Composable
private fun SectionBanner(section: Section) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(
                Brush.horizontalGradient(listOf(section.color.copy(0.2f), BgCard)),
                RoundedCornerShape(12.dp)
            )
            .border(0.5.dp, section.color.copy(0.4f), RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Column {
            Text(section.label.uppercase(), fontSize = 10.sp, color = TextMuted, letterSpacing = 1.sp)
            Text(section.title, fontSize = 13.sp, color = TextPrime, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun ChallengeNode(
    challenge: Challenge,
    level: Int,
    isLeft: Boolean,
    accentColor: Color,
    status: UiChallengeStatus,
    onClick: () -> Unit
) {
    val isLegendary = level == 10
    Box(contentAlignment = Alignment.Center) {
        when (status) {
            UiChallengeStatus.COMPLETED -> {
                val sparkle by rememberInfiniteTransition(label = "sp").animateFloat(
                    0f, 1f, infiniteRepeatable(tween(1500), RepeatMode.Reverse), label = "spf"
                )
                Box(Modifier.size(72.dp).clip(CircleShape).border(1.5.dp, PurpleDark.copy(sparkle * 0.6f), CircleShape))
                Box(
                    modifier = Modifier.size(64.dp).clip(CircleShape).background(PurpleBg).border(2.5.dp, PurpleLight, CircleShape).clickable { onClick() },
                    contentAlignment = Alignment.Center
                ) { Text("✓", fontSize = 24.sp, color = PurpleLight, fontWeight = FontWeight.Bold) }
                SparklesDots(sparkle)
                LevelBadge(level, PurpleLight, 64)
            }

            UiChallengeStatus.ACTIVE -> {
                val pulse by rememberInfiniteTransition(label = "p1").animateFloat(
                    0f, 1f, infiniteRepeatable(tween(2000), RepeatMode.Restart), label = "p1f"
                )
                val pulse2 by rememberInfiniteTransition(label = "p2").animateFloat(
                    0f, 1f, infiniteRepeatable(tween(2000, delayMillis = 700), RepeatMode.Restart), label = "p2f"
                )
                val bounce by rememberInfiniteTransition(label = "b").animateFloat(
                    0f, -8f, infiniteRepeatable(tween(1200, easing = EaseInOut), RepeatMode.Reverse), label = "bf"
                )

                Box(Modifier.size(96.dp), contentAlignment = Alignment.Center) {
                    Box(
                        Modifier.size((64 + pulse * 32).dp).clip(CircleShape)
                            .border((2 * (1f - pulse)).dp.coerceAtLeast(0.5.dp), accentColor.copy((1f - pulse) * 0.7f), CircleShape)
                    )
                    Box(
                        Modifier.size((64 + pulse2 * 32).dp).clip(CircleShape)
                            .border((2 * (1f - pulse2)).dp.coerceAtLeast(0.5.dp), accentColor.copy((1f - pulse2) * 0.5f), CircleShape)
                    )
                    Text("🧠", fontSize = 24.sp, modifier = Modifier.offset(y = (-52 + bounce).dp))
                    Box(
                        modifier = Modifier.size(64.dp).clip(CircleShape).background(accentColor.copy(0.15f)).border(3.dp, accentColor, CircleShape).clickable { onClick() },
                        contentAlignment = Alignment.Center
                    ) { Text("🎯", fontSize = 24.sp) }
                }
                LevelBadge(level, accentColor, 64)
            }

            UiChallengeStatus.LOCKED -> {
                val size = if (isLegendary) 72 else 64
                Box(
                    modifier = Modifier.size(size.dp).clip(CircleShape)
                        .background(if (isLegendary) Color(0xFF1E1A0D) else NodeLocked)
                        .border(BorderStroke(2.dp, if (isLegendary) AmberBorder.copy(0.6f) else Color(0xFF2E2E50)), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(if (isLegendary) "✦" else "🔒", fontSize = if (isLegendary) 28.sp else 20.sp, color = if (isLegendary) AmberBorder.copy(0.5f) else Color(0xFF2E2E50))
                }
                LevelBadge(level, Color(0xFF2E2E50), size)
            }
        }

        NodeLabel(challenge, isLeft, accentColor, status)
    }
}

@Composable
private fun BoxScope.SparklesDots(alpha: Float) {
    Box(Modifier.size(80.dp)) {
        listOf(4.dp to 8.dp, 68.dp to 18.dp, 16.dp to 66.dp).forEachIndexed { i, (x, y) ->
            Box(
                Modifier.size(if (i == 0) 5.dp else 4.dp)
                    .offset(x, y)
                    .clip(CircleShape)
                    .background(PurpleLight.copy(alpha * (0.6f + i * 0.2f)))
            )
        }
    }
}

@Composable
private fun BoxScope.LevelBadge(level: Int, color: Color, nodeSize: Int) {
    Box(
        modifier = Modifier.size(20.dp).align(Alignment.BottomEnd)
            .offset(x = (-(nodeSize / 2 - 20)).dp)
            .clip(CircleShape).background(BgMain)
            .border(1.5.dp, color.copy(0.5f), CircleShape),
        contentAlignment = Alignment.Center
    ) { Text("$level", fontSize = 9.sp, color = color, fontWeight = FontWeight.Medium) }
}

@Composable
private fun NodeLabel(challenge: Challenge, isLeft: Boolean, accentColor: Color, status: UiChallengeStatus) {
    val nameColor = when (status) {
        UiChallengeStatus.COMPLETED -> PurpleLight
        UiChallengeStatus.ACTIVE -> accentColor
        UiChallengeStatus.LOCKED -> TextMuted
    }
    Box(modifier = Modifier.offset(x = if (isLeft) 80.dp else (-80).dp).width(100.dp)) {
        Column(horizontalAlignment = if (isLeft) Alignment.Start else Alignment.End) {
            Text(challenge.title, fontSize = 11.sp, color = nameColor, fontWeight = FontWeight.Medium, maxLines = 2)
            Text("+${challenge.rewardXp} XP", fontSize = 10.sp, color = nameColor.copy(0.6f), modifier = Modifier.padding(top = 2.dp))
        }
    }
}

@Composable
private fun ConnectorLine(from: UiChallengeStatus, to: UiChallengeStatus) {
    val brush = when {
        from == UiChallengeStatus.COMPLETED && to == UiChallengeStatus.COMPLETED -> Brush.verticalGradient(listOf(PurpleDark, PurpleDark))
        from == UiChallengeStatus.COMPLETED && to == UiChallengeStatus.ACTIVE -> Brush.verticalGradient(listOf(PurpleDark, GreenMain))
        from == UiChallengeStatus.ACTIVE -> Brush.verticalGradient(listOf(GreenDark, BorderCard))
        else -> Brush.verticalGradient(listOf(BorderCard, BorderCard))
    }
    Box(Modifier.width(3.dp).height(40.dp).background(brush, RoundedCornerShape(2.dp)))
}

@Composable
private fun ContinueButton(challenge: Challenge, onClick: () -> Unit) {
    val scale by rememberInfiniteTransition(label = "btn").animateFloat(
        1f, 1.02f, infiniteRepeatable(tween(1000), RepeatMode.Reverse), label = "btnf"
    )
    Box(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
            .scale(scale).clip(RoundedCornerShape(16.dp))
            .background(GreenDark).clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
            Column {
                Text("Current challenge", fontSize = 10.sp, color = GreenMain.copy(0.7f))
                Spacer(Modifier.height(3.dp))
                Text(challenge.title, fontSize = 15.sp, color = TextPrime, fontWeight = FontWeight.Bold)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Surface(shape = RoundedCornerShape(8.dp), color = Color.Black.copy(0.2f)) {
                    Text("+${challenge.rewardXp} XP", Modifier.padding(horizontal = 10.dp, vertical = 5.dp), fontSize = 12.sp, color = GreenMain, fontWeight = FontWeight.Medium)
                }
                Text("→", fontSize = 20.sp, color = TextPrime)
            }
        }
    }
}

@Composable
private fun StarField() {
    val stars = remember {
        List(50) {
            Triple((0..100).random() / 100f, (0..100).random() / 100f, (6..20).random() / 10f)
        }
    }
    Box(Modifier.fillMaxSize()) {
        stars.forEachIndexed { i, (x, y, size) ->
            val alpha by rememberInfiniteTransition(label = "star$i").animateFloat(
                0.1f, 0.8f, infiniteRepeatable(tween((1500..3000).random()), RepeatMode.Reverse), label = "sa$i"
            )
            Box(
                Modifier.fillMaxSize().wrapContentSize(Alignment.TopStart)
                    .offset(x = (x * 320).dp, y = (y * 2000).dp)
                    .size(size.dp).clip(CircleShape)
                    .background(Color.White.copy(alpha))
            )
        }
    }
}

@Composable
fun ChallengeDetailScreen(
    challengeId: String,
    onBack: () -> Unit,
    onStartSession: () -> Unit,
    vm: ChallengeDetailViewModel = hiltViewModel()
) {
    val challenges by vm.challenges.collectAsState()
    val lastChallengeDate by vm.lastChallengeDate.collectAsState()
    val context = LocalContext.current
    val today = java.time.LocalDate.now().toString()
    val idLong = challengeId.toLongOrNull()
    val challenge = challenges.firstOrNull { it.id == idLong }

    if (challenge == null) {
        Box(Modifier.fillMaxSize().background(BgMain), Alignment.Center) {
            CircularProgressIndicator(color = GreenMain)
        }
        return
    }

    val level = challenges.indexOf(challenge) + 1
    val alreadyCompletedToday = lastChallengeDate == today
    val activeIndex = challenges.indexOfFirst { !it.completed }
    val isActive = !challenge.completed && challenges.indexOf(challenge) == activeIndex
    val canStartChallengeToday = isActive && !alreadyCompletedToday
    val progress = (challenge.progress.toFloat() / challenge.target.coerceAtLeast(1)).coerceIn(0f, 1f)

    Column(modifier = Modifier.fillMaxSize().background(BgMain)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, "Back", tint = TextPrime) }
            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("🔥", fontSize = 16.sp)
                    Text("12", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrime)
                }
                Text("Day Streak", fontSize = 10.sp, color = TextSec)
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Box(
                        modifier = Modifier.size(56.dp).clip(CircleShape).background(Color(0xFF0A1A0A)).border(2.5.dp, GreenMain, CircleShape),
                        contentAlignment = Alignment.Center
                    ) { Text("🎯", fontSize = 26.sp) }
                    Column {
                        Text("Level $level – ${challenge.title}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrime)
                        Text("$level of 10", fontSize = 12.sp, color = TextSec)
                    }
                }
            }

            item {
                Text(
                    buildAnnotatedString {
                        val desc = challenge.description
                        val targetStr = "${challenge.target}-minute"
                        val idx = desc.indexOf(targetStr)
                        if (idx >= 0) {
                            append(desc.substring(0, idx))
                            pushStyle(SpanStyle(color = GreenMain, fontWeight = FontWeight.Medium))
                            append(targetStr)
                            pop()
                            append(desc.substring(idx + targetStr.length))
                        } else append(desc)
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSec,
                    lineHeight = 22.sp
                )
            }

            if (isActive) {
                item {
                    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), color = BgCardDark, border = BorderStroke(0.5.dp, BorderCard)) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                                Text("YOUR PROGRESS", fontSize = 10.sp, color = TextMuted, letterSpacing = 0.8.sp)
                                Text("${challenge.progress} / ${challenge.target} min", fontSize = 12.sp, color = GreenMain, fontWeight = FontWeight.Medium)
                            }
                            Spacer(Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                                color = GreenMain,
                                trackColor = Color(0xFF1A2020)
                            )
                        }
                    }
                }
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Surface(modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), color = BgCardDark, border = BorderStroke(0.5.dp, BorderCard)) {
                        Column(Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("TARGET", fontSize = 10.sp, color = TextMuted, letterSpacing = 0.8.sp)
                            Spacer(Modifier.height(8.dp))
                            Text("⏱ ${challenge.target} min", fontSize = 16.sp, color = TextPrime, fontWeight = FontWeight.Bold)
                            Text("Focus Time", fontSize = 11.sp, color = TextSec)
                        }
                    }
                    Surface(modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), color = BgCardDark, border = BorderStroke(0.5.dp, BorderCard)) {
                        Column(Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("REWARD", fontSize = 10.sp, color = TextMuted, letterSpacing = 0.8.sp)
                            Spacer(Modifier.height(8.dp))
                            Text("+${challenge.rewardXp} XP", fontSize = 16.sp, color = GreenMain, fontWeight = FontWeight.Bold)
                            Text("On Completion", fontSize = 11.sp, color = TextSec)
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(8.dp)) }
        }

        Column(modifier = Modifier.fillMaxWidth().background(BgMain).padding(horizontal = 16.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (canStartChallengeToday) {
                Button(
                    onClick = onStartSession,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenMain)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Start Focus Session", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Text("▶", fontSize = 14.sp, color = Color.Black)
                    }
                }
            } else if (isActive && alreadyCompletedToday) {
                Button(
                    onClick = {
                        Toast.makeText(context, "Come again tomorrow", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BgCardDark,
                        contentColor = TextSec
                    )
                ) {
                    Text("Come again tomorrow", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().height(44.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(0.5.dp, BorderCard)
            ) {
                Text("Challenge Details ∨", fontSize = 13.sp, color = TextSec)
            }
        }
    }
}
