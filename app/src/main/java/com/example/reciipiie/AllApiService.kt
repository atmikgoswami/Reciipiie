package com.example.reciipiie

import com.example.reciipiie.data.Recipe
import com.example.reciipiie.data.RecipeResponse
import com.example.reciipiie.data.SearchResult
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Retrofit instance
private val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://api.spoonacular.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

// Service instance
val recipeService: ApiService = retrofit.create(ApiService::class.java)
val api_key = BuildConfig.API_KEY

// ApiService interface
interface ApiService {
    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("number") number: Int,
        @Query("apiKey") apiKey: String = api_key
    ): RecipeResponse

    @GET("recipes/{id}/information")
    suspend fun getRecipeById(
        @Path("id") id: Int,
        @Query("apiKey") apiKey: String = api_key
    ): Recipe

    @GET("recipes/autocomplete")
    suspend fun searchRecipes(
        @Query("query") query: String,
        @Query("number") number: Int,
        @Query("apiKey") apiKey: String = api_key
    ): List<SearchResult>

    @GET("recipes/informationBulk")
    suspend fun getRecipesInBulk(
        @Query("ids") recipeIds: String,
        @Query("apiKey") apiKey: String = api_key
    ): List<Recipe>
}

