package com.example.login.data.network.sesion

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(@ApplicationContext context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val TOKEN_KEY = "auth_token"
    }

    fun saveAuthToken(token: String) {
        prefs.edit().putString(TOKEN_KEY, token).apply()
    }

    fun getAuthToken(): String? {
        return prefs.getString(TOKEN_KEY, null)
    }

    fun clearAuthToken() {
        prefs.edit().remove(TOKEN_KEY).apply()
    }
}
