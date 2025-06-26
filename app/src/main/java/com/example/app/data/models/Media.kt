package com.example.app.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Media(
    @SerialName("m") val imageUrl: String,
) : Parcelable