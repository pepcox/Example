package com.example.app.data.models

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class ErrorData(
    @StringRes val title: Int,
    @StringRes val message: Int,
) : Parcelable