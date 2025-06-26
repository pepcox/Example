package com.example.app.domain.repository

import com.example.app.data.models.Result

interface DataFeedRepository {

    suspend fun getFeedData(
        tags: String,
        onSuccess: (Result) -> Unit,
        onError: (Throwable) -> Unit
    )

}