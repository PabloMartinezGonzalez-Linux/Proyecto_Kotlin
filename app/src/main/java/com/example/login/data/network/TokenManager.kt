package com.example.login.data.network

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private const val PREFS_NAME = "my_prefs"
    private const val KEY_TOKEN = "token"
    private var preferences: SharedPreferences? = null

    // Inicializa TokenManager (llamar desde Application o MainActivity)
    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // Guarda el token en SharedPreferences
    fun saveToken(token: String) {
        preferences?.edit()?.putString(KEY_TOKEN, token)?.apply()
    }

    // Recupera el token guardado
    fun getToken(): String? {
        return preferences?.getString(KEY_TOKEN, null)
    }
}
