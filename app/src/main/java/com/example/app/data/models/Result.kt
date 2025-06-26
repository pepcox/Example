package com.example.app.data.models

import android.os.Parcelable
import kotlinx.collections.immutable.ImmutableCollection
import kotlinx.collections.immutable.ImmutableList
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Result(
    @SerialName("title") val title: String,
    @SerialName("items") val items: List<Item>,
) : Parcelable