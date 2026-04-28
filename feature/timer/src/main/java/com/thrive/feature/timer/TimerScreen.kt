package com.thrive.feature.timer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

@Composable
fun TimerScreen(vm: TimerViewModel = hiltViewModel()) {
    val state by vm.state.collectAsState()
    val context = LocalContext.current

    Box(Modifier.fillMaxSize()) {
        when {
            state.needsPermission -> PermissionScreen(
                onOpenSettings = { vm.requestPermissionFromSettings(context) },
                onSkip = { vm.startWithoutPermission() }
            )

            state.result != null -> ResultScreen(
                result = state.result!!,
                onDone = { vm.dismissResult() }
            )

            else -> MainTimer(state = state, vm = vm)
        }

        AnimatedVisibility(
            visible = state.warningApp != null,
            enter = slideInVertically { -it },
            exit = slideOutVertically { -it },
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            state.warningApp?.let { app ->
                WarningBanner(appName = app, onDismiss = { vm.dismissWarning() })
            }
        }
    }
}

@Composable
internal fun MainTimer(
    state: TimerState,
    vm: TimerViewModel
) {
    val durations = listOf(30, 60, 75, 90, 120)
    val isRunning = state.phase == Phase.RUNNING
    val isPaused = state.phase == Phase.PAUSED
    val isActive = isRunning || isPaused

    val particles = remember { mutableStateListOf<NeonParticle>() }
    val scope = rememberCoroutineScope()

    val arcProgress = if (state.totalSeconds > 0) {
        state.remainingSeconds.toFloat() / state.totalSeconds
    } else {
        1f
    }

    val arcColor by animateColorAsState(
        targetValue = if (isPaused) Color(0xFFFF6B35) else Color(0xFF64DC64),
        animationSpec = tween(500),
        label = "arcColor"
    )

    var tickPulse by remember { mutableStateOf(false) }
    LaunchedEffect(state.remainingSeconds) { tickPulse = !tickPulse }
    val dialScale by animateFloatAsState(
        targetValue = if (tickPulse) 1.025f else 1f,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium),
        label = "dialScale"
    )

    val glowAlpha by animateFloatAsState(
        targetValue = if (isRunning) 0.08f else if (isPaused) 0.06f else 0.04f,
        animationSpec = tween(600),
        label = "glow"
    )

    var shakeDistCard by remember { mutableStateOf(false) }
    val shakeOffset by animateFloatAsState(
        targetValue = if (shakeDistCard) 1f else 0f,
        animationSpec = spring(Spring.DampingRatioHighBouncy, Spring.StiffnessHigh),
        label = "shake",
        finishedListener = { shakeDistCard = false }
    )

    var showDistBanner by remember { mutableStateOf(false) }
    var lastDistCount by remember { mutableIntStateOf(state.distractionCount) }

    LaunchedEffect(state.distractionCount) {
        if (state.distractionCount > lastDistCount && isRunning) {
            lastDistCount = state.distractionCount
            shakeDistCard = true
            showDistBanner = true
            kotlinx.coroutines.delay(4000)
            showDistBanner = false
        } else if (state.distractionCount < lastDistCount) {
            lastDistCount = state.distractionCount
        }
    }

    LaunchedEffect(isRunning) {
        while (isRunning) {
            kotlinx.coroutines.delay(40)
            if (arcProgress > 0f && particles.size < 80) {
                val angle = (-Math.PI / 2 + 2 * Math.PI * arcProgress).toFloat()
                val tx = 0.5f + 0.44f * cos(angle)
                val ty = 0.5f + 0.44f * sin(angle)
                if (Math.random() > 0.4) {
                    particles.add(
                        NeonParticle(
                            x = tx,
                            y = ty,
                            vx = ((Math.random() - 0.5) * 0.008).toFloat(),
                            vy = ((Math.random() - 0.5) * 0.008 - 0.004).toFloat(),
                            radius = 1.5f + Math.random().toFloat() * 2f,
                            alpha = 0.8f,
                            color = Color(0xFF64DC64).copy(alpha = 0.6f),
                            decay = 0.035f + Math.random().toFloat() * 0.02f
                        )
                    )
                }
            }

            val iter = particles.iterator()
            while (iter.hasNext()) {
                val p = iter.next()
                p.x += p.vx
                p.y += p.vy
                p.vy += 0.0002f
                p.alpha -= p.decay
                if (p.alpha <= 0f) iter.remove()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF080B12))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val glowColor = if (isPaused) Color(0xFFFF6B35) else Color(0xFF64DC64)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(glowColor.copy(alpha = glowAlpha), Color.Transparent),
                    radius = size.minDimension * 0.6f
                ),
                radius = size.minDimension * 0.6f,
                center = Offset(size.width / 2f, size.height * 0.38f)
            )
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            particles.forEach { p ->
                drawCircle(
                    color = p.color.copy(alpha = p.alpha),
                    radius = p.radius,
                    center = Offset(p.x * size.width, p.y * size.height)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Deep Focus Timer", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF1A1F2E),
                    border = BorderStroke(0.5.dp, Color(0xFF2A3040))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("🔥", fontSize = 14.sp)
                        Text("Focus streak", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Medium)
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            val statusText = when {
                isPaused -> "PAUSED"
                isRunning -> "FOCUSING"
                else -> "READY TO FOCUS"
            }
            val statusColor = when {
                isPaused -> Color(0xFFFF6B35)
                isRunning -> Color(0xFF64DC64)
                else -> Color(0xFF444444)
            }

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = when {
                    isPaused -> Color(0xFF120800)
                    isRunning -> Color(0xFF0A1408)
                    else -> Color(0xFF111620)
                },
                border = BorderStroke(
                    0.5.dp,
                    when {
                        isPaused -> Color(0xFFFF6B35).copy(0.35f)
                        isRunning -> Color(0xFF64DC64).copy(0.4f)
                        else -> Color(0xFF2A3040)
                    }
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val dotAlpha by rememberInfiniteTransition(label = "dot").animateFloat(
                        initialValue = 1f,
                        targetValue = 0.2f,
                        animationSpec = infiniteRepeatable(tween(1200), RepeatMode.Reverse),
                        label = "dotAlpha"
                    )
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(statusColor.copy(alpha = if (isRunning) dotAlpha else 1f))
                    )
                    Text(
                        statusText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = statusColor,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            Box(modifier = Modifier.size(210.dp), contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val cx = size.width / 2f
                    val cy = size.height / 2f
                    val radius = size.minDimension * 0.43f
                    val strokeWidth = 11.dp.toPx()
                    val startAngle = -90f
                    val sweep = 360f * arcProgress

                    drawCircle(
                        color = arcColor.copy(alpha = 0.06f),
                        radius = radius + strokeWidth,
                        style = Stroke(width = strokeWidth * 2.2f)
                    )
                    drawCircle(
                        color = Color(0xFF151B26),
                        radius = radius,
                        style = Stroke(width = strokeWidth)
                    )

                    if (arcProgress > 0f) {
                        drawArc(
                            color = arcColor.copy(alpha = 0.35f),
                            startAngle = startAngle,
                            sweepAngle = sweep,
                            useCenter = false,
                            style = Stroke(width = strokeWidth * 1.8f, cap = StrokeCap.Round)
                        )
                        drawArc(
                            color = arcColor,
                            startAngle = startAngle,
                            sweepAngle = sweep,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )

                        val tipAngle = Math.toRadians((startAngle + sweep).toDouble())
                        val tipX = (cx + radius * cos(tipAngle)).toFloat()
                        val tipY = (cy + radius * sin(tipAngle)).toFloat()
                        drawCircle(color = arcColor.copy(alpha = 0.4f), radius = 12.dp.toPx(), center = Offset(tipX, tipY))
                        drawCircle(color = Color.White, radius = 7.dp.toPx(), center = Offset(tipX, tipY))
                        drawCircle(color = arcColor, radius = 4.dp.toPx(), center = Offset(tipX, tipY))
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.scale(dialScale)) {
                    Text(
                        text = formatTime(state.remainingSeconds),
                        fontSize = 46.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 0.sp,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                    Text(
                        text = if (isActive) "/ ${formatTime(state.totalSeconds)}" else "minutes remaining",
                        fontSize = 11.sp,
                        color = Color(0xFF444444),
                        letterSpacing = 0.3.sp
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            if (state.phase == Phase.IDLE) {
                Row(horizontalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.wrapContentWidth()) {
                    durations.forEach { min ->
                        val isSelected = state.selectedMinutes == min
                        Surface(
                            onClick = { vm.selectDuration(min) },
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) Color(0xFF0F1A10) else Color(0xFF111620),
                            border = BorderStroke(
                                0.5.dp,
                                if (isSelected) Color(0xFF64DC64).copy(0.5f) else Color(0xFF2A3040)
                            ),
                            modifier = Modifier.scale(if (isSelected) 1.05f else 1f)
                        ) {
                            Text(
                                "$min min",
                                modifier = Modifier.padding(horizontal = 11.dp, vertical = 7.dp),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (isSelected) Color(0xFF64DC64) else Color(0xFF555555)
                            )
                        }
                    }
                }
                Spacer(Modifier.height(14.dp))
            }

            if (isActive) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .offset(x = if (shakeDistCard) (shakeOffset * 4).dp else 0.dp),
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFF111620),
                        border = BorderStroke(
                            0.5.dp,
                            if (shakeDistCard) Color(0xFFFF6B35).copy(0.6f) else Color(0xFF1E2530)
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("DISTRACTIONS", fontSize = 10.sp, color = Color(0xFF444444), letterSpacing = 0.5.sp)
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "${state.distractionCount}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (state.distractionCount > 0) Color(0xFFFF6B35) else Color.White
                            )
                        }
                    }
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFF111620),
                        border = BorderStroke(0.5.dp, Color(0xFF1E2530))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("XP EARNED", fontSize = 10.sp, color = Color(0xFF444444), letterSpacing = 0.5.sp)
                            Spacer(Modifier.height(4.dp))
                            val estimatedXp = ((state.totalSeconds - state.remainingSeconds) / 60 * 10 *
                                    if (state.distractionCount == 0) 1.2f else 1.0f).toInt()
                            Text(
                                "+$estimatedXp XP",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF64DC64)
                            )
                        }
                    }
                }
                Spacer(Modifier.height(14.dp))
            }

            when (state.phase) {
                Phase.IDLE -> {
                    Button(
                        onClick = {
                            vm.startSession()
                            scope.launch {
                                repeat(60) {
                                    val angle = (Math.random() * Math.PI * 2).toFloat()
                                    val speed = 0.008f + Math.random().toFloat() * 0.02f
                                    particles.add(
                                        NeonParticle(
                                            x = 0.5f,
                                            y = 0.38f,
                                            vx = cos(angle) * speed,
                                            vy = sin(angle) * speed - 0.01f,
                                            radius = 2f + Math.random().toFloat() * 4f,
                                            alpha = 1f,
                                            color = listOf(Color(0xFF64DC64), Color(0xFF4AFF6A), Color.White, Color(0xFFA0FF80)).random(),
                                            decay = 0.012f + Math.random().toFloat() * 0.015f
                                        )
                                    )
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64DC64))
                    ) {
                        Text("▶  Start Focus Session", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF020A04))
                    }
                }
                Phase.RUNNING -> {
                    Button(
                        onClick = { vm.stop() },
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC0392B))
                    ) {
                        Text("■  End Session", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { vm.pause() },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(0.5.dp, Color(0xFF2A3040))
                    ) {
                        Text("⏸  Pause", fontSize = 14.sp, color = Color(0xFF666666))
                    }
                }
                Phase.PAUSED -> {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { vm.resume() },
                            modifier = Modifier.weight(1f).height(52.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64DC64))
                        ) {
                            Text("▶  Resume", fontWeight = FontWeight.Bold, color = Color(0xFF020A04))
                        }
                        OutlinedButton(
                            onClick = { vm.stop() },
                            modifier = Modifier.weight(1f).height(52.dp),
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, Color(0xFFC0392B))
                        ) {
                            Text("⏹  End", fontWeight = FontWeight.Bold, color = Color(0xFFC0392B))
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = showDistBanner,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 14.dp, start = 14.dp, end = 14.dp)
                .zIndex(10f)
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF1A0800),
                border = BorderStroke(0.5.dp, Color(0xFFFF6B35).copy(0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Stay focused!", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFFFF6B35))
                        Text("You switched to a distracting app", fontSize = 11.sp, color = Color(0xFF666666))
                    }
                    TextButton(onClick = { showDistBanner = false }) {
                        Text("✕", color = Color(0xFF444444))
                    }
                }
            }
        }
    }
}

data class NeonParticle(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    var radius: Float,
    var alpha: Float,
    val color: Color,
    val decay: Float
)

private fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return "${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}"
}

@Composable
private fun LegacyMainTimer(state: TimerState, vm: TimerViewModel) {
    val durations = listOf(30, 60, 75, 90, 120)
    val isRunning = state.phase == Phase.RUNNING
    val isPaused = state.phase == Phase.PAUSED

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0F14))
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))

        // Header
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
            Text(
                "Deep Focus Timer",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            if (isRunning) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF4CAF50).copy(0.15f),
                    border = BorderStroke(0.5.dp, Color(0xFF4CAF50).copy(0.4f))
                ) {
                    Text(
                        "SESSION IN PROGRESS",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 9.sp,
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // Duration chips (only when idle)
        if (state.phase == Phase.IDLE) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                durations.forEach { min ->
                    FilterChip(
                        selected = state.selectedMinutes == min,
                        onClick = { vm.selectDuration(min) },
                        label = { Text("$min min", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF4CAF50).copy(0.2f),
                            selectedLabelColor = Color(0xFF4CAF50)
                        )
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        val progress = if (state.totalSeconds > 0) {
            state.remainingSeconds.toFloat() / state.totalSeconds
        } else 1f

        // Circular dial
        TimerDial(
            progress = progress,
            remainingSeconds = state.remainingSeconds,
            isRunning = isRunning,
            modifier = Modifier.size(220.dp)
        )

        Spacer(Modifier.height(16.dp))

        // Live session stats (only when running or paused)
        if (isRunning || isPaused) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF161B22),
                border = BorderStroke(0.5.dp, Color(0xFF1E2530))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                        Text(
                            "Level 1 – Focus Rookie",
                            fontSize = 12.sp,
                            color = Color(0xFFCCCCCC),
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            "${state.selectedMinutes - state.remainingSeconds / 60} / ${state.selectedMinutes} min",
                            fontSize = 12.sp,
                            color = Color(0xFF4CAF50)
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    val sessionProgress = 1f - (state.remainingSeconds.toFloat() / state.totalSeconds)
                    LinearProgressIndicator(
                        progress = { sessionProgress.coerceIn(0f, 1f) },
                        modifier = Modifier.fillMaxWidth().height(5.dp).clip(RoundedCornerShape(3.dp)),
                        color = Color(0xFF4CAF50),
                        trackColor = Color(0xFF1A2020)
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF161B22),
                    border = BorderStroke(0.5.dp, Color(0xFF1E2530))
                ) {
                    Column(Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("⚡ Distractions", fontSize = 10.sp, color = Color(0xFF666666))
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "${state.distractionCount}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (state.distractionCount == 0) Color.White else Color(0xFFFF6B35)
                        )
                    }
                }
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF161B22),
                    border = BorderStroke(0.5.dp, Color(0xFF1E2530))
                ) {
                    Column(Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("⭐ XP Earned", fontSize = 10.sp, color = Color(0xFF666666))
                        Spacer(Modifier.height(4.dp))
                        val estimatedXp = ((state.selectedMinutes - state.remainingSeconds / 60) * 10 *
                                if (state.distractionCount == 0) 1.2f else 1.0f).toInt()
                        Text(
                            "+$estimatedXp XP",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50)
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
        }

        // Action buttons
        when (state.phase) {
            Phase.IDLE -> {
                Button(
                    onClick = { vm.startSession() },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Start Focus Session 🚀", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }

            Phase.RUNNING -> {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = { vm.pause() },
                        modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))
                    ) {
                        Text("Pause", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }

                    OutlinedButton(
                        onClick = { vm.stop() },
                        modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, Color(0xFFC0392B))
                    ) {
                        Text("End", color = Color(0xFFC0392B), fontWeight = FontWeight.Bold)
                    }
                }
            }

            Phase.PAUSED -> {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = { vm.resume() },
                        modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) { Text("Resume", color = Color.Black, fontWeight = FontWeight.Bold) }

                    OutlinedButton(
                        onClick = { vm.stop() },
                        modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, Color(0xFFC0392B))
                    ) { Text("End", color = Color(0xFFC0392B), fontWeight = FontWeight.Bold) }
                }
            }
        }
    }
}

@Composable
private fun TimerDial(
    progress: Float,
    remainingSeconds: Int,
    isRunning: Boolean,
    modifier: Modifier
) {
    val primary = Color(0xFF7C6FCD)
    val track = Color(0xFF24243A)
    val animated by animateFloatAsState(progress, tween(500), label = "arc")

    val pulse by rememberInfiniteTransition(label = "p").animateFloat(
        0.7f,
        if (isRunning) 1f else 0.7f,
        infiniteRepeatable(tween(900), RepeatMode.Reverse),
        label = "pulse"
    )

    Box(modifier, contentAlignment = Alignment.Center) {
        // Glow
        Canvas(Modifier.matchParentSize()) {
            val radius = size.minDimension / 2.15f
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        primary.copy(alpha = if (isRunning) 0.22f else 0.14f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = radius * 1.65f
                ),
                radius = radius * 1.65f,
                center = center
            )
        }

        Canvas(Modifier.fillMaxSize()) {
            val sw = 18.dp.toPx()
            val d = min(size.width, size.height) - sw
            val tl = Offset((size.width - d) / 2f, (size.height - d) / 2f)
            val arc = Size(d, d)
            drawArc(track, -90f, 360f, false, tl, arc, style = Stroke(sw, cap = StrokeCap.Round))
            if (animated > 0f) {
                drawArc(
                    primary.copy(alpha = if (isRunning) pulse else 1f),
                    -90f,
                    360f * animated,
                    false,
                    tl,
                    arc,
                    style = Stroke(sw, cap = StrokeCap.Round)
                )
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val m = remainingSeconds / 60
            val s = (remainingSeconds % 60).toString().padStart(2, '0')
            Text(
                "$m:$s",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                "minutes remaining",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(0.45f)
            )
        }
    }
}

@Composable
private fun WarningBanner(appName: String, onDismiss: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(12.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFF6B35))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp, 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    "⚠️ Stay focused!",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 14.sp
                )
                Text("You switched to $appName", color = Color.White.copy(0.9f), fontSize = 12.sp)
            }
            TextButton(onClick = onDismiss) { Text("✕", color = Color.White) }
        }
    }
}

@Composable
private fun PermissionScreen(onOpenSettings: () -> Unit, onSkip: () -> Unit) {
    Column(
        Modifier.fillMaxSize().padding(32.dp),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        Text("📱", fontSize = 64.sp)
        Spacer(Modifier.height(20.dp))
        Text(
            "Enable Distraction Detection",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(12.dp))
        Text(
            "Thrive needs Usage Access to detect when you switch to Instagram, TikTok, YouTube, etc. during your session.\n\nAll data stays on your device.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(0.7f),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(32.dp))
        Button(onClick = onOpenSettings, modifier = Modifier.fillMaxWidth().height(52.dp)) {
            Text("Open Settings")
        }
        Spacer(Modifier.height(12.dp))
        TextButton(onClick = onSkip) {
            Text("Skip — start without detection", color = MaterialTheme.colorScheme.onSurface.copy(0.5f))
        }
    }
}

@Composable
private fun ResultScreen(result: SessionResult, onDone: () -> Unit) {
    var phase by remember { mutableStateOf(0) }
    // 0 = session summary, 1 = challenge complete, 2 = streak increased

    LaunchedEffect(Unit) {
        phase = if (result.challengeCompleted) 1 else 0
    }

    when (phase) {
        0 -> SessionSummary(result, onDone)
        1 -> ChallengeCompleteOverlay(
            title = result.completedChallengeTitle,
            xpReward = result.completedChallengeXp,
            onContinue = {
                if (result.streakIncreased) phase = 2 else onDone()
            }
        )
        2 -> StreakOverlay(
            oldStreak = result.oldStreak,
            newStreak = result.newStreak,
            onContinue = onDone
        )
    }
}

@Composable
private fun SessionSummary(result: SessionResult, onDone: () -> Unit) {
    val animXp by animateIntAsState(result.earnedXp, tween(800), label = "xp")
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF080B12)).padding(28.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🎉", fontSize = 64.sp)
        Spacer(Modifier.height(16.dp))
        Text(
            "Session Complete!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(Modifier.height(24.dp))
        Surface(
            Modifier.fillMaxWidth(),
            RoundedCornerShape(16.dp),
            color = Color(0xFF161B22),
            border = BorderStroke(1.dp, Color(0xFF64DC64).copy(0.3f))
        ) {
            Column(
                Modifier.fillMaxWidth().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "+$animXp XP",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64DC64)
                )
            }
        }
        Spacer(Modifier.height(14.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatCard(
                "Focus Rate",
                "${(result.focusRate * 100).toInt()}%",
                if (result.focusRate >= 0.8f) Color(0xFF64DC64) else Color(0xFFFF6B35),
                Modifier.weight(1f)
            )
            StatCard(
                "Distractions",
                "${result.distractionCount}",
                if (result.distractionCount == 0) Color(0xFF64DC64) else Color(0xFFFF6B35),
                Modifier.weight(1f)
            )
        }
        Spacer(Modifier.height(32.dp))
        Button(
            onClick = onDone,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64DC64))
        ) {
            Text("Done ✓", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}

@Composable
private fun ChallengeCompleteOverlay(title: String, xpReward: Int, onContinue: () -> Unit) {
    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { entered = true }

    val scale by animateFloatAsState(
        if (entered) 1f else 0.78f,
        spring(
            dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy,
            stiffness = androidx.compose.animation.core.Spring.StiffnessMedium
        ),
        label = "challenge-card-scale"
    )
    val cardAlpha by animateFloatAsState(
        if (entered) 1f else 0f,
        tween(450),
        label = "challenge-card-alpha"
    )
    val burstProgress by animateFloatAsState(
        if (entered) 1f else 0f,
        tween(1400, easing = FastOutSlowInEasing),
        label = "challenge-burst"
    )
    val animXp by animateIntAsState(xpReward, tween(1000), label = "xp")

    Box(Modifier.fillMaxSize().background(Color.Black.copy(0.9f)), Alignment.Center) {
        ChallengeConfettiBurst(burstProgress)
        Card(
            modifier = Modifier.fillMaxWidth(0.88f).scale(scale).alpha(cardAlpha),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0D0F14)),
            border = BorderStroke(1.dp, Color(0xFFFFD700).copy(0.35f))
        ) {
            Column(Modifier.padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.padding(bottom = 8.dp)) {
                    listOf(Color(0xFF4CAF50), Color(0xFFFFD700), Color(0xFFFF6B35), Color(0xFFA78BFA), Color(0xFFF472B6)).forEach { c ->
                        Box(Modifier.size(6.dp).clip(CircleShape).background(c))
                    }
                }
                ChallengeAchievementBadge()
                Spacer(Modifier.height(16.dp))
                Text("Challenge Completed!", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(title, fontSize = 13.sp, color = Color(0xFF666666), modifier = Modifier.padding(top = 4.dp, bottom = 16.dp))
                Surface(
                    Modifier.fillMaxWidth(),
                    RoundedCornerShape(12.dp),
                    color = Color(0xFF0A0A0A),
                    border = BorderStroke(0.5.dp, Color(0xFF1E2530))
                ) {
                    Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("+$animXp XP", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    "Great job! You've completed your focus challenge.",
                    fontSize = 12.sp,
                    color = Color(0xFF444444),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(20.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.padding(bottom = 16.dp)) {
                    listOf(Color(0xFF60A5FA), Color(0xFF4CAF50), Color(0xFFFFD700), Color(0xFFF472B6), Color(0xFFFF6B35)).forEach { c ->
                        Box(Modifier.size(6.dp).clip(CircleShape).background(c))
                    }
                }
                Button(
                    onClick = onContinue,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Awesome! 🎉", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }
        }
    }
}

@Composable
private fun ChallengeAchievementBadge() {
    val pulse by rememberInfiniteTransition(label = "badge-pulse").animateFloat(
        0.86f,
        1.08f,
        infiniteRepeatable(tween(900, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "badge-pulse-value"
    )

    Box(Modifier.size(116.dp), contentAlignment = Alignment.Center) {
        Canvas(Modifier.matchParentSize()) {
            val glow = Color(0xFFFFD54F)
            drawCircle(glow.copy(alpha = 0.16f * pulse), radius = size.minDimension * 0.48f)
            drawCircle(glow.copy(alpha = 0.22f), radius = size.minDimension * 0.36f)
            drawCircle(glow.copy(alpha = 0.55f), radius = size.minDimension * 0.27f, style = Stroke(4.dp.toPx()))
        }
        Box(
            Modifier.size(82.dp).scale(pulse)
                .background(Color(0xFF1A1A00), RoundedCornerShape(20.dp))
                .border(2.dp, Color(0xFFFFD700), RoundedCornerShape(20.dp)),
            Alignment.Center
        ) {
            Text("🎯", fontSize = 38.sp)
        }
    }
}

@Composable
private fun ChallengeConfettiBurst(progress: Float) {
    val colors = listOf(
        Color(0xFF4CAF50),
        Color(0xFFFFD700),
        Color(0xFFFF6B35),
        Color(0xFFA78BFA),
        Color(0xFFF472B6),
        Color(0xFF60A5FA)
    )

    Canvas(Modifier.fillMaxSize()) {
        val center = Offset(size.width / 2f, size.height * 0.33f)
        val eased = progress.coerceIn(0f, 1f)
        val fade = (1f - ((eased - 0.55f) / 0.45f).coerceIn(0f, 1f)).coerceIn(0f, 1f)

        repeat(32) { index ->
            val angle = (index / 32f) * 2f * PI.toFloat()
            val distance = (70f + (index % 7) * 18f) * eased
            val wave = sin((eased * PI.toFloat()) + index) * 16f
            val particleCenter = Offset(
                x = center.x + cos(angle) * distance + cos(angle + 1.4f) * wave,
                y = center.y + sin(angle) * distance + sin(angle + 1.4f) * wave + 80f * eased
            )
            val color = colors[index % colors.size].copy(alpha = fade)

            if (index % 3 == 0) {
                drawLine(
                    color = color,
                    start = particleCenter,
                    end = Offset(
                        particleCenter.x + cos(angle) * 18f,
                        particleCenter.y + sin(angle) * 18f
                    ),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
            } else {
                drawCircle(color = color, radius = (3 + index % 4).dp.toPx(), center = particleCenter)
            }
        }
    }
}

@Composable
private fun StreakOverlay(oldStreak: Int, newStreak: Int, onContinue: () -> Unit) {
    val pulse by rememberInfiniteTransition(label = "p")
        .animateFloat(1f, 1.1f, infiniteRepeatable(tween(800), RepeatMode.Reverse), label = "pf")
    val animNew by animateIntAsState(newStreak, tween(800), label = "sn")

    Box(Modifier.fillMaxSize().background(Color.Black.copy(0.92f)), Alignment.Center) {
        Card(
            modifier = Modifier.fillMaxWidth(0.88f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0D0F14)),
            border = BorderStroke(1.dp, Color(0xFFFF6B35).copy(0.4f))
        ) {
            Column(Modifier.padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🔥", fontSize = 72.sp, modifier = Modifier.scale(pulse))
                Spacer(Modifier.height(12.dp))
                Text("Streak Increased!", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("You're on fire! 🔥", fontSize = 13.sp, color = Color(0xFFFF6B35), modifier = Modifier.padding(top = 4.dp, bottom = 16.dp))
                Surface(
                    Modifier.fillMaxWidth(),
                    RoundedCornerShape(14.dp),
                    color = Color(0xFF0A0500),
                    border = BorderStroke(0.5.dp, Color(0xFFFF6B35).copy(0.3f))
                ) {
                    Row(Modifier.fillMaxWidth().padding(20.dp), Arrangement.Center, Alignment.CenterVertically) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("$oldStreak", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color(0xFF444444))
                            Text("Day Streak", fontSize = 10.sp, color = Color(0xFF444444))
                        }
                        Spacer(Modifier.width(20.dp))
                        Text("→", fontSize = 26.sp, color = Color(0xFFFF6B35), fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(20.dp))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("$animNew", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF6B35))
                            Text("Day Streak", fontSize = 10.sp, color = Color(0xFFFF6B35))
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text("Keep it up and don't break the chain!", fontSize = 12.sp, color = Color(0xFF444444), textAlign = TextAlign.Center)
                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = onContinue,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Let's Go! 💪", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }
        }
    }
}

@Composable
private fun StatCard(label: String, value: String, color: Color, modifier: Modifier) {
    Surface(
        modifier,
        RoundedCornerShape(12.dp),
        color = Color(0xFF161B22),
        border = BorderStroke(0.5.dp, Color(0xFF1E2530))
    ) {
        Column(Modifier.padding(14.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = color)
            Text(label, style = MaterialTheme.typography.bodySmall, color = Color(0xFF666666))
        }
    }
}

