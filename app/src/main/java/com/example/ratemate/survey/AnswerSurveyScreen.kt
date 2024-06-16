package com.example.ratemate.survey

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ratemate.R
import com.example.ratemate.common.CommonTopAppBar
import com.example.ratemate.data.QnA
import com.example.ratemate.data.Response
import com.example.ratemate.repository.SurveyResultRepository
import com.example.ratemate.repository.SurveyV2Repository
import com.example.ratemate.repository.UserRepository
import com.example.ratemate.ui.theme.NotoSansKr
import com.example.ratemate.viewModel.SurveyResultViewModel
import com.example.ratemate.viewModel.SurveyResultViewModelFactory
import com.example.ratemate.viewModel.SurveyV2ViewModel
import com.example.ratemate.viewModel.SurveyV2ViewModelFactory
import com.example.ratemate.viewModel.UserViewModel
import com.example.ratemate.viewModel.UserViewModelFactory
import com.google.firebase.auth.FirebaseAuth

// 답변 화면
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswerSurveyScreen(navController: NavController, surveyId: String?) {
    val surveyV2ViewModel: SurveyV2ViewModel = viewModel(factory = SurveyV2ViewModelFactory(SurveyV2Repository()))
    val surveyResultViewModel: SurveyResultViewModel = viewModel(factory = SurveyResultViewModelFactory(
        SurveyResultRepository()
    ))
    val userViewModel : UserViewModel = viewModel (factory = UserViewModelFactory(UserRepository()))

    if (surveyId != null) {
        surveyV2ViewModel.getSurvey(surveyId)
    }

    val survey by surveyV2ViewModel.survey.collectAsState(initial = null)
    val isSurveyLoaded by surveyV2ViewModel.isSurveyLoaded.collectAsState()

    val answers = remember { mutableStateMapOf<String, MutableList<String>>() }
    var currentPage by remember { mutableStateOf(0) }

    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val userId = user?.uid ?: ""

    val isLastPage = currentPage == (survey?.qnA?.size ?: 0) - 1
    val isFirstPage = currentPage == 0
    val allAnswered = survey?.qnA?.let { questions ->
        answers.size == questions.size && answers.values.all { it.isNotEmpty() }
    } ?: false

    Scaffold(
        topBar = {
            CommonTopAppBar(title = "설문조사", onNavigateBack = { navController.popBackStack() })
        }
    ) { paddingValues ->
        if (isSurveyLoaded && survey != null) {
            survey?.let { surveyData ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(horizontal = 20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = surveyData.title,
                        modifier = Modifier.fillMaxSize(),
                        fontFamily = NotoSansKr,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                    Question(surveyData.qnA[currentPage], answers)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            modifier = Modifier
                                .width(168.dp)
                                .height(40.dp),
                            onClick = { if (!isFirstPage) currentPage-- },
                            enabled = !isFirstPage,
                            colors = ButtonDefaults.buttonColors(
                                if (isFirstPage) colorResource(id = R.color.gray_400) else colorResource(id = R.color.main_blue)
                            ),
                            contentPadding = PaddingValues(vertical = 0.dp)
                        ) {
                            Text(
                                text = "이전",
                                fontFamily = NotoSansKr,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isFirstPage) colorResource(id = R.color.gray_500) else colorResource(id = R.color.white)
                            )
                        }

                        Button(
                            modifier = Modifier
                                .width(168.dp)
                                .height(40.dp),
                            onClick = { if (!isLastPage) currentPage++ },
                            enabled = !isLastPage,
                            colors = ButtonDefaults.buttonColors(
                                if (isLastPage) colorResource(id = R.color.gray_400) else colorResource(id = R.color.main_blue)
                            ),
                            contentPadding = PaddingValues(vertical = 0.dp)
                        ) {
                            Text(
                                text = "다음",
                                fontFamily = NotoSansKr,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isLastPage) colorResource(id = R.color.gray_500) else colorResource(id = R.color.white)
                            )
                        }
                    }

                    if (allAnswered) {
                        Button(
                            onClick = {
                                val userResponses = surveyData.qnA.map { q ->
                                    answers[q.question]?.map { answer ->
                                        q.answerList.indexOf(answer)
                                    } ?: emptyList()
                                }

                                val response = Response(userId, userResponses)
                                val updatedQnA = surveyData.qnA.map { q ->
                                    val answerCounts = q.answerCountList.toMutableList()
                                    answers[q.question]?.forEach { answer ->
                                        val index = q.answerList.indexOf(answer)
                                        if (index >= 0) {
                                            answerCounts[index]++
                                        }
                                    }
                                    q.copy(answerCountList = answerCounts)
                                }

                                val updatedResponses = surveyData.response.toMutableList()
                                updatedResponses.add(response)

                                surveyV2ViewModel.updateSurvey(
                                    surveyData.surveyId,
                                    mapOf(
                                        "qnA" to updatedQnA,
                                        "response" to updatedResponses
                                    )
                                )

                                surveyId?.let {
                                    userViewModel.addSurveyToParticipated(userId, it)
                                }

                                Log.d("surveyResult", surveyData.toString())
                                navController.navigate("SurveyResult/${surveyData.surveyId}"){
                                    popUpTo("Home"){
                                        inclusive = false
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp),
                            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.main_blue)),
                            contentPadding = PaddingValues(vertical = 0.dp)
                        ) {
                            Text(
                                text = "설문조사 제출",
                                fontFamily = NotoSansKr,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Loading...")
            }
        }
    }
}

@Composable
fun Question(question: QnA, answers: MutableMap<String, MutableList<String>>) {
    // 현재 질문에 대해 이미 선택된 답변을 가져옴
    val selectedAnswers = answers.getOrPut(question.question) { mutableStateListOf() }

    Column(modifier = Modifier.padding(vertical = 0.dp)) {
        Text(
            text = question.question,
            fontFamily = NotoSansKr,
            fontSize = 16.sp
        )
        question.answerList.forEach { answer ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (question.questionType == "single") {
                    RadioButton(
                        selected = selectedAnswers.contains(answer),
                        onClick = {
                            selectedAnswers.clear()
                            selectedAnswers.add(answer)
                            answers[question.question] = selectedAnswers
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = colorResource(id = R.color.main_blue),
                            unselectedColor = colorResource(id = R.color.gray_200)
                        )
                    )
                } else {
                    Checkbox(
                        checked = selectedAnswers.contains(answer),
                        onCheckedChange = { checked ->
                            if (checked) {
                                selectedAnswers.add(answer)
                            } else {
                                selectedAnswers.remove(answer)
                            }
                            answers[question.question] = selectedAnswers
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = colorResource(id = R.color.main_blue),
                            uncheckedColor = colorResource(id = R.color.gray_200)
                        )
                    )
                }
                Text(
                    text = answer,
                    fontFamily = NotoSansKr,
                    fontSize = 16.sp
                )
            }
        }
    }
}