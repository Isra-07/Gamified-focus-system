package com.thrive.domain.timer

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TimerController {
    fun countdown(totalSeconds: Int): Flow<Int> = flow {
        var remaining = totalSeconds
        emit(remaining)
        while (remaining > 0) {
            delay(1_000)
            remaining -= 1
            emit(remaining)
        }
    }
}
