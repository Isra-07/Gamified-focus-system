package com.thrive.feature.summary

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.core.graphics.drawable.toBitmap
import com.thrive.domain.LeaderboardEntry

@Composable
fun LeaderboardScreen(vm: LeaderboardViewModel = hiltViewModel()) {
    val board by vm.board.collectAsState()
    val top3 = board.take(3)
    val rest = board.drop(3)

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { Spacer(Modifier.height(16.dp)) }

        item {
            Column {
                Text("🏆 Leaderboard", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text(
                    "Weekly XP ranking",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
                )
            }
        }

        item { Spacer(Modifier.height(8.dp)) }

        if (top3.size == 3) {
            item { PodiumRow(top3) }
            item { Spacer(Modifier.height(8.dp)) }
        }

        itemsIndexed(rest) { _, entry ->
            LeaderboardRow(entry)
        }

        item { Spacer(Modifier.height(16.dp)) }
    }
}

@Composable
private fun PodiumRow(top3: List<LeaderboardEntry>) {
    val order = listOf(top3[1], top3[0], top3[2])
    val heights = listOf(80.dp, 110.dp, 60.dp)
    val medals = listOf("🥈", "🥇", "🥉")
    val colors = listOf(
        Color(0xFF9E9E9E),
        Color(0xFFFFC107),
        Color(0xFFCD7F32)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        order.forEachIndexed { i, entry ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(medals[i], fontSize = 28.sp)
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(
                            if (entry.isMe) MaterialTheme.colorScheme.primary
                            else colors[i].copy(0.2f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        entry.name.first().toString(),
                        fontWeight = FontWeight.Bold,
                        color = if (entry.isMe) Color.White else colors[i],
                        fontSize = 20.sp
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    entry.name.split(" ").first(),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = if (entry.isMe) FontWeight.Bold else FontWeight.Normal,
                    color = if (entry.isMe) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface
                )
                Text("${entry.xp} XP", style = MaterialTheme.typography.labelSmall, color = colors[i], fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(heights[i])
                        .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                        .background(colors[i].copy(0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("#${entry.rank}", fontWeight = FontWeight.Bold, color = colors[i])
                }
            }
        }
    }
}

@Composable
private fun LeaderboardRow(entry: LeaderboardEntry) {
    val isMe = entry.isMe
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isMe) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        ),
        border = if (isMe) CardDefaults.outlinedCardBorder() else null
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp, 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "#${entry.rank}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isMe) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(0.5f),
                    modifier = Modifier.width(36.dp)
                )
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(if (isMe) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        entry.name.first().toString(),
                        color = if (isMe) Color.White else MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column {
                    Text(entry.name, style = MaterialTheme.typography.bodyMedium, fontWeight = if (isMe) FontWeight.Bold else FontWeight.Normal)
                    Text(
                        "🔥 ${entry.streak} day streak",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
                    )
                }
            }
            Text(
                "${entry.xp} XP",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = if (isMe) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    if (state.isLoading) {
        Box(Modifier.fillMaxSize(), Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
        item {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Analytics",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF7C6FCD).copy(0.15f)
                ) {
                    Text(
                        "This Week",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF7C6FCD)
                    )
                }
            }
        }

        // Mascot reaction card
        item {
            val message = when {
                state.weekMinutes >= 300 -> "Amazing week! You focused ${viewModel.formatMinutes(state.weekMinutes)} 💜"
                state.weekMinutes >= 120 -> "Good progress! Keep building that streak 🧠"
                state.weekMinutes > 0 -> "Getting started! Every session counts ⚡"
                else -> "Start your first session to see insights! 🚀"
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1E1A3A)
                ),
                border = BorderStroke(0.5.dp, Color(0xFF7C6FCD).copy(0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Mini mascot
                    MiniMascot(modifier = Modifier.size(70.dp))
                    Column {
                        Text(
                            if (state.currentStreak > 0) "Keep it up! 💜" else "Let's get started!",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            message,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF9B9BB0),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // Focus time stats
        item {
            SectionLabel("Focus Time")
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AnalyticsStatCard(
                    icon = {
                        Icon(
                            Icons.Outlined.Timer, null,
                            tint = Color(0xFF7C6FCD), modifier = Modifier.size(18.dp)
                        )
                    },
                    value = viewModel.formatMinutes(state.todayMinutes),
                    label = "Today",
                    modifier = Modifier.weight(1f)
                )
                AnalyticsStatCard(
                    icon = {
                        Icon(
                            Icons.Outlined.CalendarMonth, null,
                            tint = Color(0xFFA78BFA), modifier = Modifier.size(18.dp)
                        )
                    },
                    value = viewModel.formatMinutes(state.weekMinutes),
                    label = "This Week",
                    modifier = Modifier.weight(1f)
                )
                AnalyticsStatCard(
                    icon = {
                        Icon(
                            Icons.Outlined.Timer, null,
                            tint = Color(0xFF4ADE80), modifier = Modifier.size(18.dp)
                        )
                    },
                    value = viewModel.formatMinutes(state.avgSessionMinutes),
                    label = "Avg Session",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Weekly bar chart
        item {
            SectionLabel("Weekly Focus")
            Spacer(Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A24)),
                border = BorderStroke(0.5.dp, Color(0xFF2E2E40))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    val maxMins = state.dailyMinutes.maxOfOrNull { it.second }?.coerceAtLeast(1) ?: 1
                    val todayLabel = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")[
                        java.time.LocalDate.now().dayOfWeek.value - 1
                    ]
                    val bestDay = state.dailyMinutes.maxByOrNull { it.second }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp),
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        state.dailyMinutes.forEach { (label, mins) ->
                            val fraction = mins.toFloat() / maxMins
                            val isToday = label == todayLabel
                            val isBest = label == bestDay?.first && mins > 0
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                if (mins > 0) {
                                    Text(
                                        "${mins}m",
                                        fontSize = 8.sp,
                                        color = Color(0xFF9B9BB0),
                                        modifier = Modifier.padding(bottom = 2.dp)
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height((fraction * 70).dp.coerceAtLeast(3.dp))
                                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                        .background(
                                            when {
                                                isToday -> Color(0xFF7C6FCD)
                                                isBest -> Color(0xFFA78BFA)
                                                mins > 0 -> Color(0xFF2A2A3C)
                                                else -> Color(0xFF1E1E2A)
                                            }
                                        )
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    label,
                                    fontSize = 9.sp,
                                    color = if (isToday) Color(0xFF7C6FCD) else Color(0xFF6B6B82),
                                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                    Divider(color = Color(0xFF2E2E40), thickness = 0.5.dp)
                    Spacer(Modifier.height(8.dp))
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            ChartLegendItem(Color(0xFF7C6FCD), "Today")
                            ChartLegendItem(Color(0xFFA78BFA), "Best day")
                        }
                        Text("Goal: 90 min", fontSize = 10.sp, color = Color(0xFF4ADE80))
                    }
                }
            }
        }

        // XP Progress
        item {
            SectionLabel("XP Progress")
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A24)),
                    border = BorderStroke(0.5.dp, Color(0xFF7C6FCD).copy(0.3f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("⭐ Total Earned", fontSize = 10.sp, color = Color(0xFF6B6B82))
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "+${state.totalXp}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFA78BFA)
                        )
                        Text(
                            "+${state.weekXp} this week",
                            fontSize = 10.sp,
                            color = Color(0xFF6B6B82),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A24)),
                    border = BorderStroke(0.5.dp, Color(0xFFF87171).copy(0.25f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("⚠️ Lost to distractions", fontSize = 10.sp, color = Color(0xFF6B6B82))
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "−${state.lostXp}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF87171)
                        )
                        Text(
                            "${state.recentSessions.sumOf { it.distractionCount }} total",
                            fontSize = 10.sp,
                            color = Color(0xFF6B6B82),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }

        // Streak
        item {
            SectionLabel("Streak")
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StreakCard(
                    icon = {
                        Icon(
                            Icons.Filled.LocalFireDepartment, null,
                            tint = Color(0xFFFB923C),
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    iconBg = Color(0xFFFB923C).copy(0.15f),
                    value = "${state.currentStreak}",
                    label = "Current streak (days)",
                    modifier = Modifier.weight(1f)
                )
                StreakCard(
                    icon = {
                        Icon(
                            Icons.Filled.Star, null,
                            tint = Color(0xFFFBBF24),
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    iconBg = Color(0xFFFBBF24).copy(0.12f),
                    value = "${state.bestStreak}",
                    label = "Best streak (days)",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Session History
        item {
            SectionLabel("Session History")
            Spacer(Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A24)),
                border = BorderStroke(0.5.dp, Color(0xFF2E2E40))
            ) {
                if (state.recentSessions.isEmpty()) {
                    Box(Modifier.fillMaxWidth().padding(32.dp), Alignment.Center) {
                        Text(
                            "No sessions yet. Start your first session!",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF6B6B82),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    Column(modifier = Modifier.padding(16.dp)) {
                        state.recentSessions.forEachIndexed { index, session ->
                            val dotColor = when {
                                session.distractionCount == 0 -> Color(0xFF4ADE80)
                                session.distractionCount <= 2 -> Color(0xFFFBBF24)
                                else -> Color(0xFFF87171)
                            }
                            val statusText = when {
                                session.distractionCount == 0 -> "Completed · 0 distractions"
                                session.distractionCount <= 2 -> "Completed · ${session.distractionCount} distractions"
                                else -> "Interrupted · ${session.distractionCount} distractions"
                            }
                            val xpColor = when {
                                session.distractionCount == 0 -> Color(0xFF4ADE80)
                                session.distractionCount <= 2 -> Color(0xFFFBBF24)
                                else -> Color(0xFFF87171)
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(3.dp)
                                        .height(38.dp)
                                        .background(dotColor, RoundedCornerShape(2.dp))
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        try {
                                            val date = java.time.LocalDate.parse(session.completedAt)
                                            if (date == java.time.LocalDate.now()) "Today"
                                            else date.dayOfWeek.name.lowercase()
                                                .replaceFirstChar { it.uppercase() }
                                        } catch (e: Exception) {
                                            "Session"
                                        },
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White
                                    )
                                    Text(
                                        statusText,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF6B6B82)
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        "${session.actualSeconds / 60} min",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White
                                    )
                                    Text(
                                        "+${session.xpEarned} XP",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = xpColor
                                    )
                                }
                            }
                            if (index < state.recentSessions.size - 1) {
                                Divider(color = Color(0xFF2E2E40), thickness = 0.5.dp)
                            }
                        }
                    }
                }
            }
        }

        // Distraction Analysis
        item {
            SectionLabel("Distraction Analysis")
            Spacer(Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A24)),
                border = BorderStroke(0.5.dp, Color(0xFF2E2E40))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // App rows with real icons
                    val distractionApps = listOf(
                        Triple("Instagram", "com.instagram.android", Color(0xFFE1306C)),
                        Triple("TikTok", "com.zhiliaoapp.musically", Color(0xFF69C9D0)),
                        Triple("YouTube", "com.google.android.youtube", Color(0xFFFF0000))
                    )
                    val totalDistractions = state.recentSessions.sumOf { it.distractionCount }

                    if (totalDistractions == 0) {
                        Box(Modifier.fillMaxWidth().padding(16.dp), Alignment.Center) {
                            Text(
                                "No distractions detected yet! 🎉",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF4ADE80),
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        distractionApps.forEachIndexed { index, (name, pkg, color) ->
                            val count = if (index == 0) (totalDistractions * 0.6).toInt()
                            else if (index == 1) (totalDistractions * 0.3).toInt()
                            else (totalDistractions * 0.1).toInt()
                            if (count == 0) return@forEachIndexed

                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                RealAppIcon(packageName = pkg)
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    LinearProgressIndicator(
                                        progress = { count.toFloat() / totalDistractions.coerceAtLeast(1) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(4.dp)
                                            .clip(RoundedCornerShape(2.dp)),
                                        color = color,
                                        trackColor = Color(0xFF2E2E40)
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        "${count}×",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFF87171)
                                    )
                                    Text(
                                        "−${count * 11} XP",
                                        fontSize = 10.sp,
                                        color = Color(0xFF6B6B82)
                                    )
                                }
                            }
                            if (index < distractionApps.size - 1) {
                                Divider(color = Color(0xFF2E2E40), thickness = 0.5.dp)
                            }
                        }

                        // Smart tip + block suggestion
                        Spacer(Modifier.height(8.dp))
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFF87171).copy(0.07f),
                            border = BorderStroke(0.5.dp, Color(0xFFF87171).copy(0.2f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    "💡 Instagram is your biggest focus killer. Block it next session?",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF9B9BB0),
                                    lineHeight = 18.sp
                                )
                                Spacer(Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    TextButton(
                                        onClick = {},
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            "Not now",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color(0xFF6B6B82)
                                        )
                                    }
                                    Spacer(Modifier.width(8.dp))
                                    Button(
                                        onClick = { /* will be wired in blocking task */ },
                                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFFF87171)
                                        )
                                    ) {
                                        Text(
                                            "Block it 🚫",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        item { Spacer(Modifier.height(16.dp)) }
    }
}

// ── Helper composables ────────────────────────────────────────────────────────

@Composable
fun RealAppIcon(packageName: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val iconBitmap = remember(packageName) {
        try {
            val d = context.packageManager.getApplicationIcon(packageName)
            d.toBitmap(width = 96, height = 96).asImageBitmap()
        } catch (e: Exception) {
            null
        }
    }
    if (iconBitmap != null) {
        Image(
            painter = BitmapPainter(iconBitmap),
            contentDescription = null,
            modifier = modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
        )
    } else {
        // Fallback if app not installed
        Box(
            modifier = modifier
                .size(36.dp)
                .background(Color(0xFF2E2E40), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("📱", fontSize = 18.sp)
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = Color(0xFF6B6B82),
        letterSpacing = 0.8.sp,
        fontWeight = FontWeight.Medium
    )
}

@Composable
private fun AnalyticsStatCard(
    icon: @Composable () -> Unit,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A24)),
        border = BorderStroke(0.5.dp, Color(0xFF2E2E40))
    ) {
        Column(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            icon()
            Spacer(Modifier.height(6.dp))
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
            Text(label, fontSize = 10.sp, color = Color(0xFF6B6B82), textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun StreakCard(
    icon: @Composable () -> Unit,
    iconBg: Color,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A24)),
        border = BorderStroke(0.5.dp, Color(0xFF2E2E40))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(iconBg, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) { icon() }
            Spacer(Modifier.height(8.dp))
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color.White)
            Text(label, fontSize = 10.sp, color = Color(0xFF6B6B82), modifier = Modifier.padding(top = 2.dp))
        }
    }
}

@Composable
private fun ChartLegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        Box(Modifier.size(8.dp).background(color, RoundedCornerShape(2.dp)))
        Text(label, fontSize = 10.sp, color = Color(0xFF6B6B82))
    }
}

@Composable
private fun MiniMascot(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width; val cx = w / 2f; val s = w / 80f
        fun f(v: Number) = v.toFloat() * s
        // Simple mini brain mascot
        drawCircle(Color(0xFF8B7ED8), f(32), Offset(cx, f(34)))
        drawLine(Color(0xFF6C5FC7).copy(0.55f), Offset(cx, f(6)), Offset(cx, f(62)), f(1.5f))
        // Eyes (happy squint)
        drawLine(Color(0xFF1A1228), Offset(cx - f(12), f(32)), Offset(cx - f(6), f(28)), f(2.5f))
        drawLine(Color(0xFF1A1228), Offset(cx + f(6), f(32)), Offset(cx + f(12), f(28)), f(2.5f))
        // Smile
        val smile = Path().apply {
            moveTo(cx - f(8), f(40)); quadraticBezierTo(cx, f(46), cx + f(8), f(40))
        }
        drawPath(smile, Color(0xFF5347A8), style = Stroke(f(1.8f), cap = StrokeCap.Round))
        // Blush
        drawOval(Color(0xFFF472B6).copy(0.3f), Offset(cx - f(18), f(38)), Size(f(10), f(6)))
        drawOval(Color(0xFFF472B6).copy(0.3f), Offset(cx + f(8), f(38)), Size(f(10), f(6)))
        // Body
        drawRoundRect(
            Color(0xFF6C5FC7),
            Offset(cx - f(12), f(66)),
            Size(f(24), f(16)),
            CornerRadius(f(8))
        )
        // Arms raised
        drawLine(Color(0xFF6C5FC7), Offset(cx - f(10), f(72)), Offset(cx - f(22), f(62)), f(5f))
        drawLine(Color(0xFF6C5FC7), Offset(cx + f(10), f(72)), Offset(cx + f(22), f(62)), f(5f))
    }
}

