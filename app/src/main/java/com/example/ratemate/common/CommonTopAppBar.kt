package com.example.ratemate.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ratemate.R
import com.example.ratemate.ui.theme.NotoSansKr


@Composable
fun CommonTopAppBar(title: String, onNavigateBack: () -> Unit) {
    Column {
        TopAppBar(
            title = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        modifier = Modifier.padding(end = 48.dp),
                        fontFamily = NotoSansKr,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = colorResource(id = R.color.gray_900)
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = { onNavigateBack() }) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                }
            },
            backgroundColor = colorResource(id = R.color.white),
            contentColor = colorResource(id = R.color.gray_900)
        )
        Divider(color = colorResource(id = R.color.gray_50))
    }
}
