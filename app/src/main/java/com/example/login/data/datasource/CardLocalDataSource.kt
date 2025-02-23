package com.example.login.data.datasource

import android.content.Context
import android.content.SharedPreferences
import com.example.login.domain.models.CardItem
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardLocalDataSource @Inject constructor(
    @ApplicationContext context: Context
) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("CardsPrefs", Context.MODE_PRIVATE)

    fun getCards(): List<CardItem> {
        val json = sharedPreferences.getString("cards", "[]")
        val type = object : TypeToken<List<CardItem>>() {}.type
        return Gson().fromJson(json, type) ?: emptyList()
    }

    fun saveCards(cards: List<CardItem>) {
        val json = Gson().toJson(cards)
        sharedPreferences.edit().putString("cards", json).apply()
    }
}
