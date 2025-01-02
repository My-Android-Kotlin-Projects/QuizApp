package com.example.quizapp.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.quizapp.R

@Composable
fun WelcomePage(modifier: Modifier = Modifier, onGoToQuiz: () -> Unit, viewModel: UserViewModel) {
    var name by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(true) }
    var imeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    LaunchedEffect(imeVisible) {
        if (!imeVisible) {
            // Perform your action here when the keyboard is hidden
            visible = true
            imeVisible = !imeVisible
        }
        else visible = false
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Blue)
            .imePadding()
    ) {
        AnimatedVisibility(visible = visible) {
            Image(

                painter = painterResource(id = R.drawable.bookreadinggirl),
                contentDescription = "Book reading girl",
                modifier = Modifier.padding(16.dp)
            )
        }

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Enter your name") },
            maxLines = 1,
            modifier = Modifier
                .padding(16.dp)
                .onFocusEvent { focusState ->
                    visible = !focusState.isFocused
                }
        )

        Text(text = "4 İşlem Dünyasına Hoş Geldiniz!", modifier = modifier)
        Button(onClick = {
            viewModel.insertUser(name)
            onGoToQuiz()
        }) {
            Text(text = "Başla")
        }
    }
}
