package com.example.ratemate.myPage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Answer() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val dataList = (1..10).map { it.toString() }.toMutableList()
        LazyColumn(
            contentPadding = PaddingValues(10.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(items = dataList) {item->
                Questionbox(text = item)
            }
        }
    }
}

@Composable
fun Point() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(350.dp, 100.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "현재 보유 포인트",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Box(
            modifier = Modifier
                .size(350.dp, 500.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "포인트 내역",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

    }
}

@Composable
fun Quest() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val dataList = (1..10).map { it.toString() }.toMutableList()
        LazyColumn(
            contentPadding = PaddingValues(10.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(items = dataList) {item->
                Questionbox(text = item)
            }
        }
    }
}

@Composable
fun Questionbox(text: String) {
    Box(
        modifier = Modifier
            .size(350.dp, 100.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(width = 4.dp, color = Color.Black, shape = RoundedCornerShape(16.dp))
            .clickable {

            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }

}