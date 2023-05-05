package com.example.news_app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.news_app.ViewModel.NewsDataLoadViewModel
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.news_app.Function.checkForInternet
import com.example.news_app.Function.localSearch
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import androidx.compose.material.*
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.news_app.Domain.Article
import com.google.gson.Gson
import java.net.URLDecoder
import java.nio.charset.StandardCharsets


class MainActivity : ComponentActivity() {
    val suggestions = listOf(" ")
    private val context = this
    private val newsLoader: NewsDataLoadViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val isRefreshing = newsLoader.isRefreshing.collectAsState()
            val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing.value)
            newsLoader.loadNews()
            NavHost(navController = navController, startDestination = "main") {
                composable("main") {
                    SwipeRefresh(state = swipeRefreshState, onRefresh = { newsLoader.loadNews() }) {
                        MainScreen(navController = navController)
                    }
                }
                composable("filter") {
                    FilterScreen(navController = navController)
                }
                composable(route = "articleDetails/{article}",
                    arguments = listOf(
                        navArgument("article") {
                            type = NavType.StringType
                        }
                    )
                )
                {
                    val articleJson =
                        it.arguments?.getString("article")
                            .let {
                                val encodedArticle = it
                                val replacedArticle = encodedArticle?.replace(Regex("%"), "%25")
                                URLDecoder.decode(
                                    replacedArticle,
                                    StandardCharsets.UTF_8.toString()
                                )
                            }
                    val articleObject = Gson().fromJson(articleJson, Article::class.java)
                    DisplayArticleDetails(article = articleObject, navController = navController)
                }
            }
        }
    }


    @Composable
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    fun MainScreen(navController: NavController) {
        Scaffold(topBar = {
            TopAppBar {
                Text(
                    text = "News!",
                    style = MaterialTheme.typography.h5
                )
            }
        }) {
            Column {
                val isLoading = newsLoader.isLoading.observeAsState().value!!
                if (isLoading) {
                    Indicate_Progress()
                }
                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        FilterButton(navController = navController)
                        SearchBar(onSearch = {
                            if (checkForInternet(context)) {
                                newsLoader.loadSearchData(it)
                            } else {
                                localSearch(newsLoader.articleList.value!!, it)
                            }
                        }, suggestions = suggestions,  onClear = {})
                    }
                }
                ListOfArticles(list = newsLoader.articleList.observeAsState().value!!)

            }
        }
    }

    @Composable
    fun FilterScreen(navController: NavController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "Select category", style = MaterialTheme.typography.h5)
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ButtonFilter(
                    navController = navController,
                    category = "Business",
                    onFilter = { newsLoader.loadFilterData(it) })
                ButtonFilter(
                    navController = navController,
                    category = "General",
                    onFilter = { newsLoader.loadFilterData(it) })
                ButtonFilter(
                    navController = navController,
                    category = "Entertainment",
                    onFilter = { newsLoader.loadFilterData(it) })
                ButtonFilter(
                    navController = navController,
                    category = "Health",
                    onFilter = { newsLoader.loadFilterData(it) })
            }
        }
    }
}

//@Composable
//fun Greeting(name: String) {
//    Text(text = "Hello $name!")
//}
//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    News_appTheme {
//        Greeting("Android")
//    }
//}