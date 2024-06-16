package com.example.ratemate.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ratemate.home.HomeScreen
import com.example.ratemate.login.LoginScreen
import com.example.ratemate.login.RegisterScreen
import com.example.ratemate.login.StartScreen
import com.example.ratemate.survey.AnswerSurveyScreen
import com.example.ratemate.survey.CreateSurveyScreen
import com.example.ratemate.survey.ResultScreen

sealed class Route(val route: String){
    object Start: Route("Start")
    object Login: Route("Login")
    object Register: Route("Register")
    object Home: Route("Home")
    object SurveyList: Route("SurveyList")
    object CreateSurvey : Route("CreateSurvey")
    object Result : Route("Result")
    object AnswerSurvey: Route("AnswerSurvey")

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

        composable(Route.CreateSurvey.route) {
            CreateSurveyScreen(navController)
        }

        composable(
            route = "Result?surveyId={surveyId}",
            arguments = listOf(
                navArgument("surveyId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val surveyId = backStackEntry.arguments?.getString("surveyId")
            ResultScreen(navController = navController, surveyId = surveyId)
        }

        composable(
            route = "AnswerSurvey?surveyId={surveyId}",
            arguments = listOf(
                navArgument("surveyId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val surveyId = backStackEntry.arguments?.getString("surveyId")
            AnswerSurveyScreen(navController = navController, surveyId = surveyId)
        }


    }
}