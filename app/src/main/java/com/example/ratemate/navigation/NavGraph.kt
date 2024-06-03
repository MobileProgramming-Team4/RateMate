package com.example.ratemate.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ratemate.Store.StoreScreen
import com.example.ratemate.home.HomeScreen
import com.example.ratemate.home.SurveyResultScreen
import com.example.ratemate.login.LoginScreen
import com.example.ratemate.login.RegisterScreen
import com.example.ratemate.login.StartScreen
import com.example.ratemate.survey.SurveyListScreen

sealed class Route(val route: String){
    object Start: Route("Start")
    object Login: Route("Login")
    object Register: Route("Register")
    object Home: Route("Home")
    object SurveyResult: Route("SurveyResult")
    object SurveyList: Route("SurveyList")
    object Store: Route("Store")
}

@Composable
fun NavGraph(navController: NavHostController, startDestination: Route) {
    NavHost(navController = navController, startDestination = startDestination.route) {
        composable("Start") {
            StartScreen(navController)
        }
        composable(
            route = "Login?mail={mail}&pw={pw}",
            arguments = listOf(
                navArgument("mail") { type = NavType.StringType },
                navArgument("pw") { type = NavType.StringType }
            )
        ){
                backStackEntry ->
            val mail = backStackEntry.arguments?.getString("mail")
            val pw = backStackEntry.arguments?.getString("pw")
            LoginScreen(navController, mail, pw)
        }


        composable("Register") {
            RegisterScreen(navController)
        }
        composable("Home") {
            HomeScreen(navController)
        }
        composable("SurveyList") {
            SurveyListScreen(navController = navController)
        }

        composable("SurveyResult") {
            SurveyResultScreen(navController)
        }

        composable("Store") {
            StoreScreen(navController)
        }
    }
}