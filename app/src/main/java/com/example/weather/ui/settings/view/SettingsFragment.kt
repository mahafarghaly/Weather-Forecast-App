package com.example.weather.ui.settings.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weather.R
import com.example.weather.databinding.FragmentSettingsBinding
import com.example.weather.ui.home.view.HomeActivity
import com.example.weather.utils.changeLanguageLocaleTo
import com.example.weather.utils.getLanguageLocale
import com.mad.iti.weather.sharedPreferences.SettingSharedPreferences


class SettingsFragment : Fragment() {


    private lateinit var binding: FragmentSettingsBinding


    private val settingSharedPreferences by lazy {
        SettingSharedPreferences.getInstance(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (getLanguageLocale() == "ar") {
            binding.radioButtonArabic.toggle()
        } else {
            binding.radioButtonEnglish.toggle()
        }
        when (settingSharedPreferences.getWindSpeedPref()) {
            SettingSharedPreferences.METER_PER_SECOND -> binding.radioButtonMPerSec.toggle()
            SettingSharedPreferences.MILE_PER_HOUR -> binding.radioButtonMilePerHour.toggle()
        }

        when (settingSharedPreferences.getLocationPref()) {
            SettingSharedPreferences.GPS -> binding.radioButtonGPS.toggle()
            SettingSharedPreferences.MAP -> binding.radioButtonMap.toggle()
        }

        when (settingSharedPreferences.getTempPref()) {
            SettingSharedPreferences.CELSIUS -> binding.radioButtonC.toggle()
            SettingSharedPreferences.KELVIN -> binding.radioButtonK.toggle()
            SettingSharedPreferences.FAHRENHEIT -> binding.radioButtonF.toggle()
        }

        binding.radioGroupChooseLanguage.setOnCheckedChangeListener { _, checked ->
            when (checked) {
                R.id.radio_button_Arabic -> changeLanguageLocaleTo("ar")
                R.id.radio_button_English -> changeLanguageLocaleTo("en")
            }
        }
        binding.radioGroupLocation.setOnCheckedChangeListener { _, checked ->
            when (checked) {
                R.id.radio_button_GPS -> {
                    settingSharedPreferences.setLocationPref(
                        SettingSharedPreferences.GPS
                    )
                    requireActivity().recreate()
                }
                R.id.radio_button_map -> {
                    settingSharedPreferences.setLocationPref(
                        SettingSharedPreferences.MAP
                    )
                    with(Intent(requireContext(), HomeActivity::class.java)) {
                        putExtra(
                            SettingSharedPreferences.NAVIGATE_TO_MAP,
                            SettingSharedPreferences.SET_LOCATION_AS_MAIN_LOCATION
                        )
                        startActivity(this)
                    }
                }
            }
        }
        binding.radioGroupWindSpeed.setOnCheckedChangeListener { _, checked ->
            when (checked) {
                R.id.radio_button_MPerSec -> settingSharedPreferences.setWindSpeedPref(
                    SettingSharedPreferences.METER_PER_SECOND
                )
                R.id.radio_button_MilePerHour -> settingSharedPreferences.setWindSpeedPref(
                    SettingSharedPreferences.MILE_PER_HOUR
                )
            }
        }
        binding.radioGroupTempDegree.setOnCheckedChangeListener { _, checked ->
            when (checked) {
                R.id.radio_button_C -> settingSharedPreferences.setTempPref(
                    SettingSharedPreferences.CELSIUS
                )
                R.id.radio_button_K -> settingSharedPreferences.setTempPref(
                    SettingSharedPreferences.KELVIN
                )
                R.id.radio_button_F -> settingSharedPreferences.setTempPref(
                    SettingSharedPreferences.FAHRENHEIT
                )
            }
       }


    }


}

