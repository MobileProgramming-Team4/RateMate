package com.example.ratemate.myPage

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

data class BarItem(
    val title: String,
    val selectIcon: ImageVector,
    val onSelectedIcon: ImageVector,
    val route: String
)

object NavBarItems {
    val BarItems = listOf(
        BarItem(
            title = "설문",
            selectIcon = Icons.Default.Search,
            onSelectedIcon = Icons.Outlined.Search,
            route = "Quest"
        ),
        BarItem(
            title = "답변",
            selectIcon = Icons.Default.CheckCircle,
            onSelectedIcon = Icons.Outlined.CheckCircle,
            route = "Answer"
        ),
        BarItem(
            title = "포인트 내역",
            selectIcon = Icons.Default.ShoppingCart,
            onSelectedIcon = Icons.Outlined.ShoppingCart,
            route = "Point"
        )

    )
}
