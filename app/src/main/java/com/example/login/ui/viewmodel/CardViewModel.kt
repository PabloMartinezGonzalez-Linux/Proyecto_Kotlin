package com.example.login.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.login.domain.models.CardItem
import com.example.login.domain.models.CardRequest
import com.example.login.domain.usecases.AddCardUseCase
import com.example.login.domain.usecases.DeleteCardUseCase
import com.example.login.domain.usecases.GetCardsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardViewModel @Inject constructor(
    private val getCardsUseCase: GetCardsUseCase,
    private val addCardUseCase: AddCardUseCase,
    private val deleteCardUseCase: DeleteCardUseCase
) : ViewModel() {

    private val _cards = MutableStateFlow<List<CardItem>>(emptyList())
    val cards: StateFlow<List<CardItem>> get() = _cards

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    fun fetchCards() {
        viewModelScope.launch {
            val result = getCardsUseCase.execute()
            result.fold(
                onSuccess = { _cards.value = it },
                onFailure = { _errorMessage.value = it.message }
            )
        }
    }

    fun addCard(newCard: CardRequest) {
        viewModelScope.launch {
            val result = addCardUseCase.execute(newCard)
            result.fold(
                onSuccess = {
                    fetchCards()
                },
                onFailure = {
                    _errorMessage.value = it.message
                }
            )
        }
    }

    fun deleteCard(card: CardItem) {
        card.id?.let { cardId ->
            viewModelScope.launch {
                val result = deleteCardUseCase.execute(cardId)
                result.fold(
                    onSuccess = {
                        fetchCards()
                    },
                    onFailure = { _errorMessage.value = it.message }
                )
            }
        } ?: run {
            _errorMessage.value = "Error: La tarjeta no tiene un ID válido."
        }
    }



}
