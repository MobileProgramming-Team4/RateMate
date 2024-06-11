package com.example.ratemate.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.navigation.NavHostController
import com.example.ratemate.R
import com.google.firebase.auth.FirebaseAuth


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Option(navController: NavHostController) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var imgchange by remember { mutableStateOf(true) }

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
                    modifier = Modifier.padding(start = 140.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                )
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 프로필 사진 변경 섹션
        SectionTitle("프로필 사진 변경")
        Spacer(modifier = Modifier.height(15.dp))
        if(imgchange){
            Image(
                painter = painterResource(id = R.drawable.profile), // 프로필 이미지 리소스
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            )
        }
        else{
            Image(
                painter = painterResource(id = R.drawable.profile2), // 프로필 이미지 리소스
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            )
        }

        Spacer(modifier = Modifier.height(15.dp))
        Button(
            onClick = { imgchange = !imgchange },
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = Color.LightGray
            )
        ) {
            Text("이미지 등록")
        }

        Spacer(modifier = Modifier.height(64.dp))


        // 비밀번호 변경 섹션
        SectionTitle("비밀번호 변경")
        Spacer(modifier = Modifier.height(15.dp))
        CustomTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("비밀번호") },
            placeholder = { Text("입력해주세요.") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("비밀번호 확인") },
            placeholder = { Text("다시 입력해주세요.") }
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                if (password == confirmPassword) {
                    FirebaseAuth.getInstance().currentUser?.updatePassword(password)
                    showSuccessDialog = true
                    password = ""
                    confirmPassword = ""
                } else {
                    showErrorDialog = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("비밀번호 변경")
        }
        Spacer(modifier = Modifier.height(30.dp))

        // 로그아웃 버튼
        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate("Start")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("로그아웃")
        }

        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = { showErrorDialog = false },
                confirmButton = {
                    TextButton(onClick = { showErrorDialog = false }) {
                        Text("OK")
                    }
                },
                title = { Text("오류") },
                text = { Text("비밀번호가 일치하지 않습니다. 다시 입력해주세요.") }
            )
        }

        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { showSuccessDialog = false },
                confirmButton = {
                    TextButton(onClick = { showSuccessDialog = false }) {
                        Text("OK")
                    }
                },
                title = { Text("성공") },
                text = { Text("비밀번호가 성공적으로 변경되었습니다.") }
            )
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




