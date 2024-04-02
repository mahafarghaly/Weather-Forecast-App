package com.mad.iti.weather.sharedPreferences

import android.content.Context
import android.content.SharedPreferences

private const val SETTING_SHARED_PREFERENCES = "SETTING_SHARED_PREFERENCES"
private const val TEMP_PREFERENCES = "TEMP_PREFERENCES"
private const val WIND_SPEED_PREFERENCES = "WIND_SPEED_PREFERENCES"

class SettingSharedPreferences private constructor(applicationContext: Context) {
    private var sharedPreferences: SharedPreferences =
        applicationContext.getSharedPreferences(SETTING_SHARED_PREFERENCES, Context.MODE_PRIVATE)


    private val editor: SharedPreferences.Editor =
        sharedPreferences.edit()



    fun setTempPref(tempPref: String) {
        editor.putString(TEMP_PREFERENCES, tempPref).apply()
    }
    fun getTempPref(): String? {
        return sharedPreferences.getString(TEMP_PREFERENCES, CELSIUS)
    }
    fun setWindSpeedPref(windSpeedPref: String) {
        editor.putString(WIND_SPEED_PREFERENCES, windSpeedPref).apply()
    }

    fun getWindSpeedPref(): String? {
        return sharedPreferences.getString(WIND_SPEED_PREFERENCES, METER_PER_SECOND)
    }

    companion object {

        const val METER_PER_SECOND = "METER_PER_SECOND"
        const val MILE_PER_HOUR = "MILE_PER_HOUR"

        const val CELSIUS = "CELSIUS"
        const val KELVIN = "KELVIN"
        const val FAHRENHEIT = "FAHRENHEIT"

        private lateinit var instance: SettingSharedPreferences

        fun getInstance(application: Context): SettingSharedPreferences {
            if (!::instance.isInitialized) {
                instance = SettingSharedPreferences(application.applicationContext)
            }
            return instance
        }
    }


}

