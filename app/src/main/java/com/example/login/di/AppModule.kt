package com.example.login.di

import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.login.R
import com.example.login.data.network.services.AuthService
import com.example.login.data.network.services.CardService
import com.example.login.data.network.sesion.CardRepositoryImpl
import com.example.login.data.network.sesion.SessionManager
import com.example.login.data.repository.AuthRepositoryImpl
import com.example.login.domain.repository.AuthRepository
import com.example.login.domain.repository.CardRepository
import com.example.login.domain.usecases.AddCardUseCase
import com.example.login.domain.usecases.DeleteCardUseCase
import com.example.login.domain.usecases.GetCardsUseCase
import com.example.login.domain.usecases.UpdateCardUseCase
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSessionManager(application: Application): SessionManager {
        return SessionManager(application)
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(sessionManager: SessionManager): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()

                if (request.url.encodedPath.endsWith("/login") || request.url.encodedPath.endsWith("/register")) {
                    return@addInterceptor chain.proceed(request)
                }

                val token = sessionManager.getAuthToken()
                val newRequest = request.newBuilder()
                if (!token.isNullOrEmpty()) {
                    newRequest.addHeader("Authorization", "Bearer $token")
                }
                chain.proceed(newRequest.build())
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
        }

        return Retrofit.Builder()
            .baseUrl("http://192.168.1.33:8080")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(client)
            .build()
    }

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

    @Provides
    @Singleton
    fun provideCardRepository(cardService: CardService): CardRepository {
        return CardRepositoryImpl(cardService)
    }

    @Provides
    @Singleton
    fun provideGetCardsUseCase(cardRepository: CardRepository): GetCardsUseCase {
        return GetCardsUseCase(cardRepository)
    }

    @Provides
    @Singleton
    fun provideAddCardUseCase(cardRepository: CardRepository): AddCardUseCase {
        return AddCardUseCase(cardRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteCardUseCase(cardRepository: CardRepository): DeleteCardUseCase {
        return DeleteCardUseCase(cardRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateCardUseCase(cardRepository: CardRepository): UpdateCardUseCase {
        return UpdateCardUseCase(cardRepository)
    }


}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

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
