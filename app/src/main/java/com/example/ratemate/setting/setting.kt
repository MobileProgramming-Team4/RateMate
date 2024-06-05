package com.example.ratemate.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ratemate.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Option() {
    var selectedMode by remember { mutableStateOf("light") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = {
                Text(
                    "Setting",
                    modifier = Modifier.padding(start = 100.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                )
            },
            navigationIcon = {
                IconButton(onClick = { /* TODO: handle back navigation */ }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 프로필 사진 변경 섹션
        SectionTitle("프로필 사진 변경")
        Spacer(modifier = Modifier.height(15.dp))
        Image(
            painter = painterResource(id = R.drawable.profile), // 프로필 이미지 리소스
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.Gray)
        )
        Spacer(modifier = Modifier.height(15.dp))
        Button(
            onClick = { /* TODO: handle image registration */ },
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = Color.LightGray
            )
        ) {
            Text("이미지 등록")
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 모드 변경 섹션
        SectionTitle("모드 변경")
        Spacer(modifier = Modifier.height(15.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            CustomRadioButton(
                text = "라이트 모드",
                selected = selectedMode == "light",
                onClick = { selectedMode = "light" }
            )
            Spacer(modifier = Modifier.width(16.dp))
            CustomRadioButton(
                text = "다크 모드",
                selected = selectedMode == "dark",
                onClick = { selectedMode = "dark" }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 비밀번호 변경 섹션
        SectionTitle("비밀번호 변경")
        Spacer(modifier = Modifier.height(15.dp))
        CustomTextField(
            value = "",
            onValueChange = { /* TODO: handle password input */ },
            label = { Text("비밀번호") },
            placeholder = { Text("입력해주세요.") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField(
            value = "",
            onValueChange = { /* TODO: handle password confirmation input */ },
            label = { Text("비밀번호 확인") },
            placeholder = { Text("다시 입력해주세요.") }
        )

        Spacer(modifier = Modifier.height(50.dp))

        // 로그아웃 버튼
        Button(
            onClick = { /* TODO: handle logout */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("로그아웃")
        }
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.LightGray.copy(alpha = 0.2f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        visualTransformation = PasswordVisualTransformation()
    )
}

@Composable
fun CustomRadioButton(text: String, selected: Boolean, onClick: () -> Unit) {
    Text(
        text = text,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
            .background(
                if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent,
                RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
    )
}