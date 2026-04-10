package com.thrive.thrive

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun TimerScreen(
    onSessionComplete: (Int) -> Unit = {}
) {
    var selectedTime by remember { mutableIntStateOf(25) }
    var isRunning by remember { mutableStateOf(false) }
    var secondsLeft by remember { mutableIntStateOf(25 * 60) }
    val times = listOf(15, 25, 30, 45, 60)

    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (secondsLeft > 0) {
                delay(1000L)
                secondsLeft--
            }
            isRunning = false
            val pointsEarned = selectedTime * 10
            onSessionComplete(pointsEarned)
        }
    }

    val minutes = secondsLeft / 60
    val seconds = secondsLeft % 60
    val timeText = String.format(Locale.US, "%02d:%02d", minutes, seconds)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = timeText,
            fontSize = 72.sp,
            color = Color(0xFF00f0ff)
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (!isRunning) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                times.forEach { time ->
                    Button(
                        onClick = {
                            selectedTime = time
                            secondsLeft = time * 60
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedTime == time)
                                Color(0xFF00f0ff)
                            else
                                Color(0xFF2d3a4a)
                        )
                    ) {
                        Text("$time", color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        Button(
            onClick = { isRunning = !isRunning },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isRunning) Color(0xFFFF3B9E) else Color(0xFF00f0ff)
            )
        ) {
            Text(
                if (isRunning) "Pause" else "Start Focus",
                color = Color.White
            )
        }

        if (isRunning) {
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(
                onClick = {
                    isRunning = false
                    secondsLeft = selectedTime * 60
                }
            ) {
                Text("Cancel", color = Color.Gray)
            }
        }
    }
}