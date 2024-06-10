package com.example.ratemate.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ratemate.R
import com.example.ratemate.ui.theme.NotoSansKr
import com.example.ratemate.ui.theme.Purple40
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(navController: NavHostController, mail : String?, pw : String?){

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally) {

        var userMail by remember { mutableStateOf(mail ?:"") }
        var password by remember { mutableStateOf(pw ?:"") }

        var passwordVisibility by remember { mutableStateOf(false) }

        val context = LocalContext.current
        val keyboardController = LocalSoftwareKeyboardController.current

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Icon",
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .clickable(onClick = {
                            navController.navigate("Start") {
                                popUpTo("Start") { inclusive = true }
                            }
                        }
                        )
                )
            }
            Text(
                text = "Login",
                fontFamily = NotoSansKr,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.black),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Divider()

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val startLogo: Painter = painterResource(id = R.drawable.start_logo)
                Image(
                    painter = startLogo,
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(100.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "로그인 하기",
                    fontFamily = NotoSansKr,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.black))
            }



            OutlinedTextField(
                value = userMail,
                onValueChange = {userMail = it},
                label = { Text(text = "이메일",
                    color = Color.Gray,
                    fontSize = 15.sp,
                    fontFamily = NotoSansKr,
                    fontWeight = FontWeight.Bold
                    ) },
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .height(70.dp)
                ,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                ),
                shape = MaterialTheme.shapes.large,
            )


            Spacer(modifier = Modifier.height(8.dp))
            Row {
                OutlinedTextField(
                    value = password,
                    onValueChange = {password = it},
                    label = { Text(text = "비밀번호",
                        color = Color.Gray,
                        fontSize = 15.sp,
                        fontFamily = NotoSansKr,
                        fontWeight = FontWeight.Bold
                    ) },
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .height(70.dp)
                    ,
                    singleLine = true,
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hide()
                    }),
                    trailingIcon = {
                        val icon = if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        val description = if (passwordVisibility) "Hide password" else "Show password"
                        IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                            Icon(imageVector = icon, contentDescription = description)
                        }
                    },
                    shape = MaterialTheme.shapes.large,


                )
            }



            Spacer(modifier = Modifier.height(30.dp))
            Button(
                onClick = {
                    if (userMail.isEmpty()){
                        Toast.makeText(context, "이메일을 입력해 주세요", Toast.LENGTH_SHORT).show()
                    } else if (password.isEmpty()){
                        Toast.makeText(context, "비밀번호를 입력해 주세요", Toast.LENGTH_SHORT).show()
                    }
                    else if (password.length < 6){
                        Toast.makeText(context, "비밀번호는 6자 이상이어야 합니다.", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        loginUser(userMail, password) {
                            if (it == "로그인 성공") {
                                navController.navigate("Home"){
                                    popUpTo("Start"){
                                        inclusive = true
                                    }
                                }
                            } else {
                                if (it == "로그인 실패: The email address is badly formatted."){
                                    Toast.makeText(context, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
                                } else if (it == "로그인 실패: The supplied auth credential is incorrect, malformed or has expired.") {
                                    Toast.makeText(context, "이메일과 비밀번호를 확인해 주세요", Toast.LENGTH_SHORT).show()
                                }

                                else {
                                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                },
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.main_blue)),
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                contentPadding = PaddingValues(vertical = 0.dp)
            ) {
                Text(
                    text = "로그인",
                    fontFamily = NotoSansKr,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.white)
                )
            }


            Spacer(modifier = Modifier.height(100.dp))

        }

    }
}

fun loginUser(email: String, password: String, onResult: (String) -> Unit) {
    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onResult("로그인 성공")
            } else {
                onResult("로그인 실패: ${task.exception?.message ?: "Unknown error"}")
                Log.d("로그인 화면", "로그인 실패: ${task.exception?.message ?: "Unknown error"}")
            }
        }
}