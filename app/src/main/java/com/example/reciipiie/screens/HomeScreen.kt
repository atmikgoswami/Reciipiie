package com.example.reciipiie.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.reciipiie.Screen
import com.example.reciipiie.data.Recipe
import com.example.reciipiie.data.UserData
import com.example.reciipiie.data.items1
import com.example.reciipiie.viewModels.RecipeViewModel


@Composable
fun Homescreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewState: RecipeViewModel.RecipeState,
    userData: UserData?,
    navigateToDetail: (Recipe) -> Unit
) {
    var selectedItemIndex1 by rememberSaveable {
        mutableStateOf(0)
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                items1.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex1 == index,
                        onClick = {
                            selectedItemIndex1 = index
                            navController.navigate(item.route)
                        },
                        label = {
                            Text(text = item.title)
                        },
                        alwaysShowLabel = true,
                        icon = {
                            Icon(
                                imageVector = if (index == selectedItemIndex1) {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        }
                    )
                }
            }
        }
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
        ) {
            when {
                viewState.loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                viewState.error != null -> {
                    Text(
                        text = "ERROR OCCURRED and it is ${viewState.error}",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    HomescreenList(navController, categories = viewState.list,userData, navigateToDetail)
                }
            }
        }
    }
}

@Composable
fun HomescreenList(
    navController: NavController,
    categories: List<Recipe>,
    userData: UserData?,
    navigateToDetail: (Recipe) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            if (userData != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(start = 8.dp,end=10.dp, top = 16.dp, bottom = 8.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "\uD83D\uDC4B Hey ${userData.username}",
                        color = Color.Black,
                        style = TextStyle(
                            fontSize = 19.sp
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    AsyncImage(
                        model = userData.profilePictureUrl,
                        contentDescription = "Profile picture",
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { navController.navigate("profile") }
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Text(
                text = "Discover healthy and tasty recipes",
                color = Color.Gray,
                style = TextStyle(
                    fontSize = 13.sp
                ),
                modifier = Modifier
                    .padding(start = 18.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(22.dp))
            SearchBar(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable { navController.navigate(Screen.SearchScreen.route) }
            )
            Text(
                text = "Popular Recipes",
                color = Color.Black,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp
                ),
                modifier = Modifier
                    .padding(top = 40.dp, start = 18.dp)
                    .fillMaxWidth()
            )
        }
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 8.dp)
            ) {
                items(categories) { category ->
                    HomescreenItem(category = category, navigateToDetail)
                }
            }
        }
        item {
            Text(
                text = "All Recipes",
                color = Color.Black,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp
                ),
                modifier = Modifier
                    .padding(start = 18.dp, top = 16.dp)
                    .fillMaxWidth()
            )
        }
        items(categories) { category ->
            FavouriteItem(category = category, navigateToDetail)
        }
    }
}

@Composable
fun HomescreenItem(category: Recipe, navigateToDetail: (Recipe) -> Unit) {
    val customGreyColor = Color(android.graphics.Color.parseColor("#ececec"))
    Box(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(2.dp, customGreyColor, RoundedCornerShape(20.dp))
            .clickable { navigateToDetail(category) }
            .width(160.dp)
            .height(160.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(category.image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)) // semi-transparent background
                .padding(12.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column {
                Text(
                    text = category.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = "Ready in ${category.readyInMinutes} mins",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(Color.LightGray, RoundedCornerShape(16.dp))
            .padding(8.dp)
    ) {
        // Your search bar content here
        Icon(
            painter = painterResource(id = androidx.appcompat.R.drawable.abc_ic_search_api_material),
            contentDescription = "Search",
            tint = Color.Black,
            modifier = Modifier.padding(start = 8.dp)
        )
        Text(
            text = "Search any recipe",
            color = Color.Gray,
            style = TextStyle(
                fontSize = 16.sp
            ),
            modifier = Modifier
                .padding(top=2.dp,start = 40.dp)
                .fillMaxWidth()
        )
    }
}
