package com.example.ratemate.home

import SurveyListScreen
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ratemate.Store.StoreScreen
import com.example.ratemate.myPage.MyPageScreen
import com.example.ratemate.setting.Option
import com.example.ratemate.survey.CreateSurveyScreen


sealed class HomeNavRoutes (val route: String) {
    object Home : HomeNavRoutes("Home")
    object Question : HomeNavRoutes("Question")
    object MyPage : HomeNavRoutes("MyPage")
    object Shop : HomeNavRoutes("Shop")
    object Option : HomeNavRoutes("Option")
}

@Composable
fun HomeNavigationHost(navController: NavHostController, startnavController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = HomeNavRoutes.Home.route
    ){
        composable(HomeNavRoutes.Home.route){
            SurveyListScreen(navController)
        }
        composable(HomeNavRoutes.Question.route){
            CreateSurveyScreen(navController)
        }
        composable(HomeNavRoutes.MyPage.route){
            MyPageScreen()
        }
        composable(HomeNavRoutes.Shop.route){
            StoreScreen(navController)
        }
        composable(HomeNavRoutes.Option.route){
            Option(startnavController)
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeNavigationBar(navController: NavController) {

    NavigationBar {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        HomeNavBarItems.HomeBarItems.forEach { navItem ->
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
                    Icon(
                        imageVector = if (currentRoute == navItem.route) navItem.onSelectedIcon else navItem.selectIcon,
                        contentDescription = navItem.title
                    )
                },
                label = {
                    Text(text = navItem.title)
                }
            )
        }
    }
}