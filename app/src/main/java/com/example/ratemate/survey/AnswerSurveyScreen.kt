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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ratemate.R
import com.example.ratemate.common.CommonTopAppBar
import com.example.ratemate.ui.theme.NotoSansKr

// 답변 화면
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswerSurveyScreen(
    surveyTitle: String,
    questions: List<QuestionItem>,
    onSubmit: (Map<String, List<String>>) -> Unit,
    onNavigateBack: () -> Unit
) {
    val answers = remember { mutableStateMapOf<String, MutableList<String>>() }
    var currentPage by remember { mutableStateOf(0) }

    val isLastPage = currentPage == questions.size - 1
    val isFirstPage = currentPage == 0

    val allAnswered = answers.size == questions.size && answers.values.all { it.isNotEmpty() }

    Scaffold(
        topBar = {
            CommonTopAppBar(title = "설문조사", onNavigateBack)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = surveyTitle,
                modifier = Modifier.fillMaxSize(),
                fontFamily = NotoSansKr,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Question(questions[currentPage], answers)

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
                        color = if (isFirstPage) colorResource(id = R.color.gray_500) else colorResource(
                            id = R.color.white
                        )
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
                        color = if (isLastPage) colorResource(id = R.color.gray_500) else colorResource(
                            id = R.color.white
                        )
                    )
                }
            }

            if (allAnswered) {
                Button(
                    onClick = { onSubmit(answers) },
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
}

@Composable
fun Question(question: QuestionItem, answers: MutableMap<String, MutableList<String>>) {
    // 현재 질문에 대해 이미 선택된 답변을 가져옴
    val selectedAnswers = answers.getOrPut(question.question) { mutableStateListOf() }

    Column(modifier = Modifier.padding(vertical = 0.dp)) {
        Text(
            text = question.question,
            fontFamily = NotoSansKr,
            fontSize = 16.sp
        )
        question.answers.forEach { answer ->
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


@Preview(showBackground = true)
@Composable
fun PreviewTakeSurveyScreen() {
    val sampleQuestions = listOf(
        QuestionItem("질문1", mutableListOf("답변1-1", "답변1-2", "답변1-3"), "single"),
        QuestionItem("질문2", mutableListOf("답변2-1", "답변2-2", "답변2-3", "답변2-4", "답변2-5"), "multiple"),
        QuestionItem(
            "질문3",
            mutableListOf("답변3-1", "답변3-2", "답변3-3", "답변3-4", "답변3-5", "답변3-6"),
            "single"
        ),
        QuestionItem("질문4", mutableListOf("답변4-1", "답변4-2", "답변4-3", "답변4-4"), "multiple")
    )
    AnswerSurveyScreen(
        surveyTitle = "사용자 만족도 조사",
        questions = sampleQuestions,
        onSubmit = { answers ->
            Log.d("Submitted answers", answers.toString())
        },
        onNavigateBack = { /* Handle navigation back */ }
    )
}