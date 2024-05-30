package com.example.ratemate.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {


        Row {
            val image: Painter = painterResource(id = R.drawable.logo_only)
            Image(
                painter = image,
                contentDescription = "Logo",
                modifier = Modifier
                    .size(20.dp)
                    .clip(RoundedCornerShape(30.dp))
            )
            Text(text = "Home Screen")
        }

        Spacer(modifier = Modifier.height(16.dp))


        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            Text(text = "Welcome ${user.email}")
        } else {
            Text(text = "Welcome")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            navController.navigate("SurveyResult")
        }) {
            Text("Survey")
        }


        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            FirebaseAuth.getInstance().signOut()
            navController.navigate("Start"){
                popUpTo("Start"){ inclusive = true }
            }
        }) {
            Text("Logout")
        }
    }
}