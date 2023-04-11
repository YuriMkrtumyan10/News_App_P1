package com.example.news_app.Data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://newsapi.org/"
const val VERSION = "v2"

object Retrofit {

    val api: ApiData by lazy {
        Retrofit.Builder()
            .baseUrl("$BASE_URL$VERSION/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiData::class.java)
    }

}