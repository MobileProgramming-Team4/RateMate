package com.example.ratemate.survey

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ratemate.home.HomeNavRoutes
import com.example.ratemate.survey.SurveyResultScreen

sealed class SurveyNavRoutes (val route: String) {
    object AnswerSurvey: SurveyNavRoutes("AnswerSurvey")
    object SurveyResult: SurveyNavRoutes("SurveyResult")
}

@Composable
fun SurveyNavigationHost(navController: NavHostController, startnavController: NavHostController) {
    NavHost(navController = navController, startDestination = SurveyNavRoutes.AnswerSurvey.route) {
        composable(
            HomeNavRoutes.AnswerSurvey.route + "/{SurveyID}",
            arguments = listOf(
                navArgument(name = "SurveyID") {
                    type = NavType.StringType
                }
            )) {
            AnswerSurveyScreen(
                navController = startnavController,
                surveyId = it.arguments?.getString("SurveyID")
            )
        }

        composable(
            HomeNavRoutes.SurveyResult.route + "/{SurveyID}",
            arguments = listOf(
                navArgument(name = "SurveyID") {
                    type = NavType.StringType
                }
            )
        ) {
            SurveyResultScreen(
                navController = navController,
                SurveyID = it.arguments?.getString("SurveyID")
            )
        }
    }
}
