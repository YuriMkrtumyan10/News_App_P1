package com.example.news_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.news_app.ViewModel.NewsDataLoadViewModel

class MainActivity : ComponentActivity() {
    private val newsLoader: NewsDataLoadViewModel by viewModels()
    val suggestions = listOf(" ")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            setContent {
                newsLoader.loadNews()
                Column {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        SearchBar(onSearch = { s -> Unit },  onClear = {}, suggestions = suggestions)
                    }
                    ListOfArticles(list = newsLoader.articleList.observeAsState().value!!)
                }

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