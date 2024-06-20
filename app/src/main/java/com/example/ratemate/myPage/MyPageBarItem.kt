package com.example.ratemate.myPage

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

data class MyPageBarItem(
    val title: String,
    val route: String
)

object MyPageNavBarItems {
    val MyPageBarItems = listOf(
        MyPageBarItem(
            title = "내 설문",
            route = "Quest"
        ),
        MyPageBarItem(
            title = "내 답변",
            route = "Answer"
        ),
        MyPageBarItem(
            title = "포인트 내역",
            route = "Point"
        )

    )
}
