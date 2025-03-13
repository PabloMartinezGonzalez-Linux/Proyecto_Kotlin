package com.example.login.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.login.data.network.sesion.SessionManager
import com.example.login.domain.usecases.LoginUseCase
import com.example.login.domain.usecases.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asLiveData()

    fun login(username: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            Log.d("LoginViewModel", "Iniciando login con usuario: $username")
            val result = loginUseCase.execute(username, password)
            result.fold(
                onSuccess = { token ->
                    Log.d("LoginViewModel", "Login exitoso, token recibido: $token")
                    sessionManager.saveAuthToken(token)
                    _authState.value = AuthState.Success(token)
                },
                onFailure = { error ->
                    Log.e("LoginViewModel", "Error en login: ${error.message}")
                    _authState.value = AuthState.Error(error.message ?: "Error desconocido")
                }
            )
        }
    }

    fun getToken(): String? {
        return sessionManager.getAuthToken()
    }

    fun register(username: String, email: String, password: String) {
        Log.d("LoginViewModel", "Registro iniciado con: username=$username, email=$email")
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = registerUseCase.execute(username, email, password)

            result.fold(
                onSuccess = {
                    Log.d("LoginViewModel", "Registro exitoso")
                    _authState.value = AuthState.Success("Registro exitoso")
                },
                onFailure = { error ->
                    Log.e("LoginViewModel", "Error en registro: ${error.message}")
                    _authState.value = AuthState.Error(error.message ?: "Error desconocido")
                }
            )
        }
    }


}
