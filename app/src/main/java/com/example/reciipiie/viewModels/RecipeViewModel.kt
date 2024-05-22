package com.example.reciipiie.viewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reciipiie.data.Recipe
import com.example.reciipiie.data.SearchResult
import com.example.reciipiie.recipeService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class RecipeViewModel : ViewModel() {

    private val _recipeState = mutableStateOf(RecipeState())
    val recipeState: State<RecipeState> = _recipeState

    private val _searchResults = mutableStateOf(SearchState(loading = false))
    val searchResults: State<SearchState> = _searchResults

    private val _searchText = mutableStateOf("")
    val searchText: State<String> = _searchText

    private val _favoriteRecipes = MutableLiveData(RecipeState())
    val favoriteRecipes: MutableLiveData<RecipeState> get() = _favoriteRecipes


    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    init {
        fetchRandomRecipes()
        retrieveFavoriteRecipes()
    }

    private fun fetchRandomRecipes() {
        viewModelScope.launch {
            try {
                val response = recipeService.getRandomRecipes(number = 10)
                println("Response: $response")
                _recipeState.value = _recipeState.value.copy(
                    list = response.recipes ?: emptyList(),
                    loading = false,
                    error = null
                )
            } catch (e: Exception) {
                println("Error fetching news articles: ${e.message}")
                _recipeState.value = _recipeState.value.copy(
                    loading = false,
                    error = "Error fetching recipes: ${e.message}"
                )
            }
        }
    }

    fun searchRecipes(query: String) {
        _searchText.value = query
        viewModelScope.launch {
            try {
                _searchResults.value = _searchResults.value.copy(loading = true)
                val results = recipeService.searchRecipes(query, number = 10)
                println("Search Results: $results")
                _searchResults.value = _searchResults.value.copy(
                    results = results,
                    loading = false,
                    error = null
                )
            } catch (e: Exception) {
                println("Error searching recipes: ${e.message}")
                _searchResults.value = _searchResults.value.copy(
                    loading = false,
                    error = "Error searching recipes: ${e.message}"
                )
            }
        }
    }

    fun fetchRecipeById(id: Int) {
        viewModelScope.launch {
            try {
                val recipe = recipeService.getRecipeById(id)
                println("Fetched Recipe: $recipe")
                _searchResults.value = _searchResults.value.copy(
                    selectedRecipe = recipe,
                    error = null
                )
            } catch (e: Exception) {
                println("Error fetching recipe by ID: ${e.message}")
                _searchResults.value = _searchResults.value.copy(
                    selectedRecipe = null,
                    error = "Error fetching recipe by ID: ${e.message}"
                )
            }
        }
    }

    fun fetchRecipesInBulk(recipeIds: List<Int>) {
        if (recipeIds.isEmpty()) {
            _favoriteRecipes.value = _favoriteRecipes.value?.copy(
                list = emptyList(),
                loading = false,
                error = "No favorite recipes found."
            )
            return
        }

        viewModelScope.launch {
            try {

                val recipeIdsString = recipeIds.joinToString(",")
                println("Fetching recipes with IDs: $recipeIdsString")

                // Fetch the recipes from the API
                val recipeList = recipeService.getRecipesInBulk(recipeIdsString)

                println("Fetched ${recipeList.size} recipes: $recipeList")

                _favoriteRecipes.value = _favoriteRecipes.value?.copy(
                    list = recipeList ?: emptyList(),
                    loading = false,
                    error = null
                )
                println("after adding in view model: ${favoriteRecipes.value?.list?.size}")
            } catch (e: Exception) {
                println("Error fetching recipes: ${e.message}")
                _favoriteRecipes.value = _favoriteRecipes.value?.copy(
                    loading = false,
                    error = "Error fetching recipes: ${e.message}"
                )
            }
        }
    }

    fun clearSelectedRecipe() {
        _searchResults.value = _searchResults.value.copy(selectedRecipe = null)
    }

    fun addFavoriteRecipe(recipe: Recipe) {
        val currentRecipes = _favoriteRecipes.value?.list?.toMutableList()
        if (currentRecipes != null) {
            currentRecipes.add(recipe)
        }
        _favoriteRecipes.value = currentRecipes?.let { _favoriteRecipes.value?.copy(list = it) }

        auth.currentUser?.uid?.let { userId ->
            db.collection("users").document(userId)
                .collection("favorite_recipes").document(recipe.id.toString())
                .set(recipe)
                .addOnSuccessListener {
                    println("Favorite recipe added to Firestore: $recipe")
                    retrieveFavoriteRecipes()
                }
                .addOnFailureListener { e ->
                    println("Error adding favorite recipe to Firestore: $e")
                }
        }
    }

    fun removeFavoriteRecipe(recipe: Recipe) {
        val currentRecipes = _favoriteRecipes.value?.list?.toMutableList()
        if (currentRecipes != null) {
            currentRecipes.remove(recipe)
        }
        _favoriteRecipes.value = currentRecipes?.let { _favoriteRecipes.value?.copy(list = it) }

        auth.currentUser?.uid?.let { userId ->
            db.collection("users").document(userId)
                .collection("favorite_recipes").document(recipe.id.toString())
                .delete()
                .addOnSuccessListener {
                    println("Favorite recipe removed from Firestore: $recipe")
                }
                .addOnFailureListener { e ->
                    println("Error removing favorite recipe from Firestore: $e")
                }
        }
    }

    private fun retrieveFavoriteRecipes() {
        auth.currentUser?.uid?.let { userId ->
            db.collection("users").document(userId)
                .collection("favorite_recipes")
                .get()
                .addOnSuccessListener { result ->
                    val favoriteRecipeIds = mutableListOf<Int>()
                    result.forEach { document ->
                        val recipeId = document.id.toInt()
                        favoriteRecipeIds.add(recipeId)
                        Log.d("TAG", "$recipeId")
                        println("Favorite recipe ID: $recipeId")
                    }
                    fetchRecipesInBulk(favoriteRecipeIds)
                }
                .addOnFailureListener { e ->
                    println("Error retrieving favorite recipe IDs from Firestore: $e")
                }
        }
    }

    data class RecipeState(
        val loading: Boolean = true,
        val list: List<Recipe> = emptyList(),
        val error: String? = null
    )

    data class SearchState(
        val loading: Boolean = false,  // Initialize with false
        val results: List<SearchResult> = emptyList(),
        val selectedRecipe: Recipe? = null,
        val error: String? = null
    )
}
