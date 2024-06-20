package com.example.ratemate.survey

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ratemate.R
import com.example.ratemate.data.SurveyV2
import com.example.ratemate.ui.theme.NotoSansKr

@Composable
fun SurveyItem(survey: SurveyV2, navController: NavController, destination: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { navController.navigate("$destination/${survey.surveyId}") },
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.gray_100),
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Text(
                text = survey.title, fontFamily = NotoSansKr,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = colorResource(R.color.gray_700)
            )

            Text(
                text = "작성자: ${survey.creatorId}\n답변 수: ${survey.response.size}, 좋아요: ${survey.likes.count}, 댓글 수: ${survey.comments.size}",
                fontFamily = NotoSansKr,
                fontSize = 12.sp,
                color = colorResource(R.color.gray_700)
            )
        }
    }
}