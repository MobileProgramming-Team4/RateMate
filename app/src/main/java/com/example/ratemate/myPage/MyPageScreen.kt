package com.example.ratemate.myPage

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ratemate.common.CommonTopAppBar


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MyPageScreen(startNav: NavController) {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            CommonTopAppBar(title = "My Page", onNavigateBack = {
                if (navController.previousBackStackEntry != null) {
                    navController.popBackStack()
                } else {
                    startNav.navigate("Home")
                }
            }, false)
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            MyPageNavigationBar(navController)
            MyPageNavigationHost(navController = navController, startNav)
        }
    }
}