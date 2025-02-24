package com.example.login.domain.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardItem(
    var imageRes: Int?,
    var brand: String,
    var model: String,
    var bgImageRes: Int = 0,
    var imageBase64: String? = null
) : Parcelable
