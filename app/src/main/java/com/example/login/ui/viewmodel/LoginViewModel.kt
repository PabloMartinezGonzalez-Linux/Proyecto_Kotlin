package com.example.login.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.login.domain.usecases.LoginUseCase
import com.example.login.domain.usecases.RegisterUseCase
import com.example.login.domain.usecases.ResetPasswordUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {

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
