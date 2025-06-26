package com.example.app.com.example.app.data.models

import android.os.Parcelable
import com.example.app.data.models.ErrorData
import com.example.app.data.models.Item
import kotlinx.collections.immutable.ImmutableList
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class ViewState(
    val query: String = "",
    val isLoading: Boolean = false,
    val errorData: ErrorData? = null,
    val items: ImmutableList<Item>? = null
) : Parcelable