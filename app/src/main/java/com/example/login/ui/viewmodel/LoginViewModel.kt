package com.example.login.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.login.data.network.TokenManager
import com.example.login.domain.usecases.LoginUseCase
import com.example.login.domain.usecases.RegisterUseCase
import com.example.login.domain.usecases.ResetPasswordUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asLiveData()

    fun login(username: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = loginUseCase.execute(username, password)
            result.fold(
                onSuccess = { token ->
                    TokenManager.saveToken(token) // ✅ Guarda el token correctamente
                    _authState.value = AuthState.Success(token)
                },
                onFailure = { error ->
                    _authState.value = AuthState.Error(error.message ?: "Error desconocido")
                }
            )
        }
    }

    fun register(username: String, email: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = registerUseCase.execute(username, email, password)
            result.fold(
                onSuccess = { _authState.value = AuthState.Success("Registro exitoso") },
                onFailure = { _authState.value = AuthState.Error(it.message ?: "Error desconocido") }
            )
        }
    }

    fun resetPassword(email: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = resetPasswordUseCase.execute(email)
            result.fold(
                onSuccess = { _authState.value = AuthState.Success("Correo enviado") },
                onFailure = { _authState.value = AuthState.Error(it.message ?: "Error desconocido") }
            )
        }
    }
}
