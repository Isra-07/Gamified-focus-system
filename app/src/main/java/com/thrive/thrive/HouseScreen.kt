package com.thrive.thrive

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thrive.thrive.ui.theme.*

@Composable
fun HouseScreen(
    currentLevel: Int = 1
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            colors = CardDefaults.cardColors(
                containerColor = CardBg
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🏠 My Home",
                    fontSize = 32.sp,
                    color = TextLight
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Display items based on level
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (currentLevel >= 1) Text("🌱", fontSize = 40.sp)
                    if (currentLevel >= 2) Text("🛋️", fontSize = 40.sp)
                    if (currentLevel >= 3) Text("💡", fontSize = 40.sp)
                    if (currentLevel >= 4) Text("📺", fontSize = 40.sp)
                    if (currentLevel >= 5) Text("🪑", fontSize = 40.sp)
                }
            }
        }
    }
}