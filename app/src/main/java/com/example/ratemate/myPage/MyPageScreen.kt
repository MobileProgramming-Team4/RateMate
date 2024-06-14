package com.example.ratemate.myPage

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ratemate.ui.theme.NotoSansKr


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MyPageScreen(startNav: NavController) {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { /* Handle navigation icon press */ }) {
                        Icon(imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로가기", tint = Color.Black,
                            modifier = Modifier.clickable(onClick = {
                                if (navController.previousBackStackEntry != null) {
                                    navController.popBackStack()
                                }
                                else {
                                    startNav.navigate("Home")
                                }
                            })
                        )
                    }
                    Spacer(modifier = Modifier.weight(3.3f))
                    Text(
                        "My Page",
                        fontSize = 25.sp,
                        fontFamily = NotoSansKr,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(8f)
                    )

                }
                MyPageNavigationBar(navController)
            }
        },

    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            MyPageNavigationHost(navController = navController)
        }
    }
}