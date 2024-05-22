package com.example.reciipiie.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reciipiie.R
import com.example.reciipiie.data.Recipe
import com.example.reciipiie.viewModels.RecipeViewModel
import org.jsoup.Jsoup

@Composable
fun RecipeScreen(navController: NavController, category: Recipe) {
    val customGreyColor = Color(android.graphics.Color.parseColor("#ececec"))

    var nutritionExpanded by remember { mutableStateOf(false) }
    var goodForHealthExpanded by remember { mutableStateOf(false) }
    var badForHealthExpanded by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(category.favourite) }


    val viewModel: RecipeViewModel = viewModel()

    val toggleFavorite: () -> Unit = {
        isFavorite = !isFavorite
        if (isFavorite) {
            // Add the recipe to favorites
            category.favourite = true
            viewModel.addFavoriteRecipe(category)
        } else {
            // Remove the recipe from favorites
            category.favourite = false
            viewModel.removeFavoriteRecipe(category)
        }
    }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 8.dp)
    )
    {
        item {
            Box(modifier = Modifier
                .fillMaxWidth()
                .width(600.dp)
                .height(274.dp)){
                Image(
                    painter = rememberAsyncImagePainter(category.image),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()

                )
                IconButton(
                    onClick = { toggleFavorite() },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorite"
                    )
                }
            }
            Box(modifier = Modifier.padding(start=15.dp, top=10.dp), contentAlignment = Alignment.Center){
                Text(
                    text = category.title,
                    color = androidx.compose.ui.graphics.Color.Black,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(modifier = Modifier.fillMaxWidth()){
                Row {
                    Spacer(modifier = Modifier.width(15.dp));
                    Box(modifier = Modifier
                        .width(110.dp)
                        .height(80.dp)
                        .border(2.dp, customGreyColor, RoundedCornerShape(20.dp)),
                        contentAlignment = Alignment.Center
                    ){
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Ready in",color=colorResource(id = R.color.neutralGray))
                            Text(text = "${category.readyInMinutes} mins", color = colorResource(id = R.color.signInOrange),fontWeight = FontWeight.Bold,)
                        }
                    }
                    Spacer(modifier = Modifier.width(20.dp));
                    Box(modifier = Modifier
                        .width(110.dp)
                        .height(80.dp)
                        .border(2.dp, customGreyColor, RoundedCornerShape(20.dp)),
                        contentAlignment = Alignment.Center
                    ){
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Servings",color=colorResource(id = R.color.neutralGray))
                            Text(text = "${category.servings}", color = colorResource(id = R.color.signInOrange),fontWeight = FontWeight.Bold,)
                        }
                    }
                    Spacer(modifier = Modifier.width(20.dp));
                    Box(modifier = Modifier
                        .width(120.dp)
                        .height(80.dp)
                        .border(2.dp, customGreyColor, RoundedCornerShape(20.dp)),
                        contentAlignment = Alignment.Center
                    ){
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Price/Serving",color=colorResource(id = R.color.neutralGray))
                            Text(text = "${category.pricePerServing}", color = colorResource(id = R.color.signInOrange),fontWeight = FontWeight.Bold,)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, top = 8.dp, bottom = 8.dp, end = 10.dp)) {
                Column {
                    Text("Ingredients", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow {
                        val seenIngredients = mutableSetOf<Recipe.Instruction.Step.Ingredient>()
                        items(category.analyzedInstructions) { instruction ->
                            instruction.steps.forEach { step ->
                                if (step.ingredients.isNotEmpty()) {
                                    step.ingredients.forEach { ingredient ->
                                        if (seenIngredients.add(ingredient)) {
                                            IngredientItem(ingredient)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, top = 8.dp, bottom = 8.dp, end = 10.dp)) {
                Column {
                    Text("Instructions", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = parseHtml(category.instructions), color=colorResource(id = R.color.neutralGray))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, top = 8.dp, bottom = 8.dp, end = 10.dp)) {
                Column {
                    Text("Equipments", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow {
                        val seenEquipments = mutableSetOf<Recipe.Instruction.Step.Equipment>()
                        items(category.analyzedInstructions) { instruction ->
                            instruction.steps.forEach { step ->
                                if (step.equipment.isNotEmpty()) {
                                    step.equipment.forEach { equipment ->
                                        if (seenEquipments.add(equipment)) {
                                            EquipmentItem(equipment)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, top = 8.dp, bottom = 8.dp, end = 10.dp)) {
                Column {
                    Text("Quick Summary", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = parseHtml(category.summary), color=colorResource(id = R.color.neutralGray))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            ExpandableSection(
                title = "Nutrition",
                expanded = nutritionExpanded,
                onToggle = { nutritionExpanded = !nutritionExpanded }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp,end = 10.dp)
                        .background(colorResource(id = R.color.expandableBoxColor))
                        .height(50.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Text("Nutrition details here...", modifier = Modifier.padding(start=2.dp))
                }
            }

            Spacer(modifier = Modifier.height(2.dp))
            ExpandableSection(
                title = "Bad for Health Nutrition",
                expanded = badForHealthExpanded,
                onToggle = { badForHealthExpanded = !badForHealthExpanded }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 10.dp)
                        .background(colorResource(id = R.color.expandableBoxColor))
                        .height(50.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Text("Nutrition details here...")
                }
            }

            Spacer(modifier = Modifier.height(2.dp))
            ExpandableSection(
                title = "Good for Health Nutrition",
                expanded = goodForHealthExpanded,
                onToggle = { goodForHealthExpanded = !goodForHealthExpanded }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 10.dp)
                        .background(colorResource(id = R.color.expandableBoxColor))
                        .height(50.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Text("Nutrition details here...")
                }

            }
        }
    }
}

@Composable
fun EquipmentItem(equipments: Recipe.Instruction.Step.Equipment){
    val customGreyColor = Color(android.graphics.Color.parseColor("#ececec"))
    Box(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(20.dp))
            .width(160.dp)
            .height(160.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = rememberAsyncImagePainter(equipments.image),
                contentDescription = null,
                //contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .border(2.dp, customGreyColor, CircleShape)
            )
            Box(modifier = Modifier.padding(4.dp)){
                Text(
                    text = equipments.localizedName.capitalizeWords(),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun IngredientItem(ingredients: Recipe.Instruction.Step.Ingredient,) {
    val customGreyColor = Color(android.graphics.Color.parseColor("#ececec"))
    Box(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(20.dp))
            .width(160.dp)
            .height(160.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = rememberAsyncImagePainter(ingredients.image),
                contentDescription = null,
                //contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .border(2.dp, customGreyColor, CircleShape)
            )
            Box(modifier = Modifier.padding(4.dp)){
                Text(
                    text = ingredients.localizedName.capitalizeWords(),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun ExpandableSection(
    title: String,
    expanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle() }
                .padding(start = 15.dp, top = 8.dp, end = 10.dp)
                .background(colorResource(id = R.color.expandableBoxColor))
                .height(50.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 20.sp,modifier = Modifier.padding(start=4.dp))
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = null
            )
        }
        if (expanded) {

            content()
        }
    }
}

fun parseHtml(description: String): String {
    return Jsoup.parse(description).text()
}

fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it.capitalize() }

