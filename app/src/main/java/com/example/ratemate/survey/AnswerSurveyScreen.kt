package com.example.ratemate.survey

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ratemate.survey.QuestionItem

// 답변 화면
@Composable
fun AnswerSurveyScreen(surveyTitle: String, questions: List<QuestionItem>, onSubmit: (Map<String, List<String>>) -> Unit) {
    val answers = remember { mutableStateMapOf<String, MutableList<String>>() }
    var currentPage by remember { mutableStateOf(0) }

    val isLastPage = currentPage == questions.size - 1
    val isFirstPage = currentPage == 0

    val allAnswered = answers.size == questions.size && answers.values.all { it.isNotEmpty() }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = surveyTitle,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Question(questions[currentPage], answers)
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (!isFirstPage) {
                    Button(onClick = { currentPage-- }) {
                        Text("이전")
                    }
                }
                if (!isLastPage) {
                    Button(onClick = { currentPage++ }) {
                        Text("다음")
                    }
                }
            }

            if (isLastPage && allAnswered) {
                Button(
                    onClick = { onSubmit(answers) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text("설문조사 제출")
                }
            }
        }
    }
}

@Composable
fun Question(question: QuestionItem, answers: MutableMap<String, MutableList<String>>) {
    // 현재 질문에 대해 이미 선택된 답변을 가져옴
    val selectedAnswers = answers.getOrPut(question.question) { mutableStateListOf() }

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = question.question,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        question.answers.forEach { answer ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (question.questionType == "single") {
                    RadioButton(
                        selected = selectedAnswers.contains(answer),
                        onClick = {
                            selectedAnswers.clear()
                            selectedAnswers.add(answer)
                            answers[question.question] = selectedAnswers
                        }
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
                        }
                    )
                }
                Text(text = answer, modifier = Modifier.padding(start = 8.dp))
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
        QuestionItem("질문3", mutableListOf("답변3-1", "답변3-2", "답변3-3", "답변3-4", "답변3-5", "답변3-6"), "single"),
        QuestionItem("질문4", mutableListOf("답변4-1", "답변4-2", "답변4-3", "답변4-4"), "multiple")
    )
    AnswerSurveyScreen("사용자 만족도 조사", sampleQuestions) { answers ->
        Log.d("Submitted answers", answers.toString())
    }
}