package com.example.ratemate.survey

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ratemate.data.Survey
import com.example.ratemate.repository.SurveyRepository
import com.example.ratemate.viewModel.SortType
import com.example.ratemate.viewModel.SurveyModelFactory
import com.example.ratemate.viewModel.SurveyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyListScreen(navController: NavController) {
    val repository = SurveyRepository()
    val viewModel: SurveyViewModel = viewModel(factory = SurveyModelFactory(repository))
    val surveys by viewModel.surveys.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    var sortText by remember { mutableStateOf("최신순") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Home", color = Color.White)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { /* Handle navigation icon press */ }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "뒤로가기", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        bottomBar = {
            BottomNavigation(
                contentColor = Color.White
            ) {
                BottomNavigationItem(
                    icon = { Icon(imageVector = Icons.Default.Add, contentDescription = "등록하기") },
                    label = { Text("등록하기") },
                    selected = false,
                    onClick = { /* Handle navigation */ }
                )
                BottomNavigationItem(
                    icon = { Icon(imageVector = Icons.Default.AttachMoney, contentDescription = "포인트 상점") },
                    label = { Text("포인트 상점") },
                    selected = false,
                    onClick = { /* Handle navigation */ }
                )
                BottomNavigationItem(
                    icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "홈") },
                    label = { Text("홈") },
                    selected = true,
                    onClick = { /* Handle navigation */ }
                )
                BottomNavigationItem(
                    icon = { Icon(imageVector = Icons.Default.Person, contentDescription = "마이페이지") },
                    label = { Text("마이페이지") },
                    selected = false,
                    onClick = { /* Handle navigation */ }
                )
                BottomNavigationItem(
                    icon = { Icon(imageVector = Icons.Default.Settings, contentDescription = "설정") },
                    label = { Text("설정") },
                    selected = false,
                    onClick = { /* Handle navigation */ }
                )
            }
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(sortText, color = Color.White, style = MaterialTheme.typography.bodyLarge)
                    Box {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "정렬", tint = Color.Black)
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("최신순", color = Color.Black) },
                                onClick = {
                                    viewModel.sortSurveys(SortType.LATEST)
                                    sortText = "최신순"
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("좋아요 많은 순", color = Color.Black) },
                                onClick = {
                                    viewModel.sortSurveys(SortType.MOST_LIKED)
                                    sortText = "좋아요 많은 순"
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("답변 많은 순", color = Color.Black) },
                                onClick = {
                                    viewModel.sortSurveys(SortType.MOST_RESPONDED)
                                    sortText = "답변 많은 순"
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                if (surveys == null) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally)) // 로딩 표시
                } else {
                    LazyColumn {
                        items(surveys ?: emptyList()) { survey ->
                            SurveyItem(survey)
                        }
                    }
                }
            }
        }
    )
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

@Preview(showBackground = true)
@Composable
fun SurveyListScreenPreview() {
    SurveyListScreen(navController = rememberNavController())
}
