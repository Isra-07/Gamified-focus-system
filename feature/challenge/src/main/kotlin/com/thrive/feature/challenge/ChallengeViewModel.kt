package com.thrive.feature.challenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thrive.domain.model.Challenge
import com.thrive.domain.usecase.EvaluateChallengesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed interface ChallengeUiState {
    data object Loading : ChallengeUiState
    data class Ready(val challenges: List<Challenge>) : ChallengeUiState
}

@HiltViewModel
class ChallengeViewModel @Inject constructor(
    evaluateChallenges: EvaluateChallengesUseCase
) : ViewModel() {
    val uiState: StateFlow<ChallengeUiState> = evaluateChallenges.observe()
        .map { ChallengeUiState.Ready(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ChallengeUiState.Loading)
}
