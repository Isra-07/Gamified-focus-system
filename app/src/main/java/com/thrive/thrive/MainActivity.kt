package com.thrive.thrive

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.thrive.thrive.ui.theme.ThriveTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ThriveTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    TimerScreen()
                }
            }
        }
    }
}