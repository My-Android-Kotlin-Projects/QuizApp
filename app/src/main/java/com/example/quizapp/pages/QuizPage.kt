package com.example.quizapp.pages

import android.annotation.SuppressLint
import android.widget.ProgressBar
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quizapp.db.QuizModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun QuizPage(modifier: Modifier = Modifier, category: String, navController: NavController) {
    val context = LocalContext.current
    var quizList by remember { mutableStateOf<List<QuizModel>>(emptyList()) }
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var visible by remember { mutableStateOf(true) }
    var correctAnswer by remember { mutableStateOf("") }
    var selectedAnswer by remember { mutableStateOf("") }
    var score by remember { mutableIntStateOf(0) }
    var wrongAnsweredCounts by remember { mutableIntStateOf(0) }
    var indexBackgroundColor by remember { mutableStateOf(Color.Blue) }
    var time by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        getDataFromFirebase({ data, quizTime ->
            quizList = data
            time = quizTime
        }, category)
    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(indexBackgroundColor)
    ) {

        if (time > 0) Timer(
            time = time * 60,
            navController = navController,
            score = score,
            wrongAnsweredCounts = wrongAnsweredCounts,
            totalQuestions = quizList[0].questionList.size
        )

        if (quizList.isNotEmpty()) {
            Row(horizontalArrangement = Arrangement.Center) {
                Text(
                    text = "$category ${currentQuestionIndex + 1}/${quizList[0].questionList.size}",
                    modifier = Modifier.height(50.dp),
                    color = Color.White
                )
            }
            val question = quizList[0].questionList[currentQuestionIndex]
            correctAnswer = question.correctAnswer
            AnimatedVisibility(
                visible = visible,
            ) {
                OutlinedTextField(
                    value = question.question,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.background(Color.Gray)
                )
            }

            question.options.forEach { option ->
                val buttonColor = when {
                    selectedAnswer == option -> Color.Red
                    else -> Color(0xFF51D17C)
                }
                Button(
                    onClick = { selectedAnswer = option },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Text(text = option)
                }
            }
        } else {
            Text(text = "Yükleniyor...")
        }
        val coroutineScope = rememberCoroutineScope()
        Button(onClick = {
            if (selectedAnswer == "") {
                Toast.makeText(context, "Bir Cevap sec!", Toast.LENGTH_SHORT).show()
                return@Button
            }
            if (selectedAnswer == correctAnswer) {
                indexBackgroundColor = Color.Green
                coroutineScope.launch {
                    delay(500)
                    indexBackgroundColor = Color.Blue
                }
                score++
            } else {
                indexBackgroundColor = Color.Red
                coroutineScope.launch {
                    delay(500)
                    indexBackgroundColor = Color.Blue
                }
                wrongAnsweredCounts++
            }
            selectedAnswer = ""
            visible = false
            coroutineScope.launch {
                delay(500)
                visible = true
            }
            if (currentQuestionIndex < quizList[0].questionList.size - 1) {
                currentQuestionIndex++
            } else {
                navController.navigate(
                    Routes.ResultScreen(
                        score, wrongAnsweredCounts, quizList[0].questionList.size
                    )
                )
                return@Button
            }
        }) {
            Text(text = "Sonra ki Soru")
        }
    }
}

fun getDataFromFirebase(onDataReceived: (List<QuizModel>, Int) -> Unit, category: String): Int {
    var quizList = mutableListOf<QuizModel>()
    var time = 0
    FirebaseDatabase.getInstance("https://quizapp-ed25b-default-rtdb.europe-west1.firebasedatabase.app/").reference.get()
        .addOnSuccessListener { data ->
            if (data.exists()) {
                for (data in data.children) {
                    val question = data.getValue(QuizModel::class.java)
                    if (question != null && question?.title.toString().trim() == category.trim()) {
                        quizList.add(question)
                        time = question.time.toInt()
                    }
                }
            }
            onDataReceived(quizList, time)
        }.addOnFailureListener {
            onDataReceived(emptyList(), 0)
        }
    return time
}

@SuppressLint("DefaultLocale")
@Composable
fun Timer(
    modifier: Modifier = Modifier,
    time: Int,
    navController: NavController,
    score: Int,
    wrongAnsweredCounts: Int,
    totalQuestions: Int
) {
    var timeLeft by remember { mutableStateOf(time) }
    var progressBar by remember { mutableStateOf(timeLeft.toFloat() / time) }
    var minutes = timeLeft / 60
    var seconds = timeLeft % 60
    val currentScore by rememberUpdatedState(newValue = score)
    val currentWrongAnswers by rememberUpdatedState(newValue = wrongAnsweredCounts)
    LaunchedEffect(Unit) {
        while (timeLeft > 0) {
            delay(1000L)
            timeLeft--
            progressBar = timeLeft.toFloat() / time
        }
        navController.navigate(
            Routes.ResultScreen(
                currentScore, currentWrongAnswers, totalQuestions
            )
        )
    }
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(100.dp)) {
        CircularProgressIndicator(modifier = modifier.size(100.dp),
            color = Color.Green,
            trackColor = Color.Gray,
            progress = { progressBar })
        Text(text = String.format("%02d:%02d", minutes, seconds), color = Color.White)
    }
    Spacer(modifier = modifier.height(16.dp))
}