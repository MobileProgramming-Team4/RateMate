package com.example.ratemate.survey

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ratemate.R
import com.example.ratemate.common.CommonTextField
import com.example.ratemate.common.CommonTopAppBar
import com.example.ratemate.data.Like
import com.example.ratemate.data.QnA
import com.example.ratemate.data.SurveyV2
import com.example.ratemate.repository.SurveyV2Repository
import com.example.ratemate.ui.theme.NotoSansKr
import com.example.ratemate.viewModel.SurveyV2ViewModel
import com.example.ratemate.viewModel.SurveyV2ViewModelFactory

// 설문조사 생성 화면
@Composable
fun CreateSurveyScreen(navController: NavHostController) {
    val context = LocalContext.current
    val repository = SurveyV2Repository()
    val viewModel: SurveyV2ViewModel = viewModel(factory = SurveyV2ViewModelFactory(repository))

    var surveyTitle by remember { mutableStateOf("") }
    val questionsList = remember { mutableStateListOf<QuestionItem>() }

    val onNavigateToResult: (String) -> Unit = { /* Handle navigation to result */ }
//    val onNavigateBack: () -> Unit = { /* Handle navigation back */ }

    Scaffold(
        topBar = {
            CommonTopAppBar(title = "등록하기", onNavigateBack = { /* Handle navigation back */ })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            CommonTextField(
                label = "설문조사 제목",
                value = surveyTitle,
                onValueChange = { surveyTitle = it },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            questionsList.forEachIndexed { index, questionItem ->
                QuestionEditor(
                    question = questionItem,
                    onAddAnswer = {
                        val updatedAnswers = questionItem.answers.toMutableList()
                        updatedAnswers.add("")
                        questionsList[index] = questionItem.copy(answers = updatedAnswers)
                    },
                    onRemoveAnswer = { answerIndex ->
                        val updatedAnswers = questionItem.answers.toMutableList()
                        updatedAnswers.removeAt(answerIndex)
                        questionsList[index] = questionItem.copy(answers = updatedAnswers)
                    },
                    onRemoveQuestion = {
                        Log.d("index", index.toString())
                        questionsList.removeAt(index)
                    }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    modifier = Modifier
                        .width(168.dp)
                        .height(40.dp),
                    onClick = {
                        questionsList.add(QuestionItem("", mutableListOf(""), "single"))
                    },
                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.main_blue)),
                    contentPadding = PaddingValues(vertical = 0.dp)
                ) {
                    Text("단일 응답 질문 추가")
                }
                Button(
                    modifier = Modifier
                        .width(168.dp)
                        .height(40.dp),
                    onClick = {
                        questionsList.add(QuestionItem("", mutableListOf(""), "multiple"))
                    },
                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.main_blue)),
                    contentPadding = PaddingValues(vertical = 0.dp)
                ) {
                    Text(
                        "복수 응답 질문 추가",
                        fontFamily = NotoSansKr,
                        fontSize = 14.sp
                    )
                }
            }

            Button(
                onClick = {
                    val surveyData = SurveyV2(
                        surveyId = "", // 필요시 설정
                        creatorId = "", // 필요시 설정
                        title = surveyTitle,
                        content = "", // 필요시 설정
                        likes = Like(),
                        numOfComments = 0,
                        createdDate = "", // 필요시 설정
                        modifiedDate = "", // 필요시 설정
                        status = "active",
                        qnA = questionsList.mapIndexed { index, questionItem ->
                            QnA(
                                order = index + 1,
                                question = questionItem.question,
                                answerList = questionItem.answers,
                                answerCountList = List(questionItem.answers.size) { 0 },
                                questionType = questionItem.questionType
                            )
                        }.toMutableList(),
                        response = mutableListOf(),
                        comments = mutableListOf()
                    )
                    viewModel.addSurvey(surveyData)
////                    navController.navigate("SurveyResult/${surveyData.surveyId}")
////                    navController.navigate("SurveyResult")
//                    navController.navigate("Start") {
//                        popUpTo("Start") { inclusive = true }
//                    }
                },
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 0.dp),
                colors = ButtonDefaults.buttonColors(
                    colorResource(id = R.color.main_blue)
                ),
            ) {
                Text(
                    "설문조사 등록",
                    fontFamily = NotoSansKr,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.white)
                )
            }
        }
    }
}

@Composable
fun QuestionEditor(
    question: QuestionItem,
    onAddAnswer: () -> Unit,
    onRemoveAnswer: (Int) -> Unit,
    onRemoveQuestion: () -> Unit
) {
    var questionText by remember { mutableStateOf(question.question) }
    val answerTexts = remember { mutableStateListOf<String>().apply { addAll(question.answers) } }

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CommonTextField(
                label = "질문",
                value = questionText,
                onValueChange = {
                    questionText = it
                    question.question = it  // 바로 상태 업데이트
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 12.dp)
            )
            IconButton(
                onClick = { onRemoveQuestion() }
            ) {
                Icon(Icons.Default.Delete, contentDescription = "질문 삭제")
            }
        }

        answerTexts.forEachIndexed { index, answerText ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (question.questionType == "single") {
                    RadioButton(
                        selected = false,
                        onClick = { /* 비활성화 상태 */ },
                        enabled = false,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                } else {
                    Checkbox(
                        checked = false,
                        onCheckedChange = { /* 비활성화 상태 */ },
                        enabled = false,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
                CommonTextField(
                    label = "답변 옵션",
                    value = answerText,
                    onValueChange = {
                        answerTexts[index] = it
                        question.answers[index] = it  // 바로 상태 업데이트
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Text
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 12.dp),
                )
                IconButton(
                    onClick = {
                        answerTexts.removeAt(index)
                        question.answers.removeAt(index)
                        onRemoveAnswer(index)
                    },
                    enabled = answerTexts.size > 1 // 답변 옵션이 1개만 있을 때는 삭제 버튼 비활성화
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "답변 옵션 삭제")
                }
            }
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            onClick = {
                answerTexts.add("")
                question.answers.add("")
                onAddAnswer()
            },
            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.main_blue)),
            contentPadding = PaddingValues(vertical = 0.dp)
        ) {
            Text("옵션 추가")
        }
        Divider(modifier = Modifier.padding(vertical = 8.dp))
    }
}

//@Preview
//@Composable
//private fun PreviewCreateScreen() {
//    CreateSurveyScreen(
//        onSubmit = { title, questions ->
//            println("Title: $title")
//            questions.forEach { println("Question: ${it.question}, Answers: ${it.answers.joinToString()}, Type: ${it.questionType}") }
//        },
//        onNavigateBack = { /* Handle navigation back */ }
//    )
//}
