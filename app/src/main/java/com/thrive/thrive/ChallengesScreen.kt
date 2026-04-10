package com.thrive.thrive

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thrive.thrive.ui.theme.*

@Composable
fun ChallengesScreen(
    onChallengeComplete: (String) -> Unit = {}
) {
    val challenges = remember {
        listOf(
            Challenge(1, "Focus Beginner", 1, 0, "Small Plant", "🌱"),
            Challenge(2, "Daily Habit", 3, 0, "Sofa", "🛋️"),
            Challenge(3, "Starting Out", 5, 0, "Lamp", "💡"),
            Challenge(4, "Consistent", 7, 0, "Television", "📺"),
            Challenge(5, "Committed", 10, 0, "Table", "🪑")
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(challenges) { challenge ->
            ChallengeCard(challenge = challenge)
        }
    }
}

@Composable
fun ChallengeCard(challenge: Challenge) {
    val progress = challenge.currentProgress.toFloat() / challenge.targetSessions

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = CardBg
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = challenge.icon,
                    fontSize = 32.sp,
                    modifier = Modifier.padding(end = 12.dp)
                )
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = challenge.title,
                        color = TextLight,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Reward: ${challenge.reward}",
                        color = TextSoft,
                        fontSize = 14.sp
                    )
                }
                Text(
                    text = "${challenge.currentProgress}/${challenge.targetSessions}",
                    color = VioletLight,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxWidth(),
                color = VioletLight,
                trackColor = SurfaceWhite10
            )
        }
    }
}