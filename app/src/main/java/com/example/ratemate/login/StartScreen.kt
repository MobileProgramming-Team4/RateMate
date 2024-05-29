package com.example.ratemate.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ratemate.R
import com.example.ratemate.ui.theme.Purple40

@Composable
fun StartScreen(navController: NavHostController){

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        val image : Painter = painterResource(id = R.drawable.logo)
        Image(
            painter = image,
            contentDescription = "Logo",
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(50.dp))

        )


        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                navController.navigate("login?mail=&pw=")
            },
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(Purple40),
            modifier = Modifier
                .size(100.dp, 40.dp)
        ){
            Text(text = "Login")
        }


        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                navController.navigate("register")
            },
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(Purple40),
            modifier = Modifier
                .size(100.dp, 40.dp)
        ){
            Text(text = "Register")
        }
    }
}