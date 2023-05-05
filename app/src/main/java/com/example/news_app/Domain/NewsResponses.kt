package com.example.news_app.Domain

import com.google.gson.annotations.SerializedName

data class NewsResponses(
    @SerializedName("status")
    val status: String?,
    @SerializedName("totalResults")
    val totalResults: Int?,
    @SerializedName("articles")
    val articles: List<Article>
)

class Article (
    @SerializedName("source")
    val source: Source?,
    val author: String,
    val title: String,
    val urlToImage: String,
    @SerializedName("content")
    val content: String?,
)

data class Source(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)