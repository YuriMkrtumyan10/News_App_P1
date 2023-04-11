package com.example.news_app.Data

import com.example.news_app.Domain.NewsResponses
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiData {

    @GET("top-headlines")
    suspend fun fetchNews(
        @Query("country") country:String,
        @Query("apiKey") key:String): Response<NewsResponses>


}
