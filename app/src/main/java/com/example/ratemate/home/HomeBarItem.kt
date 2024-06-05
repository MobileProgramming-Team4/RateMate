package com.example.ratemate.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MonetizationOn
import androidx.compose.material.icons.outlined.Settings
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
            selectIcon = Icons.Outlined.AddBox,
            onSelectedIcon = Icons.Default.AddBox,
            route = "Question"
        ),
        HomeBarItem(
            title = "포인트 상점",
            selectIcon = Icons.Outlined.MonetizationOn,
            onSelectedIcon = Icons.Default.MonetizationOn,
            route = "Shop"
        ),
        HomeBarItem(
            title = "홈",
            selectIcon = Icons.Outlined.Home,
            onSelectedIcon = Icons.Default.Home,
            route = "Home"
        ),
        HomeBarItem(
            title = "마이페이지",
            selectIcon = Icons.Outlined.AccountCircle,
            onSelectedIcon = Icons.Default.AccountCircle,
            route = "MyPage"
        ),
        HomeBarItem(
            title = "설정",
            selectIcon = Icons.Outlined.Settings,
            onSelectedIcon = Icons.Default.Settings,
            route = "Option"
        )

    )
}