import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Ekstensi untuk membuat DataStore di Context
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    // Kunci untuk tema gelap
    private val THEME_KEY = booleanPreferencesKey("theme_setting")

    // Kunci untuk daily reminder
//    private val DAILY_REMINDER_KEY = booleanPreferencesKey("daily_reminder_setting")

    // Fungsi untuk mendapatkan preferensi tema
    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: false // Default tema tidak aktif (light mode)
        }
    }

    // Fungsi untuk menyimpan preferensi tema
    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkModeActive
        }
    }

    // Fungsi untuk mendapatkan preferensi daily reminder
//    fun getDailyReminderSetting(): Flow<Boolean> {
//        return dataStore.data.map { preferences ->
//            preferences[DAILY_REMINDER_KEY] ?: false // Default daily reminder tidak aktif
//        }
//    }
//
//    // Fungsi untuk menyimpan preferensi daily reminder
//    suspend fun saveDailyReminderSetting(isDailyReminderActive: Boolean) {
//        dataStore.edit { preferences ->
//            preferences[DAILY_REMINDER_KEY] = isDailyReminderActive
//        }
//    }

    companion object {
        @Volatile
        private var INSTANCE: SettingPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
