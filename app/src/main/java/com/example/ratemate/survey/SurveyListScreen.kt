import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ratemate.data.SurveyV2
import com.example.ratemate.data.User
import com.example.ratemate.repository.SurveyV2Repository
import com.example.ratemate.repository.UserRepository
import com.example.ratemate.ui.theme.NotoSansKr
import com.example.ratemate.viewModel.SortType
import com.example.ratemate.viewModel.SurveyV2ViewModel
import com.example.ratemate.viewModel.SurveyV2ViewModelFactory
import com.example.ratemate.viewModel.UserViewModel
import com.example.ratemate.viewModel.UserViewModelFactory
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyListScreen(navController: NavController) {
    val surveyRepository = SurveyV2Repository()
    val surveyViewModel: SurveyV2ViewModel =
        viewModel(factory = SurveyV2ViewModelFactory(surveyRepository))
    val surveys by surveyViewModel.surveys.collectAsState(initial = emptyList())

    val userRepository = UserRepository()
    val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(userRepository))

    var user by remember { mutableStateOf<User?>(null) }

    var expanded by remember { mutableStateOf(false) }
    var sortText by remember { mutableStateOf("최신순") }

    val auth = FirebaseAuth.getInstance()
    val uid = auth.currentUser?.uid

    // UID를 print로 출력
    LaunchedEffect(uid) {
        println("Logged in user ID: $uid")
    }

    LaunchedEffect(Unit) {
        uid?.let {
            userViewModel.getUser(it)
        }
        surveyViewModel.getAllSurveys()
    }

    LaunchedEffect(userViewModel.user) {
        userViewModel.user.collect { fetchedUser ->
            user = fetchedUser
        }
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
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
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
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "정렬",
                                tint = Color.Black
                            )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "최신순",
                                        fontFamily = NotoSansKr,
                                        fontWeight = FontWeight.Bold
                                    )
                                },
                                onClick = {
                                    surveyViewModel.sortSurveys(SortType.LATEST)
                                    sortText = "최신순"
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "좋아요 많은 순",
                                        fontFamily = NotoSansKr,
                                        fontWeight = FontWeight.Bold
                                    )
                                },
                                onClick = {
                                    surveyViewModel.sortSurveys(SortType.MOST_LIKED)
                                    sortText = "좋아요 많은 순"
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "답변 많은 순",
                                        fontFamily = NotoSansKr,
                                        fontWeight = FontWeight.Bold
                                    )
                                },
                                onClick = {
                                    surveyViewModel.sortSurveys(SortType.MOST_RESPONDED)
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
                        items(surveys.filter { survey ->
                            user?.let {
                                survey.surveyId !in it.surveysCreated && survey.surveyId !in it.surveysParticipated
                            } ?: true
                        }) { survey ->
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
        .clickable { navController.navigate("AnswerSurvey/${survey.surveyId}") }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = survey.title, style = MaterialTheme.typography.headlineSmall)
            Text(text = "작성자: ${survey.creatorId}", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "좋아요: ${survey.likes.count}, 답변 수: ${survey.response.size}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
