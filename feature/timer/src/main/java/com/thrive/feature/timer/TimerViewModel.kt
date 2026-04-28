package com.thrive.feature.timer

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thrive.data.distraction.DistractionDetector
import com.thrive.domain.DistractionEvent
import com.thrive.domain.DistractionRepository
import com.thrive.domain.FocusSession
import com.thrive.domain.SessionRepository
import com.thrive.domain.UserRepository
import com.thrive.domain.XpCalculator
import com.thrive.domain.repository.ChallengeRepository
import kotlinx.coroutines.flow.first
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

data class TimerState(
    val phase: Phase = Phase.IDLE,
    val selectedMinutes: Int = 30,
    val remainingSeconds: Int = 30 * 60,
    val totalSeconds: Int = 30 * 60,
    val distractionCount: Int = 0,
    val warningApp: String? = null,
    val needsPermission: Boolean = false,
    val result: SessionResult? = null
)

enum class Phase { IDLE, RUNNING, PAUSED }

data class SessionResult(
    val xpEarned: Int,
    val focusRatePct: Int,
    val distractionCount: Int,
    val challengeCompleted: Boolean = false,
    val completedChallengeTitle: String = "",
    val completedChallengeXp: Int = 0,
    val streakIncreased: Boolean = false,
    val oldStreak: Int = 0,
    val newStreak: Int = 0,
    val focusRate: Float = focusRatePct / 100f,
    val earnedXp: Int = xpEarned
)

@HiltViewModel
class TimerViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository,
    private val distractionRepository: DistractionRepository,
    private val detector: DistractionDetector,
    private val challengeRepository: ChallengeRepository
) : ViewModel() {

    private val DEBUG_FAST_MODE = true  // set to false before release

    private val _state = MutableStateFlow(TimerState())
    val state: StateFlow<TimerState> = _state.asStateFlow()

    private var countdownJob: Job? = null
    private var pollingJob: Job? = null
    private var sessionId: String = ""

    init {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.checkAndResetStreakIfBroken()
            challengeRepository.initDefaultChallenges()
        }
    }

    fun selectDuration(minutes: Int) {
        if (_state.value.phase == Phase.IDLE) {
            _state.update {
                it.copy(
                    selectedMinutes = minutes,
                    remainingSeconds = minutes * 60,
                    totalSeconds = minutes * 60
                )
            }
        }
    }

    fun startSession() {
        try {
            if (!detector.hasPermission()) {
                _state.update { it.copy(needsPermission = true) }
                return
            }
            launchSession()
        } catch (e: Exception) {
            _state.update { it.copy(needsPermission = false) }
            launchSession(withDetection = false)
        }
    }

    fun startWithoutPermission() {
        _state.update { it.copy(needsPermission = false) }
        launchSession(withDetection = false)
    }

    fun requestPermissionFromSettings(context: android.content.Context) {
        // Only call this when user explicitly taps "Open Settings" button
        val intent = android.content.Intent(
            android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS
        )
        context.startActivity(intent)
    }

    fun pause() {
        countdownJob?.cancel()
        pollingJob?.cancel()
        stopForegroundTimer()
        _state.update { it.copy(phase = Phase.PAUSED) }
    }

    fun resume() {
        if (_state.value.phase != Phase.PAUSED) return
        startCountdown()
        try {
            if (detector.hasPermission()) startPolling()
        } catch (e: Exception) {
            // Keep resume reliable even if usage-access detection is unavailable.
        }
        _state.update { it.copy(phase = Phase.RUNNING) }
    }

    fun stop() {
        countdownJob?.cancel()
        pollingJob?.cancel()
        stopForegroundTimer()
        _state.update { TimerState() }
        sessionId = ""
    }

    fun dismissWarning() = _state.update { it.copy(warningApp = null) }

    fun dismissResult() {
        stopForegroundTimer()
        _state.update { TimerState() }
        sessionId = ""
    }

    private fun launchSession(withDetection: Boolean = true) {
        try {
            sessionId = java.util.UUID.randomUUID().toString()
            _state.update {
                it.copy(
                    phase = Phase.RUNNING,
                    distractionCount = 0,
                    result = null,
                    warningApp = null,
                    totalSeconds = it.selectedMinutes * 60,
                    remainingSeconds = it.selectedMinutes * 60,
                    needsPermission = false
                )
            }
            startCountdown()
            if (withDetection) {
                try { startPolling() } catch (e: Exception) { /* ignore */ }
            }
        } catch (e: Exception) {
            _state.update { TimerState() }
        }
    }

    private fun startCountdown() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch(Dispatchers.Default) {
            val tickMs = if (DEBUG_FAST_MODE) 16L else 1_000L
            val secondsPerTick = if (DEBUG_FAST_MODE) 60 else 1
            while (isActive) {
                delay(tickMs)
                val current = _state.value.remainingSeconds
                val next = current - secondsPerTick
                if (next <= 0) {
                    withContext(Dispatchers.Main) { onComplete() }
                    break
                }
                _state.update { it.copy(remainingSeconds = next) }
            }
        }
    }

    private fun startPolling() {
        pollingJob?.cancel()
        pollingJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                delay(5_000L)
                val fg = detector.getForegroundApp() ?: continue
                if (fg == context.packageName) continue
                if (!detector.isDistracting(fg)) continue

                val appName = detector.getAppName(fg)
                val sid = sessionId

                distractionRepository.saveDistraction(
                    DistractionEvent(
                        id = UUID.randomUUID().toString(),
                        sessionId = sid,
                        packageName = fg,
                        appName = appName,
                        timestamp = System.currentTimeMillis()
                    )
                )

                _state.update { it.copy(distractionCount = it.distractionCount + 1, warningApp = appName) }

                delay(4_000L)
                _state.update { it.copy(warningApp = null) }
            }
        }
    }

    private fun onComplete() {
        countdownJob?.cancel()
        pollingJob?.cancel()
        val s = _state.value
        val xp = XpCalculator.calculate(s.selectedMinutes, s.distractionCount)
        val focusRate = XpCalculator.focusRate(s.distractionCount)

        viewModelScope.launch(Dispatchers.IO) {
            // 1. Save focus session
            sessionRepository.saveSession(
                FocusSession(
                    id = sessionId,
                    startTime = System.currentTimeMillis() - s.selectedMinutes * 60_000L,
                    targetSeconds = s.totalSeconds,
                    actualSeconds = s.totalSeconds,
                    xpEarned = xp,
                    distractionCount = s.distractionCount,
                    focusRate = focusRate,
                    completedAt = LocalDate.now().toString()
                )
            )

            // 2. Always add XP (every session earns XP)
            userRepository.addXp(xp)

            // 3. Get current user to check streak and challenge date
            val user = userRepository.getUser()
            val oldStreak = user?.currentStreak ?: 0
            val challengeAlreadyDoneToday = user?.lastChallengeDate == LocalDate.now().toString()

            // 4. Calculate totals for challenge evaluation
            val allSessions = sessionRepository.observeAllSessions().first()
            val totalSessions = allSessions.size
            val totalMinutes = allSessions.sumOf { it.actualSeconds / 60 }
            val hadCleanSession = s.distractionCount == 0

            // 5. Evaluate challenge (only completes if not already done today)
            val result = challengeRepository.evaluateChallenge(
                totalSessions = totalSessions,
                totalMinutes = totalMinutes,
                hadCleanSession = hadCleanSession,
                lastSessionMinutes = s.selectedMinutes,
                lastSessionDistractions = s.distractionCount,
                currentStreak = oldStreak,
                challengeAlreadyCompletedToday = challengeAlreadyDoneToday
            )

            // 6. Update streak ONLY if challenge was completed
            var newStreak = oldStreak
            var streakIncreased = false
            if (result.challengeCompleted) {
                // Add challenge XP reward ONCE (daily gate prevents duplicates)
                if (result.completedXpReward > 0) {
                    userRepository.addXp(result.completedXpReward)
                }
                userRepository.updateStreakOnChallengeComplete()
                val updatedUser = userRepository.getUser()
                newStreak = updatedUser?.currentStreak ?: oldStreak
                streakIncreased = newStreak > oldStreak
            }

            // 7. Emit result to UI
            withContext(Dispatchers.Main) {
                _state.update {
                    TimerState(
                        phase = Phase.IDLE,
                        result = SessionResult(
                            xpEarned = xp,
                            focusRatePct = (focusRate * 100).toInt(),
                            distractionCount = s.distractionCount,
                            challengeCompleted = result.challengeCompleted,
                            completedChallengeTitle = result.completedTitle,
                            completedChallengeXp = result.completedXpReward,
                            streakIncreased = streakIncreased,
                            oldStreak = oldStreak,
                            newStreak = newStreak
                        )
                    )
                }
            }
        }
        stopForegroundTimer()
        sessionId = ""
    }

    private fun startForegroundTimer(seconds: Int) {
        val i = Intent(context, FocusTimerService::class.java).apply {
            action = FocusTimerService.ACTION_START
            putExtra(FocusTimerService.EXTRA_SECONDS, seconds)
        }
        ContextCompat.startForegroundService(context, i)
    }

    private fun stopForegroundTimer() {
        val i = Intent(context, FocusTimerService::class.java).apply {
            action = FocusTimerService.ACTION_STOP
        }
        context.startService(i)
    }
}
