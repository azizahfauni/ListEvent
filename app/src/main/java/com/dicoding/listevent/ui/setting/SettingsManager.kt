package com.dicoding.listevent.ui.setting

import android.content.Context
import android.content.SharedPreferences

class SettingsManager(context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    var isNotificationEnabled: Boolean
        get() = preferences.getBoolean("NOTIFICATION_ENABLED", false)
        set(value) {
            preferences.edit().putBoolean("NOTIFICATION_ENABLED", value).apply()
        }
}
