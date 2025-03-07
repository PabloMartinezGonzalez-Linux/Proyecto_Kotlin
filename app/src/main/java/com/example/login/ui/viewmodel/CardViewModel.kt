package com.example.login.ui.viewmodel

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.login.data.models.CardRequest
import com.example.login.domain.models.CardItem
import com.example.login.domain.repository.CardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardViewModel @Inject constructor(
    private val cardRepository: CardRepository
) : ViewModel() {

    private val _cards = MutableLiveData<List<CardItem>>() // 🔥 Ahora usa CardItem
    val cards: LiveData<List<CardItem>> get() = _cards

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _imageBase64 = MutableLiveData<String>()
    val imageBase64: LiveData<String> get() = _imageBase64

    init {
        fetchCards() // 🔄 Cargar las tarjetas desde el backend al iniciar
    }

    /** 📌 Obtener todas las tarjetas desde el backend */
    fun fetchCards() {
        viewModelScope.launch {
            val result = cardRepository.getCards()
            result.fold(
                onSuccess = { fetchedCards ->
                    Log.d("CardViewModel", "Cantidad de tarjetas recibidas: ${fetchedCards.size}")
                    _cards.value = fetchedCards.map { it.toCardItem() }
                },
                onFailure = { throwable ->
                    _error.value = "Error al obtener tarjetas: ${throwable.message}"
                    Log.e("CardViewModel", "Error al obtener tarjetas", throwable)
                }
            )
        }
    }



    /** 📌 Crear una nueva tarjeta */
    fun addCard(cardRequest: CardRequest) {
        Log.d("CardViewModel", "Enviando CardRequest: $cardRequest") // 🔥 Agregamos un log para depurar
        viewModelScope.launch {
            val result = cardRepository.createCard(cardRequest)
            result.fold(
                onSuccess = {
                    fetchCards() // 🔄 Recargar la lista tras añadir una tarjeta
                },
                onFailure = { throwable ->
                    _error.value = "Error al crear tarjeta: ${throwable.message}"
                    Log.e("CardViewModel", "Error al crear tarjeta", throwable)
                }
            )
        }
    }

    /** 📌 Actualizar una tarjeta existente */
    fun updateCard(id: Int, cardRequest: CardRequest) {
        viewModelScope.launch {
            val result = cardRepository.updateCard(id, cardRequest)
            result.fold(
                onSuccess = {
                    fetchCards() // 🔄 Recargar la lista tras actualizar una tarjeta
                },
                onFailure = { throwable ->
                    _error.value = "Error al actualizar tarjeta: ${throwable.message}"
                    Log.e("CardViewModel", "Error al actualizar tarjeta", throwable)
                }
            )
        }
    }

    /** 📌 Eliminar una tarjeta */
    fun deleteCard(id: Int) {
        viewModelScope.launch {
            val result = cardRepository.deleteCard(id)
            result.fold(
                onSuccess = {
                    fetchCards() // 🔄 Recargar la lista tras eliminar una tarjeta
                },
                onFailure = { throwable ->
                    _error.value = "Error al eliminar tarjeta: ${throwable.message}"
                    Log.e("CardViewModel", "Error al eliminar tarjeta", throwable)
                }
            )
        }
    }

    fun processCapturedImage(bitmap: Bitmap) {
        viewModelScope.launch {
            val base64Image = encodeToBase64(bitmap)
            _imageBase64.postValue(base64Image) // 🔥 Guarda la imagen en LiveData
        }
    }

    /** Convierte un Bitmap en una cadena Base64 */
    private fun encodeToBase64(bitmap: Bitmap): String {
        val outputStream = java.io.ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }

}
