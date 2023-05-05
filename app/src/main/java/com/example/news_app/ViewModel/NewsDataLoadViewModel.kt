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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NewsDataLoadViewModel : ViewModel() {
    private val _list: MutableLiveData<List<Article>> = MutableLiveData(listOf())
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    private val _isRefreshing = MutableStateFlow(false)
    private val api: ApiData = Retrofit.api
    val articleList: LiveData<List<Article>> = _list
    val isLoading: LiveData<Boolean> = _isLoading
    val isRefreshing = _isRefreshing.asStateFlow()
    fun loadSearchData(query: String) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            _list.postValue(
                mapToArticles(api.getSearchQuery("us", query, "5189442ea3fd472b94ba50c569f42552"))
                    ?: listOf()
            )
            _isLoading.postValue(false)
        }
    }
    fun loadNews() {
        _isLoading.postValue(true)
        viewModelScope.launch {
            _list.postValue(
                mapToArticles(api.fetchNews("us", "5189442ea3fd472b94ba50c569f42552"))
                    ?: listOf()
            )
            _isLoading.postValue(false)
        }
    }

    fun loadFilterData(filter: String) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            _list.postValue(
                mapToArticles(api.fetchNews("us", filter, "5189442ea3fd472b94ba50c569f42552"))
                    ?: listOf()
            )
            _isLoading.postValue(false)
        }
    }


    private fun mapToArticles(news: Response<NewsResponses>): List<Article>? {
        return news.body()?.articles
    }
}