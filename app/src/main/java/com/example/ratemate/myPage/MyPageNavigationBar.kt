package com.example.ratemate.myPage

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ratemate.R
import com.example.ratemate.ui.theme.NotoSansKr

sealed class MyPageNavRoutes (val route: String) {
    object Quest : MyPageNavRoutes("Quest")
    object Answer : MyPageNavRoutes("Answer")
    object Point : MyPageNavRoutes("Point")
    object SurveyResult: MyPageNavRoutes("SurveyResult")
}

@Composable
fun MyPageNavigationHost(navController: NavHostController, startnav: NavController) {
    NavHost(
        navController = navController,
        startDestination = MyPageNavRoutes.Quest.route
    ){
        composable(MyPageNavRoutes.Quest.route){
            Quest(navController, startnav)
        }
        composable(MyPageNavRoutes.Answer.route){
            Answer(navController, startnav)
        }
        composable(MyPageNavRoutes.Point.route){
            Point(navController)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyPageNavigationBar(navController: NavController) {

    NavigationBar(
        containerColor = colorResource(R.color.gray_50)
    )  {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        MyPageNavBarItems.MyPageBarItems.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.route,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = colorResource(R.color.gray_50),
                    selectedIconColor = colorResource(R.color.gray_900),
                    unselectedIconColor = colorResource(R.color.gray_500)
                ),
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
                    Text(text = navItem.title,
                        fontFamily = NotoSansKr,
                        fontSize = 18.sp,
                        fontWeight = if (currentRoute == navItem.route) FontWeight.Bold else FontWeight.Normal)
                },
                label = null
            )
        }
    }
}