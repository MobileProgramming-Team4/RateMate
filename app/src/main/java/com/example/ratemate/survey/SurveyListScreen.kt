import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ratemate.data.SurveyV2
import com.example.ratemate.repository.SurveyV2Repository
import com.example.ratemate.ui.theme.NotoSansKr
import com.example.ratemate.viewModel.SortType
import com.example.ratemate.viewModel.SurveyV2ViewModel
import com.example.ratemate.viewModel.SurveyV2ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyListScreen(navController: NavController) {
    val repository = SurveyV2Repository()
    val viewModel: SurveyV2ViewModel = viewModel(factory = SurveyV2ViewModelFactory(repository))
    val surveys by viewModel.surveys.collectAsState(initial = emptyList())

    var expanded by remember { mutableStateOf(false) }
    var sortText by remember { mutableStateOf("최신순") }

    LaunchedEffect(Unit) {
        viewModel.getAllSurveys()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Home",
                            fontSize = 25.sp,
                            fontFamily = NotoSansKr,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        sortText,
                        fontSize = 15.sp,
                        fontFamily = NotoSansKr,
                        fontWeight = FontWeight.Bold
                    )
                    Box {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "정렬", tint = Color.Black)
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("최신순", fontFamily = NotoSansKr, fontWeight = FontWeight.Bold) },
                                onClick = {
                                    viewModel.sortSurveys(SortType.LATEST)
                                    sortText = "최신순"
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("좋아요 많은 순", fontFamily = NotoSansKr, fontWeight = FontWeight.Bold) },
                                onClick = {
                                    viewModel.sortSurveys(SortType.MOST_LIKED)
                                    sortText = "좋아요 많은 순"
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("답변 많은 순", fontFamily = NotoSansKr, fontWeight = FontWeight.Bold) },
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
                if (surveys.isEmpty()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally)) // 로딩 표시
                } else {
                    LazyColumn {
                        items(surveys) { survey ->
                            SurveyItem(survey, navController)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun SurveyItem(survey: SurveyV2, navController: NavController) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)
        .clickable { navController.navigate("answerSurvey/${survey.surveyId}") }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = survey.title, style = MaterialTheme.typography.headlineSmall)
            Text(text = "작성자: ${survey.creatorId}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "좋아요: ${survey.likes.count}, 답변 수: ${survey.response.size}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
