package com.thrive.feature.challenge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.thrive.core.ui.components.BackIconButton
import com.thrive.core.ui.components.PrimaryGreenButton
import com.thrive.core.ui.components.ProgressCard
import com.thrive.core.ui.components.ThriveCard
import com.thrive.core.ui.components.ThriveScreen
import com.thrive.core.ui.theme.Aqua
import com.thrive.core.ui.theme.Leaf
import com.thrive.core.ui.theme.Sun
import com.thrive.core.ui.theme.TextPrimary
import com.thrive.core.ui.theme.TextSecondary

@Composable
fun ChallengeRoute(
    onBack: () -> Unit,
    viewModel: ChallengeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    ChallengeScreen(state, onBack)
}

@Composable
fun ChallengeScreen(state: ChallengeUiState, onBack: () -> Unit) {
    ThriveScreen {
        when (state) {
            ChallengeUiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
            is ChallengeUiState.Ready -> Column(Modifier.fillMaxSize().padding(top = 30.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                BackIconButton(onBack)
                ThriveCard(accent = Aqua) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Challenge Path", color = TextPrimary, style = MaterialTheme.typography.headlineMedium)
                        Text("${state.challenges.count { it.completed }}/${state.challenges.size} challenges", color = TextSecondary)
                    }
                }
                LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    items(state.challenges) { challenge ->
                        ThriveCard(accent = if (challenge.completed) Leaf else Sun) {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text(challenge.title, color = TextPrimary, style = MaterialTheme.typography.titleLarge)
                                Text(challenge.description, color = TextSecondary)
                                ProgressCard(
                                    title = "Progress",
                                    subtitle = "${challenge.progress}/${challenge.target} | reward ${challenge.rewardXp} XP",
                                    progress = challenge.progress / challenge.target.toFloat(),
                                    accent = if (challenge.completed) Leaf else Aqua
                                )
                                if (!challenge.completed) {
                                    PrimaryGreenButton("Start Focus Session") { }
                                }
                            }
                        }
                    }
                    item { Spacer(Modifier.height(90.dp)) }
                }
            }
        }
    }
}
