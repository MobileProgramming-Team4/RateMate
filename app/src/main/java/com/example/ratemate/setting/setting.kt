package com.example.ratemate.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Option() {
    var isDarkTheme by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(80.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "SETTING",
                    color = Color.Black,
                    fontSize = 40.sp
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
                    .padding(16.dp)
                    .width(300.dp)
            ) {
                Text(text = "화면 모드 설정", fontSize = 20.sp, fontWeight = FontWeight.Bold)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = !isDarkTheme,
                        onClick = { isDarkTheme = false }
                    )
                    Text(text = "라이트 모드")
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = isDarkTheme,
                        onClick = { isDarkTheme = true }
                    )
                    Text(text = "다크 모드")
                }
            }

            Box(
                modifier = Modifier
                    .size(150.dp, 80.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
                    .padding(16.dp)
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "로그아웃", fontSize = 25.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}