package com.example.ratemate.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ratemate.R
import com.example.ratemate.ui.theme.Purple40
import com.google.firebase.auth.FirebaseAuth

@Composable
fun RegisterScreen(navController: NavHostController){

    var userMail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier.fillMaxSize()
    ){

        Button(
            onClick = {
                navController.navigate("Start"){
                    popUpTo("Start") {
                        inclusive = true
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                Color.Transparent,
                contentColor = Color.Black
            )
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
        }



        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = "회원가입", modifier = Modifier.height(40.dp))
            val image: Painter = painterResource(id = R.drawable.small_logo)
            Image(
                painter = image,
                contentDescription = "Logo",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(30.dp))
            )


            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = userMail,
                onValueChange = { userMail = it },
                label = { Text("E-mail") },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                )
            )


            Spacer(modifier = Modifier.height(8.dp))
            Row {
                OutlinedTextField(
                    value = password,
                    onValueChange = {password = it},
                    label = { Text("Password") },
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
                    }
                )
            }


            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (userMail.isEmpty()) {
                        Toast.makeText(context, "이메일을 입력해 주세요", Toast.LENGTH_SHORT).show()
                    } else if (password.isEmpty()) {
                        Toast.makeText(context, "비밀번호를 입력해 주세요", Toast.LENGTH_SHORT).show()
                    } else if (password.length < 6) {
                        Toast.makeText(context, "비밀번호는 6자 이상이어야 합니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        registerUser(userMail, password) {
                            if (it == "가입 성공") {
                                Toast.makeText(context, "회원가입 성공 : $userMail", Toast.LENGTH_SHORT).show()
                                navController.navigate("login?mail=$userMail&pw=$password") {
                                    popUpTo("Start"){
                                        inclusive = true
                                    }
                                }
                            } else {
                                if (it == "회원가입 실패: The email address is already in use by another account.") {
                                    Toast.makeText(context, "이미 등록된 사용자 입니다.", Toast.LENGTH_SHORT)
                                        .show()
                                } else if (it == "회원가입 실패: The email address is badly formatted.") {
                                    Toast.makeText(context, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT)
                                        .show()
                                } else {
                                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                },

                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(Purple40),
                modifier = Modifier
                    .size(100.dp, 40.dp)
            ) {
                Text("회원가입")
            }
        }
    }
}

fun registerUser(email: String, password: String, onResult: (String) -> Unit) {
    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onResult("가입 성공")
            } else {
                onResult("회원가입 실패: ${task.exception?.message ?: "Unknown error"}")
            }
        }
}