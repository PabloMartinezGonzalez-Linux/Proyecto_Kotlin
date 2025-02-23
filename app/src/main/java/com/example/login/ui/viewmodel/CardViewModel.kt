package com.example.login.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.login.domain.models.CardItem
import com.example.login.domain.usecases.AddCardUseCase
import com.example.login.domain.usecases.DeleteCardUseCase
import com.example.login.domain.usecases.GetCardsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CardViewModel @Inject constructor(
    private val getCardsUseCase: GetCardsUseCase,
    private val addCardUseCase: AddCardUseCase,
    private val deleteCardUseCase: DeleteCardUseCase
) : ViewModel() {

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
        loadCards()
    }

    fun deleteCard(card: CardItem) {
        deleteCardUseCase.execute(card)
        loadCards()
    }
}
