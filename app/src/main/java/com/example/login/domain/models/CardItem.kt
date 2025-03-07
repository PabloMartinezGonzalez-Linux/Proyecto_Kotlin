package com.example.login.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardItem(
    val id: Int? = null,  // ID opcional para backend
    val imageRes: Int? = null,
    val brand: String,
    val model: String,
    val bgImageRes: Int = 0,
    val imageBase64: String? = null
) : Parcelable
