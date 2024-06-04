package com.example.ratemate.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

data class HomeBarItem(
    val title: String,
    val selectIcon: ImageVector,
    val onSelectedIcon: ImageVector,
    val route: String
)

object HomeNavBarItems {
    val HomeBarItems = listOf(
        HomeBarItem(
            title = "등록하기",
            selectIcon = Icons.Default.Search,
            onSelectedIcon = Icons.Outlined.Search,
            route = "Question"
        ),
        HomeBarItem(
            title = "포인트 상점",
            selectIcon = Icons.Default.ShoppingCart,
            onSelectedIcon = Icons.Outlined.ShoppingCart,
            route = "Shop"
        ),
        HomeBarItem(
            title = "Home",
            selectIcon = Icons.Default.Home,
            onSelectedIcon = Icons.Outlined.Home,
            route = "Home"
        ),
        HomeBarItem(
            title = "My Page",
            selectIcon = Icons.Default.AccountCircle,
            onSelectedIcon = Icons.Outlined.AccountCircle,
            route = "MyPage"
        ),
        HomeBarItem(
            title = "Option",
            selectIcon = Icons.Default.Settings,
            onSelectedIcon = Icons.Outlined.Settings,
            route = "Option"
        )

    )
}