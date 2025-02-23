package com.example.login.domain.usecases

import com.example.login.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun execute(email: String, password: String) = authRepository.login(email, password)
}
