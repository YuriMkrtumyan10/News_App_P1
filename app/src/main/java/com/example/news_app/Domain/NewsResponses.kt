package com.example.news_app.Domain

data class NewsResponses (
    val status: String?,
    val totalResults: Int?,
    val articles: List<Article>
)

class Article (
    val author: String,
    val title: String,
    val urlToImage: String
)