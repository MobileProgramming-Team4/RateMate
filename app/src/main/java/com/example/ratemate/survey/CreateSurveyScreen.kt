package com.example.ratemate.survey

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ratemate.survey.QuestionItem

// 설문조사 생성 화면
@Composable
fun CreateSurveyScreen(onSubmit: (String, List<QuestionItem>) -> Unit) {
    var surveyTitle by remember { mutableStateOf("") }
    val questionsList = remember { mutableStateListOf<QuestionItem>() }


    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("새 설문조사 등록", style = MaterialTheme.typography.headlineLarge)
            OutlinedTextField(
                value = surveyTitle,
                onValueChange = { surveyTitle = it },
                label = { Text("제목") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            questionsList.forEachIndexed { index, questionItem ->
                QuestionEditor(
                    question = questionItem,
                    onAddAnswer = {
                        val updatedAnswers = questionItem.answers.toMutableList()
                        updatedAnswers.add("")
                        questionsList[index] = questionItem.copy(answers = updatedAnswers)
                    },
                    onRemove = {
                        questionsList.removeAt(index)
                    }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        questionsList.add(QuestionItem("", mutableListOf(""), "single"))
                    }
                ) {
                    Text("단일 응답 질문 추가")
                }
                Button(
                    onClick = {
                        questionsList.add(QuestionItem("", mutableListOf(""), "multiple"))
                    }
                ) {
                    Text("복수 응답 질문 추가")
                }
            }

            Button(
                onClick = { onSubmit(surveyTitle, questionsList) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text("설문조사 등록")
            }
        }
    }
}

@Composable
fun QuestionEditor(question: QuestionItem, onAddAnswer: () -> Unit, onRemove: () -> Unit) {
    var questionText by remember { mutableStateOf(question.question)}

    Column {
        OutlinedTextField(
            value = questionText,
            onValueChange = {
                questionText = it
                question.question = it  // 바로 상태 업데이트
            },
            label = { Text("질문") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
        question.answers.forEachIndexed { index, answer ->
            var answerText by remember { mutableStateOf(answer)}

            Row(verticalAlignment = Alignment.CenterVertically) {
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
                OutlinedTextField(
                    value = answerText,
                    onValueChange = {
                        answerText = it
                        question.answers[index] = it  // 바로 상태 업데이트
                    },
                    label = { Text("답변 옵션") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Button(
            onClick = onAddAnswer,
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        ) {
            Text("옵션 추가")
        }
        IconButton(onClick = onRemove, modifier = Modifier.align(Alignment.End)) {
            Icon(Icons.Default.Delete, contentDescription = "질문 삭제")
        }
        Divider(modifier = Modifier.padding(vertical = 8.dp))
    }
}

