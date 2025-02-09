package com.example.login.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.login.data.repository.FirebaseAuthRepositoryImpl
import com.example.login.domain.usecases.LoginUseCase
import com.example.login.domain.usecases.RegisterUseCase
import com.example.login.domain.usecases.ResetPasswordUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Representa los diferentes estados de autenticación
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = FirebaseAuthRepositoryImpl()
    private val loginUseCase = LoginUseCase(authRepository)
    private val registerUseCase = RegisterUseCase(authRepository)
    private val resetPasswordUseCase = ResetPasswordUseCase(authRepository)

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asLiveData()

    fun login(email: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = loginUseCase.execute(email, password)
            _authState.value = if (result.isSuccess) AuthState.Success
            else AuthState.Error(result.exceptionOrNull()?.message ?: "Error desconocido")
        }
    }

    fun register(email: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = registerUseCase.execute(email, password)
            _authState.value = if (result.isSuccess) AuthState.Success
            else AuthState.Error(result.exceptionOrNull()?.message ?: "Error desconocido")
        }
    }

    fun resetPassword(email: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = resetPasswordUseCase.execute(email)
            _authState.value = if (result.isSuccess) AuthState.Success
            else AuthState.Error(result.exceptionOrNull()?.message ?: "Error desconocido")
        }
    }
}
