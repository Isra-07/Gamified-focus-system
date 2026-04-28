package com.thrive.feature.friends

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FriendsScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Coming Soon", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Friends & groups will be here.", color = MaterialTheme.colorScheme.onSurface.copy(0.6f))
        Text("🤝", fontSize = 48.sp, modifier = Modifier.padding(top = 12.dp))
    }
}

