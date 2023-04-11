package com.example.news_app.ViewModel


import com.example.news_app.Data.ApiData
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.news_app.Data.Retrofit
import androidx.lifecycle.viewModelScope
import com.example.news_app.Domain.Article
import com.example.news_app.Domain.NewsResponses
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsDataLoadViewModel: ViewModel() {
    private val list: MutableLiveData<List<Article>> = MutableLiveData(listOf())
    private val api: ApiData = Retrofit.api
    val articleList: LiveData<List<Article>> = list

    fun loadNews() {
        viewModelScope.launch {
            list.postValue(api.fetchNews("us", "5189442ea3fd472b94ba50c569f42552")
                .let{ mapToArticles(it) })
        }
    }

    private fun mapToArticles(news: Response<NewsResponses>): List<Article>? {
        return news.body()?.articles

    }
}