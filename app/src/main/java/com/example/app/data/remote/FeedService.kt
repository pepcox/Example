package com.example.app.data.remote

import com.example.app.data.models.Result
import retrofit2.http.GET
import retrofit2.http.Query


interface FeedService {

    @Throws(Throwable::class)
    @GET("feeds/photos_public.gne?format=json&nojsoncallback=1")
    suspend fun getFeed(@Query("tags") tags: String) : Result

}