package com.thrive.feature.home

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import java.time.LocalDate

private val BgMain      = Color(0xFF080B12)
private val BgCard      = Color(0xFF0D0F18)
private val BgCardDark  = Color(0xFF0A0C14)
private val Border      = Color(0xFF1A1E2E)
private val BorderDark  = Color(0xFF12151E)
private val Purple      = Color(0xFF7C6FCD)
private val PurpleLight = Color(0xFFA78BFA)
private val PurpleDark  = Color(0xFF534AB7)
private val PurpleBg    = Color(0xFF1A1E2E)
private val Green       = Color(0xFF4CAF50)
private val OrangeFire  = Color(0xFFFF6B35)
private val GoldColor   = Color(0xFFFFD700)
private val TextPrime   = Color(0xFFFFFFFF)
private val TextSec     = Color(0xFFBBBBBB)
private val TextMuted   = Color(0xFF444444)
private val TextDim     = Color(0xFF333333)

private val mascotMessages = listOf(
    Pair("DAILY TIP", "Your brain is **35% sharper** after a focus session. Start now!"),
    Pair("DID YOU KNOW", "Top performers focus for **90 minutes** then take a break."),
    Pair("MOTIVATION", "You're building a habit that **changes everything** - keep going!"),
    Pair("CHALLENGE", "Today's goal: complete a session with **zero distractions.**"),
    Pair("INSIGHT", "Every minute of focus is an investment in **tomorrow's you.**"),
    Pair("REMINDER", "Consistency beats intensity - **one session** makes the difference.")
)

private val weekLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

@Composable
fun HomeScreen(
    onStartSession: () -> Unit = {},
    onOpenProfile: () -> Unit = {},
    vm: HomeViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BgMain),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item { TopBar(state.userName, onOpenProfile) }
        item { NeonStreakCard(state) }
        item {
            MascotMessageCard(
                messageIndex = state.messageIndex,
                onRefresh = { vm.nextMessage() }
            )
        }
        item { StartFocusButton(onStartSession) }
        item { QuickStatsRow(state) }

        if (state.recentSessions.isNotEmpty()) {
            item { SessionsCard(state.recentSessions) }
        }
    }
}

@Composable
private fun TopBar(userName: String, onOpenProfile: () -> Unit) {
    val hour = java.time.LocalTime.now().hour
    val greeting = when {
        hour < 12 -> "Good morning"
        hour < 17 -> "Good afternoon"
        else -> "Good evening"
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("$greeting 👋", fontSize = 11.sp, color = TextMuted)
            Spacer(Modifier.height(2.dp))
            Text(userName, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrime)
        }
        IconButton(onClick = onOpenProfile) {
            Icon(Icons.Filled.Menu, "Menu", tint = Color(0xFF666666))
        }
    }
}

@Composable
private fun NeonStreakCard(state: HomeUiState) {
    val flamePulse by rememberInfiniteTransition(label = "flame")
        .animateFloat(
            0.7f,
            1f,
            infiniteRepeatable(tween(1800, easing = EaseInOut), RepeatMode.Reverse),
            label = "fp"
        )

    val glowPulse by rememberInfiniteTransition(label = "glow")
        .animateFloat(
            0.4f,
            0.7f,
            infiniteRepeatable(tween(2000, easing = EaseInOut), RepeatMode.Reverse),
            label = "gp"
        )

    val xpProgress = (state.totalXp.toFloat() / state.xpGoal).coerceIn(0f, 1f)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp)
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(22.dp),
        color = BgCard,
        border = BorderStroke(0.5.dp, Border)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(modifier = Modifier.size(44.dp), contentAlignment = Alignment.Center) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .scale(flamePulse)
                                .clip(CircleShape)
                                .border(1.5.dp, OrangeFire.copy(alpha = 0.5f), CircleShape)
                        )
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .scale(1f - (flamePulse - 0.7f))
                                .clip(CircleShape)
                                .border(1.dp, OrangeFire.copy(alpha = 0.25f), CircleShape)
                        )
                        Canvas(modifier = Modifier.size(26.dp)) {
                            val w = size.width
                            val h = size.height
                            val flamePath = Path().apply {
                                moveTo(w * 0.5f, 0f)
                                cubicTo(w * 0.2f, h * 0.3f, w * 0.1f, h * 0.5f, w * 0.2f, h * 0.65f)
                                cubicTo(w * 0.1f, h * 0.55f, w * 0.2f, h * 0.45f, w * 0.35f, h * 0.55f)
                                cubicTo(w * 0.3f, h * 0.75f, w * 0.4f, h * 0.85f, w * 0.5f, h)
                                cubicTo(w * 0.6f, h * 0.85f, w * 0.7f, h * 0.75f, w * 0.65f, h * 0.55f)
                                cubicTo(w * 0.8f, h * 0.45f, w * 0.9f, h * 0.55f, w * 0.8f, h * 0.65f)
                                cubicTo(w * 0.9f, h * 0.5f, w * 0.8f, h * 0.3f, w * 0.5f, 0f)
                                close()
                            }
                            drawPath(flamePath, OrangeFire)
                            val innerPath = Path().apply {
                                moveTo(w * 0.5f, h * 0.2f)
                                cubicTo(w * 0.35f, h * 0.45f, w * 0.35f, h * 0.6f, w * 0.42f, h * 0.72f)
                                cubicTo(w * 0.45f, h * 0.85f, w * 0.5f, h * 0.9f, w * 0.5f, h * 0.95f)
                                cubicTo(w * 0.5f, h * 0.9f, w * 0.55f, h * 0.85f, w * 0.58f, h * 0.72f)
                                cubicTo(w * 0.65f, h * 0.6f, w * 0.65f, h * 0.45f, w * 0.5f, h * 0.2f)
                                close()
                            }
                            drawPath(innerPath, GoldColor)
                        }
                    }

                    Column {
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                "${state.currentStreak}",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrime,
                                lineHeight = 28.sp
                            )
                            Text("days", fontSize = 12.sp, color = TextMuted, modifier = Modifier.padding(bottom = 4.dp))
                        }
                    }
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF1A1500),
                    border = BorderStroke(0.5.dp, OrangeFire.copy(0.4f))
                ) {
                    Text(
                        "On Fire!",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        fontSize = 10.sp,
                        color = OrangeFire,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("XP PROGRESS", fontSize = 10.sp, color = TextDim, letterSpacing = 0.4.sp)
                Text("${state.totalXp} / ${state.xpGoal}", fontSize = 10.sp, color = Purple, fontWeight = FontWeight.Medium)
            }
            Spacer(Modifier.height(6.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF12151E))
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(xpProgress)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Purple.copy(alpha = glowPulse))
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(xpProgress)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Purple)
                )
                if (xpProgress > 0.02f) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .align(Alignment.CenterStart)
                            .offset(x = (xpProgress * 260 - 6).dp.coerceAtLeast(0.dp))
                            .clip(CircleShape)
                            .background(PurpleLight)
                            .border(2.dp, BgMain, CircleShape)
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                val todayIndex = LocalDate.now().dayOfWeek.value - 1

                weekLabels.forEachIndexed { i, label ->
                    val isDone = state.weekDays.getOrElse(i) { false }
                    val isToday = i == todayIndex
                    val todayPulse by rememberInfiniteTransition(label = "today$i")
                        .animateFloat(
                            0f,
                            if (isToday) 1f else 0f,
                            infiniteRepeatable(tween(2000), RepeatMode.Reverse),
                            label = "tp$i"
                        )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .then(
                                    when {
                                        isToday -> Modifier.border(
                                            (1.5 + todayPulse * 0.5).dp,
                                            Green.copy(alpha = 0.5f + todayPulse * 0.3f),
                                            CircleShape
                                        )
                                        isDone -> Modifier.border(1.5.dp, Purple, CircleShape)
                                        else -> Modifier.border(1.dp, Border, CircleShape)
                                    }
                                )
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isToday -> Green.copy(0.1f)
                                        isDone -> Purple.copy(0.13f)
                                        else -> BgCardDark
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isToday -> Green
                                            isDone -> Purple
                                            else -> Color.Transparent
                                        }
                                    )
                            )
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(
                            label,
                            fontSize = 8.sp,
                            color = when {
                                isToday -> Green
                                isDone -> PurpleDark
                                else -> TextDim
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MascotMessageCard(messageIndex: Int, onRefresh: () -> Unit) {
    val (tag, message) = mascotMessages[messageIndex % mascotMessages.size]
    val bounce by rememberInfiniteTransition(label = "bounce")
        .animateFloat(
            0f,
            -6f,
            infiniteRepeatable(tween(2200, easing = EaseInOut), RepeatMode.Reverse),
            label = "bf"
        )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp)
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(22.dp),
        color = BgCard,
        border = BorderStroke(0.5.dp, Border)
    ) {
        Row(
            modifier = Modifier.padding(14.dp, 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MiniMascot(
                modifier = Modifier
                    .size(60.dp, 70.dp)
                    .offset(y = bounce.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = PurpleBg,
                    border = BorderStroke(0.5.dp, Border)
                ) {
                    Text(
                        tag,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        fontSize = 9.sp,
                        color = Purple,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.4.sp
                    )
                }

                Spacer(Modifier.height(6.dp))

                val parts = message.split("**")
                Text(
                    buildAnnotatedString {
                        parts.forEachIndexed { i, part ->
                            if (i % 2 == 1) {
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = TextPrime)) { append(part) }
                            } else {
                                withStyle(SpanStyle(color = TextSec)) { append(part) }
                            }
                        }
                    },
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )

                Spacer(Modifier.height(6.dp))

                Row(
                    modifier = Modifier.clickable { onRefresh() },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = "Refresh",
                        tint = TextDim,
                        modifier = Modifier.size(10.dp)
                    )
                    Text("tap to refresh", fontSize = 10.sp, color = TextDim)
                }
            }
        }
    }
}

@Composable
private fun MiniMascot(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val s = size.minDimension / 80f
        fun f(v: Number) = v.toFloat() * s

        val body = Color(0xFF6C5FC7)
        val bodyDark = Color(0xFF4F45A5)
        val head = Color(0xFF8E7CEC)
        val headDark = Color(0xFF6B5AC8)
        val face = Color(0xFF12111F)

        drawOval(
            color = Color.Black.copy(alpha = 0.16f),
            topLeft = Offset(f(24), f(69)),
            size = Size(f(32), f(6))
        )

        drawRoundRect(body, Offset(f(29), f(49)), Size(f(22), f(21)), CornerRadius(f(10)))
        drawRoundRect(bodyDark, Offset(f(31), f(66)), Size(f(7), f(9)), CornerRadius(f(4)))
        drawRoundRect(bodyDark, Offset(f(42), f(66)), Size(f(7), f(9)), CornerRadius(f(4)))
        drawRoundRect(body, Offset(f(24), f(69)), Size(f(16), f(6)), CornerRadius(f(4)))
        drawRoundRect(body, Offset(f(40), f(69)), Size(f(16), f(6)), CornerRadius(f(4)))

        drawLine(body, Offset(f(30), f(54)), Offset(f(19), f(47)), f(6), cap = StrokeCap.Round)
        drawLine(body, Offset(f(50), f(54)), Offset(f(61), f(47)), f(6), cap = StrokeCap.Round)
        drawCircle(head, f(3.7f), Offset(f(18), f(46)))
        drawCircle(head, f(3.7f), Offset(f(62), f(46)))

        drawCircle(headDark, f(6), Offset(f(14), f(32)))
        drawCircle(headDark, f(6), Offset(f(66), f(32)))
        drawLine(headDark, Offset(f(17), f(30)), Offset(f(63), f(30)), f(3), cap = StrokeCap.Round)

        drawCircle(head, f(25), Offset(f(40), f(30)))
        drawArc(
            color = headDark.copy(alpha = 0.45f),
            startAngle = 195f,
            sweepAngle = 150f,
            useCenter = false,
            topLeft = Offset(f(17), f(7)),
            size = Size(f(46), f(46)),
            style = Stroke(width = f(2.4f), cap = StrokeCap.Round)
        )
        drawLine(
            color = Color.White.copy(alpha = 0.18f),
            start = Offset(f(40), f(8)),
            end = Offset(f(40), f(21)),
            strokeWidth = f(1.3f),
            cap = StrokeCap.Round
        )

        drawRoundRect(face, Offset(f(20), f(27)), Size(f(40), f(22)), CornerRadius(f(11)))
        drawCircle(Color.White.copy(alpha = 0.96f), f(5.6f), Offset(f(31), f(35)))
        drawCircle(Color.White.copy(alpha = 0.96f), f(5.6f), Offset(f(49), f(35)))
        drawCircle(Color(0xFF111021), f(2.3f), Offset(f(31), f(36)))
        drawCircle(Color(0xFF111021), f(2.3f), Offset(f(49), f(36)))

        drawCircle(Color(0xFFB9A8FF).copy(alpha = 0.55f), f(2), Offset(f(24), f(41)))
        drawCircle(Color(0xFFB9A8FF).copy(alpha = 0.55f), f(2), Offset(f(56), f(41)))

        val smilePath = Path().apply {
            moveTo(f(33), f(44))
            quadraticBezierTo(f(40), f(48), f(47), f(44))
        }
        drawPath(smilePath, headDark, style = Stroke(width = f(2.1f), cap = StrokeCap.Round))
    }
}

@Composable
private fun StartFocusButton(onClick: () -> Unit) {
    rememberInfiniteTransition(label = "shine")
        .animateFloat(
            -1f,
            2f,
            infiniteRepeatable(tween(3000, delayMillis = 1000), RepeatMode.Restart),
            label = "sf"
        )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp)
            .padding(bottom = 12.dp)
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Green)
        ) {
            Text("▶  Start Focus Session", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}

@Composable
private fun QuickStatsRow(state: HomeUiState) {
    fun formatMinutes(m: Int): String {
        return if (m >= 60) "${m / 60}h ${m % 60}m" else "$m min"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp)
            .padding(bottom = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp),
            color = BgCard,
            border = BorderStroke(0.5.dp, Border)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("⏱", fontSize = 18.sp, color = PurpleLight)
                Spacer(Modifier.height(6.dp))
                Text(formatMinutes(state.todayMinutes), fontSize = 19.sp, fontWeight = FontWeight.Bold, color = PurpleLight)
                Text("Focus today", fontSize = 10.sp, color = TextDim)
            }
        }
        Surface(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp),
            color = BgCard,
            border = BorderStroke(0.5.dp, Border)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("⚡", fontSize = 18.sp, color = Green)
                Spacer(Modifier.height(6.dp))
                Text("+${state.todayXp} XP", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Green)
                Text("Earned today", fontSize = 10.sp, color = TextDim)
            }
        }
    }
}

@Composable
private fun SessionsCard(sessions: List<com.thrive.domain.FocusSession>) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp)
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(16.dp),
        color = BgCard,
        border = BorderStroke(0.5.dp, Border)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("TODAY'S SESSIONS", fontSize = 10.sp, color = TextDim, letterSpacing = 0.4.sp)
                Text("See all", fontSize = 10.sp, color = PurpleDark)
            }
            Spacer(Modifier.height(10.dp))

            sessions.take(3).forEachIndexed { i, session ->
                val dotColor = when {
                    session.distractionCount == 0 -> Green
                    session.distractionCount <= 2 -> Color(0xFFFBBF24)
                    else -> Color(0xFFF87171)
                }

                if (i > 0) {
                    Divider(color = BorderDark, thickness = 0.5.dp)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(dotColor)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Focus Session", fontSize = 11.sp, color = TextSec, fontWeight = FontWeight.Medium)
                        Text(
                            "${session.actualSeconds / 60} min · ${session.distractionCount} distractions",
                            fontSize = 10.sp,
                            color = TextDim,
                            modifier = Modifier.padding(top = 1.dp)
                        )
                    }
                    Text("+${session.xpEarned} XP", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = dotColor)
                }
            }
        }
    }
}
