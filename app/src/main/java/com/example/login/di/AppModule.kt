package com.example.login.di

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.login.R
import com.example.login.data.network.interceptor.ApiClient
import com.example.login.data.network.services.AuthService
import com.example.login.data.network.services.CardService
import com.example.login.data.repository.AuthRepositoryImpl
import com.example.login.data.repository.CardRepositoryImpl
import com.example.login.domain.repository.AuthRepository
import com.example.login.domain.repository.CardRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/** ✅ Módulo para proporcionar Retrofit y AuthService */

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = ApiClient.retrofit

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideCardService(retrofit: Retrofit): CardService {
        return retrofit.create(CardService::class.java)
    }
}

/** ✅ Módulo para inyectar repositorios con Hilt */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCardRepository(
        impl: CardRepositoryImpl
    ): CardRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository
}


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
