package com.multi.game.domain.other

import android.content.Context

class AppSharedPrefs(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE)

    fun increaseBalance(value: Int) {
        sharedPreferences.edit().putInt("BALANCE", getBalance() + value).apply()
    }

    fun getBalance(): Int = sharedPreferences.getInt("BALANCE", 0)

    fun setVolume(volume: Int) {
        sharedPreferences.edit().putInt("VOLUME", volume).apply()
    }

    fun getVolume(): Int = sharedPreferences.getInt("VOLUME", 50)

    fun setVibro(value: Boolean) {
        sharedPreferences.edit().putBoolean("VIBRO", value).apply()
    }

    fun getVibro(): Boolean = sharedPreferences.getBoolean("VIBRO", true)

}