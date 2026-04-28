package com.thrive.feature.timer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.max

class FocusTimerService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        ensureChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_STOP -> {
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
                return START_NOT_STICKY
            }
            ACTION_START -> {
                val seconds = intent.getIntExtra(EXTRA_SECONDS, 0)
                if (seconds <= 0) {
                    stopSelf()
                    return START_NOT_STICKY
                }
                startForeground(NOTIFICATION_ID, buildNotification(seconds))
                scope.launch {
                    var remaining = seconds
                    while (isActive && remaining > 0) {
                        delay(1_000L)
                        remaining = max(0, remaining - 1)
                        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        nm.notify(NOTIFICATION_ID, buildNotification(remaining))
                    }
                    stopForeground(STOP_FOREGROUND_REMOVE)
                    stopSelf()
                }
                return START_STICKY
            }
            else -> return START_NOT_STICKY
        }
    }

    private fun buildNotification(remainingSeconds: Int) =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("Thrive • Focus session")
            .setContentText("Time left: ${format(remainingSeconds)}")
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .build()

    private fun ensureChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID,
                    "Focus Timer",
                    NotificationManager.IMPORTANCE_LOW
                )
            )
        }
    }

    private fun format(seconds: Int): String {
        val m = seconds / 60
        val s = (seconds % 60).toString().padStart(2, '0')
        return "$m:$s"
    }

    companion object {
        const val ACTION_START = "com.thrive.feature.timer.action.START"
        const val ACTION_STOP = "com.thrive.feature.timer.action.STOP"
        const val EXTRA_SECONDS = "extra_seconds"

        private const val CHANNEL_ID = "focus_timer"
        private const val NOTIFICATION_ID = 42
    }
}

