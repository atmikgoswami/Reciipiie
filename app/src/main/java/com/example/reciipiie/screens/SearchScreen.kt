package com.example.reciipiie.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.reciipiie.R
import com.example.reciipiie.Screen
import com.example.reciipiie.data.Recipe
import com.example.reciipiie.viewModels.RecipeViewModel

@Composable
fun SearchScreen(navController: NavController, navigateToDetail: (Recipe) -> Unit) {
    val viewModel: RecipeViewModel = viewModel()
    val searchState by viewModel.searchResults
    val searchText by viewModel.searchText
    val isSearching = searchState.loading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .background(color = colorResource(id = R.color.expandableBoxColor))
            ,
            verticalAlignment = Alignment.CenterVertically,

        ) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
            TextField(
                value = searchText,
                onValueChange = { query ->
                    viewModel.searchRecipes(query)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(15.dp)),
                placeholder = { Text(text = "Search any recipe", style = TextStyle(
                    fontSize = 19.sp
                )
                ) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = colorResource(id = R.color.expandableBoxColor),
                    focusedIndicatorColor =  Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

        }
        Spacer(modifier = Modifier.height(16.dp))
        if (isSearching) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(searchState.results) { result ->
                    Box(modifier = Modifier.fillMaxWidth().padding(10.dp)){
                        Row {
                            androidx.compose.material3.Icon(
                                painter = painterResource(id = R.drawable.img_2),
                                contentDescription = "Search",
                                tint = Color.Black,
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .size(25.dp)
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            // Highlight the searched text in bold
                            val titleParts = result.title.split(searchText, ignoreCase = true)
                            for ((index, part) in titleParts.withIndex()) {
                                Text(
                                    text = part,
                                    style = TextStyle(
                                        fontSize = 21.sp
                                    )
                                )
                                if (index < titleParts.size - 1) {
                                    Text(
                                        text = searchText.capitalizeWords(),
                                        style = TextStyle(
                                            fontSize = 21.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier.clickable {
                                            viewModel.fetchRecipeById(result.id)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

        }

        searchState.selectedRecipe?.let { recipe ->
            LaunchedEffect(recipe) {
                // Navigate to the ItemScreen with the selected recipe
                navigateToDetail(recipe)
                viewModel.clearSelectedRecipe()
            }
        }
    }
}
