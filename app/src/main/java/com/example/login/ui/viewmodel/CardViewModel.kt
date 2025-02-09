package com.example.login.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.login.data.datasource.CardLocalDataSource
import com.example.login.data.repository.CardRepositoryImpl
import com.example.login.domain.models.CardItem
import com.example.login.domain.usecases.AddCardUseCase
import com.example.login.domain.usecases.DeleteCardUseCase
import com.example.login.domain.usecases.GetCardsUseCase

/**
 * ViewModel encargado de gestionar las tarjetas (cards) mediante los casos de uso:
 * - GetCardsUseCase: para obtener la lista de tarjetas.
 * - AddCardUseCase: para agregar una tarjeta.
 * - DeleteCardUseCase: para eliminar una tarjeta.
 *
 * Se utiliza AndroidViewModel para disponer del contexto de la aplicación.
 */

class CardViewModel(application: Application) : AndroidViewModel(application) {

    // Configuración de la fuente de datos, repositorio y casos de uso:
    private val localDataSource = CardLocalDataSource(application)
    private val repository = CardRepositoryImpl(localDataSource)
    private val getCardsUseCase = GetCardsUseCase(repository)
    private val addCardUseCase = AddCardUseCase(repository)
    private val deleteCardUseCase = DeleteCardUseCase(repository)

    // LiveData que contendrá la lista de tarjetas:
    private val _cards = MutableLiveData<List<CardItem>>()
    val cards: LiveData<List<CardItem>> get() = _cards

    init {
        loadCards()
    }

    private fun loadCards() {
        _cards.value = getCardsUseCase.execute()
    }

    fun addCard(card: CardItem) {
        addCardUseCase.execute(card)
        loadCards() // Actualizamos la lista tras agregar la tarjeta.
    }

    fun deleteCard(card: CardItem) {
        deleteCardUseCase.execute(card)
        loadCards() // Actualizamos la lista tras eliminar la tarjeta.
    }
}
