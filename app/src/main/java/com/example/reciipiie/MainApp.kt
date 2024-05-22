package com.example.reciipiie

import android.app.Activity.RESULT_OK
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.reciipiie.data.Recipe
import com.example.reciipiie.screens.FavouritesScreen
import com.example.reciipiie.screens.Homescreen
import com.example.reciipiie.screens.ProfileScreen
import com.example.reciipiie.screens.RecipeScreen
import com.example.reciipiie.screens.SearchScreen
import com.example.reciipiie.screens.SignInScreen
import com.example.reciipiie.viewModels.RecipeViewModel
import com.example.reciipiie.viewModels.SignInViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainApp(
    navController: NavHostController = rememberNavController(),
    googleAuthUiClient: GoogleAuthUiClient,
    lifecycleScope: LifecycleCoroutineScope,
    applicationContext: Context
){
    val recipeViewModel: RecipeViewModel = viewModel()
    val viewState by recipeViewModel.recipeState

    NavHost(navController = navController, startDestination = Screen.SignInScreen.route ) {

        composable(Screen.SignInScreen.route) {
            val viewModel = viewModel<SignInViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            LaunchedEffect(key1 = Unit) {
                if(googleAuthUiClient.getSignedInUser() != null) {
                    navController.navigate(Screen.HomeScreen.route)
                }
            }

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if(result.resultCode == RESULT_OK) {
                        lifecycleScope.launch {
                            val signInResult = googleAuthUiClient.signInWithIntent(
                                intent = result.data ?: return@launch
                            )
                            viewModel.onSignInResult(signInResult)
                        }
                    }
                }
            )

            LaunchedEffect(key1 = state.isSignInSuccessful) {
                if(state.isSignInSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "Sign in successful",
                        Toast.LENGTH_LONG
                    ).show()

                    navController.navigate(Screen.HomeScreen.route)
                    viewModel.resetState()
                }
            }

            SignInScreen(
                state = state,
                onSignInClick = {
                    lifecycleScope.launch {
                        val signInIntentSender = googleAuthUiClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                }
            )
        }

        composable(Screen.ProfileScreen.route) {
            ProfileScreen(
                userData = googleAuthUiClient.getSignedInUser(),
                onSignOut = {
                    lifecycleScope.launch {
                        googleAuthUiClient.signOut()
                        Toast.makeText(
                            applicationContext,
                            "Signed out",
                            Toast.LENGTH_LONG
                        ).show()
                        navController.navigate(Screen.SignInScreen.route)
                    }
                }
            )
        }

        composable(route = Screen.HomeScreen.route){
            Homescreen(navController, viewState = viewState, userData = googleAuthUiClient.getSignedInUser(),navigateToDetail = {
                navController.currentBackStackEntry?.savedStateHandle?.set("cat", it)
                navController.navigate(Screen.RecipeScreen.route)
            })
        }

        composable(route = Screen.FavouriteScreen.route){
                FavouritesScreen(navController, navigateToDetail = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("cat",it)
                    navController.navigate(Screen.RecipeScreen.route)
                })
        }

        composable(route = Screen.SearchScreen.route){
            SearchScreen(navController,navigateToDetail = {
                navController.currentBackStackEntry?.savedStateHandle?.set("cat", it)
                navController.navigate(Screen.RecipeScreen.route){
                    popUpTo(Screen.SearchScreen.route) { inclusive = false }
                }
            })
        }

        composable(route = Screen.RecipeScreen.route){
            val category = navController.previousBackStackEntry?.savedStateHandle?.get<Recipe>("cat")?: Recipe()
            RecipeScreen(navController,category = category)
        }
    }
}
