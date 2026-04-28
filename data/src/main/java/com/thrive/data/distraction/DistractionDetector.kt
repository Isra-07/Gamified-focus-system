package com.thrive.data.distraction

import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Process
import android.provider.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DistractionDetector @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val distractingPackages = setOf(
        "com.instagram.android",
        "com.zhiliaoapp.musically",
        "com.google.android.youtube",
        "com.twitter.android",
        "com.snapchat.android",
        "com.facebook.katana",
        "com.reddit.frontpage",
        "com.netflix.mediaclient",
        "com.discord",
        "com.whatsapp",
        "org.telegram.messenger"
    )

    fun hasPermission(): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.unsafeCheckOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            context.packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }

    fun openPermissionSettings() {
        context.startActivity(
            Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        )
    }

    fun getForegroundApp(): String? {
        return try {
            val usm = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            val now = System.currentTimeMillis()
            val stats = usm.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                now - 10_000L,
                now
            )
            val foreground = stats
                ?.filter { it.lastTimeUsed > 0 }
                ?.maxByOrNull { it.lastTimeUsed }
                ?.packageName

            // Ignore our own app and system apps
            if (foreground == context.packageName) return null
            if (foreground?.startsWith("com.android") == true) return null
            if (foreground?.startsWith("com.miui") == true) return null
            if (foreground?.startsWith("com.xiaomi") == true) return null
            if (foreground?.startsWith("android") == true) return null

            foreground
        } catch (e: Exception) {
            null
        }
    }

    fun isDistracting(pkg: String): Boolean = pkg in distractingPackages

    fun getAppName(pkg: String): String {
        return try {
            val info = context.packageManager.getApplicationInfo(pkg, 0)
            context.packageManager.getApplicationLabel(info).toString()
        } catch (e: Exception) {
            pkg.substringAfterLast(".")
        }
    }
}
