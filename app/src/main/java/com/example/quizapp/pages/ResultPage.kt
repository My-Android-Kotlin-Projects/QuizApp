package com.example.quizapp.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ResultPage(modifier: Modifier = Modifier, correctAnswers: Int, wrongAnswers: Int, totalQuestions: Int, navController: NavController, viewModel: UserViewModel) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(text = "Doğru Cevaplar: $correctAnswers", modifier = Modifier.background(color = Color(0xFF4CAF50), shape = RoundedCornerShape(8.dp)).padding(8.dp).width(200.dp))
            Spacer(modifier = Modifier.padding(8.dp))
            Text(text = "Yanlış Cevaaplar: $wrongAnswers", modifier = Modifier.background(color = Color(0xFFF44336), shape = RoundedCornerShape(8.dp)).padding(8.dp).width(200.dp))
            Spacer(modifier = Modifier.padding(8.dp))
            Text(text = "Toplam Soru: $totalQuestions", modifier = Modifier.background(color = Color(0xFF2196F3), shape = RoundedCornerShape(8.dp)).padding(8.dp).width(200.dp))
            Spacer(modifier = Modifier.padding(8.dp))
            Button(onClick = { navController.navigate(Routes.CategoriesScreen) }) {
                Text(text ="Tekrar Dene")
            }
            Button(onClick = {
                viewModel.deleteAllUsers()
                navController.navigate(Routes.WelcomeScreen) }) {
                Text(text ="Yeni kişi  Dene")
            }
        }
}