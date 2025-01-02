package com.example.quizapp.pages

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quizapp.db.QuizModel
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun QuizPage(modifier: Modifier = Modifier, category: String, navController: NavController) {
    val context = LocalContext.current
    var quizList by remember { mutableStateOf<List<QuizModel>>(emptyList()) }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var visible by remember { mutableStateOf(true) }
    var correctAnswer by remember { mutableStateOf("") }
    var selectedAnswer by remember { mutableStateOf("") }
    var score by remember { mutableStateOf(0) }
    var wrongAnsweredCounts by remember { mutableStateOf(0) }
    var indexBackgroundColor by remember { mutableStateOf(Color.Blue) }
    LaunchedEffect(Unit) {
        getDataFromFirebase( { data ->
            quizList = data
        }, category)
    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(indexBackgroundColor)
    ) {

        if (quizList.isNotEmpty()) {
            Row(horizontalArrangement = Arrangement.Center) {
                Text(text = "$category ${currentQuestionIndex+1}/${quizList[0].questionList.size}", modifier = Modifier.height(50.dp), color = Color.White)
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
                    modifier = Modifier
                        .background(Color.Gray)
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
            }
            else{
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
                navController.navigate(Routes.ResultScreen(score, wrongAnsweredCounts, quizList[0].questionList.size))
                return@Button
            }
        }) {
            Text(text = "Sonra ki Soru")
        }
    }
}

fun getDataFromFirebase(onDataReceived: (List<QuizModel>) -> Unit, category: String) {
    var quizList = mutableListOf<QuizModel>()
    FirebaseDatabase.getInstance("https://quizapp-ed25b-default-rtdb.europe-west1.firebasedatabase.app/").reference.get()
        .addOnSuccessListener { data ->
            if (data.exists()) {
                for (data in data.children) {
                    val question = data.getValue(QuizModel::class.java)
                    if (question != null && question?.title.toString().trim() == category.trim()) {
                        quizList.add(question)
                    }
                }
            }
            onDataReceived(quizList)
        }.addOnFailureListener {
            onDataReceived(emptyList())
        }
}