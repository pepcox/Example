package com.example.app.di

import com.example.app.data.remote.FeedService
import com.example.app.data.repository.DataFeedRepositoryImpl
import com.example.app.domain.repository.DataFeedRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Converter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    internal fun provideDataFeedRepository(feedService: FeedService): DataFeedRepository {
        return DataFeedRepositoryImpl(feedService)
    }

    @Provides
    @Singleton
    internal fun provideConverterFactory(): Converter.Factory {
        val networkJson = Json { ignoreUnknownKeys = true }
        return networkJson.asConverterFactory("application/json".toMediaType())
    }

}