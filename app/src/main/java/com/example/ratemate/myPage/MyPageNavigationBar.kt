package com.example.ratemate.myPage

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.ratemate.home.SurveyResultScreen
import com.example.ratemate.navigation.Route

sealed class MyPageNavRoutes (val route: String) {
    object Quest : MyPageNavRoutes("Quest")
    object Answer : MyPageNavRoutes("Answer")
    object Point : MyPageNavRoutes("Point")
    object SurveyResult: MyPageNavRoutes("SurveyResult")
}

@Composable
fun MyPageNavigationHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = MyPageNavRoutes.Quest.route
    ){
        composable(MyPageNavRoutes.Quest.route){
            Quest(navController)
        }
        composable(MyPageNavRoutes.Answer.route){
            Answer(navController)
        }
        composable(MyPageNavRoutes.Point.route){
            Point(navController)
        }
        composable(MyPageNavRoutes.SurveyResult.route + "/{SurveyID}",
            arguments = listOf(
                navArgument(name = "SurveyID") {
                    type = NavType.StringType
                }
            )) {
            SurveyResultScreen(
                navController = navController,
                SurveyID = it.arguments?.getString("SurveyID")
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyPageNavigationBar(navController: NavController) {

    NavigationBar {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        MyPageNavBarItems.MyPageBarItems.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.route,
                onClick = {
                    navController.navigate(navItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Text(text = navItem.title)
                },
                label = null
            )
        }
    }
}