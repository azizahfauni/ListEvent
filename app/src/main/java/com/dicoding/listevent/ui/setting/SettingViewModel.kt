package com.dicoding.listevent.ui.setting

import SettingPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.launch

class SettingViewModel(private val pref: SettingPreferences) : ViewModel() {

    // Mengambil pengaturan tema sebagai LiveData
    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData() // Mengubah Flow menjadi LiveData
    }

    // Menyimpan pengaturan tema
    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive) // Menyimpan pengaturan tema ke DataStore
        }
    }

    // Metode untuk mendapatkan pengaturan Daily Reminder
//    fun getReminderSetting(): LiveData<Boolean> {
//        return pref.getDailyReminderSetting().asLiveData()
//    }
//
//    // Metode untuk menyimpan pengaturan Daily Reminder
//    fun saveReminderSetting(isReminderActive: Boolean) {
//        viewModelScope.launch {
//            pref.saveDailyReminderSetting(isReminderActive)
//        }
//
//    }
}
