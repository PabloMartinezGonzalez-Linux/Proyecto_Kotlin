package com.example.login.data.datasource

import android.content.Context
import android.content.SharedPreferences
import com.example.login.domain.models.CardItem
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Singleton
class CardLocalDataSource @Inject constructor(
    @ApplicationContext context: Context
) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("CardsPrefs", Context.MODE_PRIVATE)

    private val jsonParser = Json { ignoreUnknownKeys = true; prettyPrint = true }

    fun getCards(): List<CardItem> {
        val json = sharedPreferences.getString("cards", "[]") ?: "[]"
        return jsonParser.decodeFromString(json)
    }

    fun saveCards(cards: List<CardItem>) {
        val json = jsonParser.encodeToString(cards)
        sharedPreferences.edit().putString("cards", json).apply()
    }
}
