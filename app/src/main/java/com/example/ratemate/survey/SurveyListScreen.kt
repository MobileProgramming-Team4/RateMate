package com.example.ratemate.survey

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ratemate.data.Survey
import com.example.ratemate.repository.SurveyRepository


@Composable
fun SurveyListScreen() {
    val repository = SurveyRepository()
    val viewModel: SurveyViewModel = viewModel(factory = SurveyModelFactory(repository))
    val surveys by viewModel.surveys.observeAsState(initial = null)
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "정렬 방식", style = MaterialTheme.typography.titleMedium)
            IconButton(onClick = { expanded = !expanded }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "정렬")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("최신순") },
                    onClick = {
                        viewModel.sortSurveys(SortType.LATEST)
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("좋아요 많은 순") },
                    onClick = {
                        viewModel.sortSurveys(SortType.MOST_LIKED)
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("답변 많은 순") },
                    onClick = {
                        viewModel.sortSurveys(SortType.MOST_RESPONDED)
                        expanded = false
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // null 체크 및 데이터 처리
        if (surveys == null) {
            CircularProgressIndicator() // 로딩 표시
        } else {
            LazyColumn {
                items(surveys ?: emptyList()) { survey ->
                    SurveyItem(survey)
                }
            }
        }
    }
}

@Composable
fun SurveyItem(survey: Survey) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = survey.title, style = MaterialTheme.typography.headlineSmall)
            Text(text = "작성자: ${survey.creatorId}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "좋아요: ${survey.likes}, 답변 수: ${survey.responses}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
