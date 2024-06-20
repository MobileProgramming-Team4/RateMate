package com.example.ratemate.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ratemate.R

@Composable
fun StartScreen(navController: NavHostController) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // PNG 이미지를 로드합니다.
        val startLogo: Painter = painterResource(id = R.drawable.start_logo)
        val startLogoTxt: Painter = painterResource(id = R.drawable.start_logo_txt)

        Image(
            painter = startLogo,
            contentDescription = "Logo",
            modifier = Modifier
                .size(100.dp)
        )

        Image(
            painter = startLogoTxt,
            contentDescription = "logo text",
            modifier = Modifier
                .padding(top = 28.dp)
                .size(100.dp)
        )

        Spacer(modifier = Modifier.height(140.dp))

        Button(
            onClick = {
                navController.navigate("login?mail=&pw=")
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
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.white)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                navController.navigate("register")
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
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.white)
            )
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStartScreen() {
    StartScreen(navController = rememberNavController())
}
