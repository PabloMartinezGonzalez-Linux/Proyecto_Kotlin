package com.example.login.ui.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.login.domain.models.CardItem
import com.example.login.domain.usecases.AddCardUseCase
import com.example.login.domain.usecases.DeleteCardUseCase
import com.example.login.domain.usecases.GetCardsUseCase
import com.example.login.domain.usecases.UpdateCardUseCase
import com.example.login.domain.usecases.ConvertImageToBase64UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CardViewModel @Inject constructor(
    private val getCardsUseCase: GetCardsUseCase,
    private val addCardUseCase: AddCardUseCase,
    private val deleteCardUseCase: DeleteCardUseCase,
    private val updateCardUseCase: UpdateCardUseCase,
    private val convertImageToBase64UseCase: ConvertImageToBase64UseCase
) : ViewModel() {

    private val _cards = MutableLiveData<List<CardItem>>()
    val cards: LiveData<List<CardItem>> get() = _cards

    private val _imageBase64 = MutableLiveData<String>()
    val imageBase64: LiveData<String> get() = _imageBase64

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

    fun updateCard(card: CardItem) {
        updateCardUseCase.execute(card)
        loadCards()
    }

    fun processCapturedImage(bitmap: Bitmap) {
        val base64 = convertImageToBase64UseCase.execute(bitmap)
        _imageBase64.value = base64
        Log.d("CardViewModel", "Imagen convertida a Base64: $base64")
    }

}
