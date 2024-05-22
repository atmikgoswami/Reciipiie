package com.example.reciipiie.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.reciipiie.data.Recipe
import com.example.reciipiie.data.items1
import com.example.reciipiie.viewModels.RecipeViewModel

@Composable
fun FavouritesScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    recipeViewModel:RecipeViewModel = viewModel(),
    navigateToDetail: (Recipe) -> Unit

) {
    var selectedItemIndex1 by rememberSaveable {
        mutableStateOf(1)
    }

    val viewState by recipeViewModel.favoriteRecipes.observeAsState(RecipeViewModel.RecipeState())

    println("FavouritesScreen viewState: ${viewState.list.size}")
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
                    FavouritesList(categories = viewState.list,navigateToDetail)
                }
            }
        }
    }
}

@Composable
fun FavouritesList(
    categories: List<Recipe>,
    navigateToDetail: (Recipe) -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Favourite Recipes",
                color = androidx.compose.ui.graphics.Color.Black,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                ),
                modifier = Modifier.weight(1f))

        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 54.dp, bottom = 8.dp)
        ) {
            items(categories) { category ->
                FavouriteItem(category = category, navigateToDetail)
            }
        }
    }
}

@Composable
fun FavouriteItem(category: Recipe, navigateToDetail: (Recipe) -> Unit) {
    val customGreyColor = Color(android.graphics.Color.parseColor("#ececec"))
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(2.dp, customGreyColor, RoundedCornerShape(20.dp))
            .clickable { navigateToDetail(category) },
        verticalAlignment = Alignment.CenterVertically
    ){
        Image(
            painter = rememberAsyncImagePainter(category.image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight()
                .width(150.dp)
                .height(100.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = category.title,
                color = Color.Black,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Ready in ${category.readyInMinutes} mins",
                color = Color.Gray,
                style = TextStyle(
                    fontSize = 14.sp
                )
            )
        }
    }
}
