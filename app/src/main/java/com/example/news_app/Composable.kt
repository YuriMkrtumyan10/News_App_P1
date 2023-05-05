package com.example.news_app

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.news_app.Domain.Article

@Composable
fun DisplayArticle(article: Article,  onClick: (Article) -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable { onClick(article) }
    ) {
        Column {
            AsyncImage(
                model = article.urlToImage,
                contentDescription = article.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
            )
            article.title?.let {
                Text(
                    text = it,
                    modifier = Modifier.padding(16.dp)
                )
            }
            if (!article.title.isNullOrEmpty()) {
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun FilterButton(navController: NavController) {
    IconButton(onClick = { navController.navigate("filter") }) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Filter",
            modifier = Modifier
                .padding(10.dp),

            )
    }
}
@Composable
fun ButtonFilter(navController: NavController, category: String, onFilter: (String) -> Unit) {
    Button(
        onClick = {
            navController.popBackStack().also { onFilter(category) }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = category, style = MaterialTheme.typography.h5)
    }
}
@Composable
fun ListOfArticles(list: List<Article>) {
    LazyColumn {
        items(list) {
            DisplayArticle(article = it, onClick = {})
        }
    }
}
@Composable
fun Indicate_Progress() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
        )
    }

}

@Composable
fun DisplayArticleDetails(article: Article, navController: NavController) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color.DarkGray)
        ) {
            Button(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                article.author?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                article.source?.let {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            AsyncImage(
                model = article.urlToImage,
                contentDescription = article.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(CircleShape)

            )

            Spacer(modifier = Modifier.height(16.dp))

            article.title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.h6
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            article.content?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}

@Composable
fun SearchBar(
    onSearch: (String) -> Unit,
    onClear: () -> Unit,
    suggestions: List<String>
) {
    var query by remember { mutableStateOf("") }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
    ) {
        IconButton(
            onClick = { onSearch(query) },
            content = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search",
                    tint = if (query.isEmpty()) Color.Gray else Color.Black
                )
            },
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        TextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("Search") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(onSearch = { onSearch(query) }),
            leadingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            query = ""
                            onClear()
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Clear search query",
                                tint = Color.Gray
                            )
                        },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            },
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                cursorColor = Color.Black,
                textColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )

        if (query.isNotEmpty() && suggestions.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 100.dp)
                    .background(Color.White)
                    .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
                    .padding(8.dp)
            ) {
                LazyColumn {
                    items(suggestions.filter { it.contains(query, ignoreCase = true) }) { suggestion ->
                        Text(
                            text = suggestion,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

