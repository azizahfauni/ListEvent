package com.dicoding.listevent.ui.setting

import SettingPreferences
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import android.Manifest
import androidx.annotation.RequiresApi
import com.dicoding.listevent.databinding.FragmentSettingBinding
import dataStore
import java.util.concurrent.TimeUnit

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding
    private lateinit var settingViewModel: SettingViewModel
    private lateinit var settingPreferences: SettingPreferences

    private val requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Reminder enabled", Toast.LENGTH_SHORT).show()
//                immediateNotif() // Show immediate notification after permission is granted
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Initialize SettingPreferences
        settingPreferences = SettingPreferences.getInstance(requireContext().dataStore)

        // Initialize ViewModel
        settingViewModel = ViewModelProvider(this, SettingsViewModelFactory(settingPreferences))[SettingViewModel::class.java]

        // Observe theme preference and apply to Switch
        settingViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkMode: Boolean ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
            }
        }

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            settingViewModel.saveThemeSetting(isChecked)
        }


        val sharedPreference = requireContext().getSharedPreferences("event_prefs", Context.MODE_PRIVATE)
        val isNotifEnabled = sharedPreference.getBoolean("daily_reminder", false)

//        settingViewModel.getReminderSetting().observe(viewLifecycleOwner) { isReminderActive ->
//            binding.switchNotification.isChecked = isReminderActive
//        }

        // Handle notification switch changes
        binding.switchNotification.isChecked = isNotifEnabled

        binding.switchNotification.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPreference.edit()
            editor.putBoolean("daily_reminder", isChecked)
            editor.apply()
            if (isChecked) {
                // Start daily reminder and show immediate notification
                requestPermissions.launch(Manifest.permission.POST_NOTIFICATIONS)
                immediateNotif()
                startDailyReminder()
            } else {
                // Cancel daily reminder if switch is turned off
                WorkManager.getInstance(requireContext().applicationContext).cancelAllWorkByTag("daily_reminder")
            }
        }
    }

    private fun immediateNotif() {
        val oneTime = OneTimeWorkRequestBuilder<DailyReminderWorker>()
            .addTag("immediate_notif")
            .build()

        WorkManager.getInstance(requireContext().applicationContext).enqueue(oneTime)
    }

    // Start daily reminder using WorkManager
    private fun startDailyReminder() {
        val workRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(1, TimeUnit.DAYS)
            .addTag("daily_reminder")
            .build()

        WorkManager.getInstance(requireContext().applicationContext).enqueue(workRequest)
    }
}
