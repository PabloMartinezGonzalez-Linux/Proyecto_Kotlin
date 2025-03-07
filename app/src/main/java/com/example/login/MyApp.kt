package com.example.login

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import com.example.login.data.network.TokenManager

@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Inicializa el TokenManager con el contexto de la aplicación
        TokenManager.init(this)
    }
}
