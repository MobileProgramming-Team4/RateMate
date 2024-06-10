package com.example.ratemate.login

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ratemate.R
import com.example.ratemate.data.User
import com.example.ratemate.repository.UserRepository
import com.example.ratemate.ui.theme.NotoSansKr
import com.example.ratemate.ui.theme.Purple40
import com.example.ratemate.viewModel.UserViewModel
import com.example.ratemate.viewModel.UserViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Date

@Composable
fun RegisterScreen(navController: NavHostController){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally

    ){


        var userMail by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var passwordCheck by remember { mutableStateOf("") }
        var passwordVisibility by remember { mutableStateOf(false) }
        var passwordVisibility2 by remember { mutableStateOf(false) }
        var addUser by rememberSaveable { mutableStateOf(false) }


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
                text = "Sign Up",
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
        ){
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
                Text(text = "회원가입 하기",
                    fontFamily = NotoSansKr,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.black))
            }

            OutlinedTextField(
                value = userMail,
                onValueChange = { userMail = it },
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
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Password
                    ),
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


            Spacer(modifier = Modifier.height(8.dp))
            Row {
                OutlinedTextField(
                    value = passwordCheck,
                    onValueChange = {passwordCheck = it},
                    label = { Text(text = "비밀번호 확인",
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
                    visualTransformation = if (passwordVisibility2) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hide()
                    }),
                    trailingIcon = {
                        val icon = if (passwordVisibility2) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        val description = if (passwordVisibility2) "Hide password" else "Show password"
                        IconButton(onClick = { passwordVisibility2 = !passwordVisibility2 }) {
                            Icon(imageVector = icon, contentDescription = description)
                        }
                    },
                    shape = MaterialTheme.shapes.large,


                    )
            }


            Spacer(modifier = Modifier.height(30.dp))
            Button(
                onClick = {
                    if (userMail.isEmpty()) {
                        Toast.makeText(context, "이메일을 입력해 주세요", Toast.LENGTH_SHORT).show()
                    } else if (password.isEmpty()) {
                        Toast.makeText(context, "비밀번호를 입력해 주세요", Toast.LENGTH_SHORT).show()
                    } else if (password.length < 6) {
                        Toast.makeText(context, "비밀번호는 6자 이상이어야 합니다.", Toast.LENGTH_SHORT).show()
                    } else if (password != passwordCheck) {
                        Toast.makeText(context, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        registerUser(userMail, password) {
                            if (it == "가입 성공") {
                                Toast.makeText(context, "회원가입 성공 : $userMail", Toast.LENGTH_SHORT).show()
                                addUser = true

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
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.main_blue)),
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                contentPadding = PaddingValues(vertical = 0.dp)
            ) {
                Text(
                    text = "회원가입",
                    fontFamily = NotoSansKr,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.white)
                )
            }

            Spacer(modifier = Modifier.height(100.dp))


            if (addUser) {
                addUser = false
                AddUserData(email = userMail, password = password)

                navController.navigate("login?mail=$userMail&pw=$password") {
                    popUpTo("Start"){
                        inclusive = true
                    }
                }
            }

        }




    }


}

//회원가입 함수
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


@Composable
fun AddUserData(email: String, password: String) {
    loginUser(email, password){}
    val madeUser = FirebaseAuth.getInstance().currentUser
    val userViewModel : UserViewModel = viewModel (factory = UserViewModelFactory(UserRepository()))

    val insertUser : User = User(
        userId = madeUser?.uid.toString(),
        email = madeUser?.email.toString(),
        password = password,
        points = 500,
        createdDate = Date().toString(),
        modifiedDate = Date().toString(),
        status = "active",
        profileImage = R.drawable.profile.toString(),
        surveysCreated = mutableListOf(),
        surveysParticipated = mutableListOf(),
        PurchaseList = mutableListOf()
    )

    userViewModel.addUser(insertUser)
    FirebaseAuth.getInstance().signOut()

}