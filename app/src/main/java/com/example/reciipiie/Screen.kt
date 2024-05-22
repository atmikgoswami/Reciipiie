package com.example.reciipiie

sealed class Screen(val route:String) {
    object HomeScreen:Screen("homeScreen")
    object FavouriteScreen:Screen("favouriteScreen")
    object RecipeScreen:Screen("recipeScreen")
    object SearchScreen:Screen("searchScreen")
    object SignInScreen:Screen("signinScreen")
    object ProfileScreen:Screen("profileScreen")
}