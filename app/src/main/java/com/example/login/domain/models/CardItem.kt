package com.example.login.domain.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardItem(
    var imageRes: Int?,
    var brand: String,
    var model: String,
    var imageUri: Uri? = null,
    var bgImageRes: Int = 0
) : Parcelable
