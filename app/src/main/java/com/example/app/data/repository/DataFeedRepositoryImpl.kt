package com.example.app.data.repository

import com.example.app.data.models.Result
import com.example.app.data.remote.FeedService
import com.example.app.domain.repository.DataFeedRepository
import kotlinx.coroutines.CancellationException

class DataFeedRepositoryImpl(
    private val feedService: FeedService
) : DataFeedRepository {

    override suspend fun getFeedData(
        tags: String,
        onSuccess: (Result) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        try {
            onSuccess.invoke(feedService.getFeed(tags))
        } catch (e: Throwable) {
            e.printStackTrace()
            onError.invoke(e)
            if (e is CancellationException) throw e
        }
    }
}