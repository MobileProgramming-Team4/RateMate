package com.example.ratemate.myPage

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.ratemate.R
import com.example.ratemate.data.Survey
import com.example.ratemate.data.SurveyV2
import com.example.ratemate.repository.PointTransactionRepository
import com.example.ratemate.repository.StoreItemRepository
import com.example.ratemate.repository.SurveyRepository
import com.example.ratemate.repository.SurveyV2Repository
import com.example.ratemate.repository.UserRepository
import com.example.ratemate.ui.theme.NotoSansKr
import com.example.ratemate.viewModel.PointTransactionViewModel
import com.example.ratemate.viewModel.PointTransactionViewModelFactory
import com.example.ratemate.viewModel.SortType
import com.example.ratemate.viewModel.StoreItemViewModel
import com.example.ratemate.viewModel.StoreItemViewModelFactory
import com.example.ratemate.viewModel.SurveyModelFactory
import com.example.ratemate.viewModel.SurveyV2ViewModel
import com.example.ratemate.viewModel.SurveyV2ViewModelFactory
import com.example.ratemate.viewModel.SurveyViewModel
import com.example.ratemate.viewModel.UserViewModel
import com.example.ratemate.viewModel.UserViewModelFactory
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Answer(navController: NavHostController, startnav:NavController) {
    val repository = SurveyV2Repository()
    val viewModel: SurveyV2ViewModel = viewModel(factory = SurveyV2ViewModelFactory(repository))
    val surveys by viewModel.surveys.collectAsState(initial = emptyList())
    LaunchedEffect(Unit) {
        viewModel.getAllSurveys()
    }

    val auth = FirebaseAuth.getInstance()
    val uid = auth.currentUser?.uid
    val userViewModel : UserViewModel = viewModel (factory = UserViewModelFactory(UserRepository()))
    userViewModel.getUser(uid!!)
    val user by userViewModel.user.collectAsState(initial = null)

    var expanded by remember { mutableStateOf(false) }
    var sortText by remember { mutableStateOf("최신순") }
    user?.let {
        Scaffold(
            content = { paddingValues ->
                Column(modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)) {
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
                                    text = { Text("최신순",fontFamily = NotoSansKr,
                                        fontWeight = FontWeight.Bold) },
                                    onClick = {
                                        viewModel.sortSurveys(SortType.LATEST)
                                        sortText = "최신순"
                                        expanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("좋아요 많은 순", fontFamily = NotoSansKr,
                                        fontWeight = FontWeight.Bold) },
                                    onClick = {
                                        viewModel.sortSurveys(SortType.MOST_LIKED)
                                        sortText = "좋아요 많은 순"
                                        expanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("답변 많은 순", fontFamily = NotoSansKr,
                                        fontWeight = FontWeight.Bold) },
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
                    LazyColumn {
                        items(surveys) { survey ->
                            if(user!!.surveysParticipated.contains(survey.surveyId)){
                                SurveyItem(survey, startnav)
                            }
                        }
                    }
                }
            }
        )
    }

}

@Composable
fun Point(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val uid = auth.currentUser?.uid
    val userViewModel : UserViewModel = viewModel (factory = UserViewModelFactory(UserRepository()))
    userViewModel.getUser(uid!!)
    val user by userViewModel.user.collectAsState(initial = null)

    val repository = PointTransactionRepository()
    val viewModel: PointTransactionViewModel = viewModel(factory = PointTransactionViewModelFactory(repository))
    val pointsT by viewModel.pointTransactions.collectAsState(initial = emptyList())
    LaunchedEffect(Unit) {
        viewModel.loadAllPointTransactions()
    }

    user?.let{
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.small_logo2), // 실제 drawable로 교체
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(text = "${user!!.email}님", fontSize = 30.sp, color = Color.Black, fontWeight = FontWeight.Bold )
                    Text(text = "잔여 포인트: ${user!!.points}", fontSize = 28.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                }

            }
            LazyColumn {
                items(pointsT) { point ->
                    if(point.userId == user!!.userId){
                        point.transactionType?.let { it1 -> PointItem(itemName = it1, itemPoints = point.amount) }
                    }
                }
            }
        }
    }




}

@Composable
fun Quest(navController: NavHostController, startnav:NavController) {
    val repository = SurveyV2Repository()
    val viewModel: SurveyV2ViewModel = viewModel(factory = SurveyV2ViewModelFactory(repository))
    val surveys by viewModel.surveys.collectAsState(initial = emptyList())
    LaunchedEffect(Unit) {
        viewModel.getAllSurveys()
    }

    val auth = FirebaseAuth.getInstance()
    val uid = auth.currentUser?.uid
    val userViewModel : UserViewModel = viewModel (factory = UserViewModelFactory(UserRepository()))
    userViewModel.getUser(uid!!)
    val user by userViewModel.user.collectAsState(initial = null)

    var expanded by remember { mutableStateOf(false) }
    var sortText by remember { mutableStateOf("최신순") }

    user?.let {
        Scaffold(
            content = { paddingValues ->
                Column(modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)) {
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
                                    text = { Text("최신순",fontFamily = NotoSansKr,
                                        fontWeight = FontWeight.Bold) },
                                    onClick = {
                                        viewModel.sortSurveys(SortType.LATEST)
                                        sortText = "최신순"
                                        expanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("좋아요 많은 순", fontFamily = NotoSansKr,
                                        fontWeight = FontWeight.Bold) },
                                    onClick = {
                                        viewModel.sortSurveys(SortType.MOST_LIKED)
                                        sortText = "좋아요 많은 순"
                                        expanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("답변 많은 순", fontFamily = NotoSansKr,
                                        fontWeight = FontWeight.Bold) },
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
                    LazyColumn {
                        items(surveys) { survey ->
                            if(user!!.surveysCreated.contains(survey.surveyId)){
                                SurveyItem(survey, startnav)
                            }
                        }
                    }
                }
            }
        )
    }

}

@Composable
fun SurveyItem(survey: SurveyV2, navController: NavController) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)
        .clickable { navController.navigate("Result/${survey.surveyId}") }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = survey.title, style = MaterialTheme.typography.headlineSmall)
            Text(text = "작성자: ${survey.creatorId}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "좋아요: ${survey.likes.count}, 답변 수: ${survey.response.size}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun PointItem(itemName: String, itemPoints: Int) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = itemName, style = MaterialTheme.typography.headlineSmall)
            Text(text = "${itemPoints}P", style = MaterialTheme.typography.headlineSmall)
        }
    }
}