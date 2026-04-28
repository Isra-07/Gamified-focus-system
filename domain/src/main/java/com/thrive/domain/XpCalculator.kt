package com.thrive.domain

object XpCalculator {

    /**
     * Base XP = duration in minutes × 10
     * Bonus ×1.2 if zero distractions
     * Penalty = distractionCount × 11
     * Minimum XP earned = 10 (always reward something)
     */
    fun calculate(
        durationMinutes: Int,
        distractionCount: Int
    ): Int {
        val base = durationMinutes * 10
        val multiplier = if (distractionCount == 0) 1.2f else 1.0f
        val earned = (base * multiplier).toInt()
        val penalty = distractionCount * 11
        return maxOf(10, earned - penalty)
    }

    fun focusRate(distractionCount: Int): Float =
        maxOf(0.5f, 1f - distractionCount * 0.1f)
}
