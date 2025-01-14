package com.example.quizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.quizapp.pages.CategoriesPage
import com.example.quizapp.pages.QuizPage
import com.example.quizapp.pages.ResultPage
import com.example.quizapp.pages.Routes
import com.example.quizapp.pages.UserViewModel
import com.example.quizapp.pages.WelcomePage
import com.example.quizapp.ui.theme.QuizAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        enableEdgeToEdge()
        setContent {
            QuizAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navigation(
                        modifier = Modifier.padding(innerPadding),
                        viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun Navigation(modifier: Modifier = Modifier, viewModel: UserViewModel) {
    val navController = rememberNavController()
    val username by viewModel.user.observeAsState()
    var startDestination: Routes?
    if (username == null || username!!.isEmpty()) {
        startDestination = Routes.WelcomeScreen
    } else {
        startDestination = Routes.CategoriesScreen
    }
    NavHost(navController = navController, startDestination = startDestination) {
        composable<Routes.WelcomeScreen> {
            WelcomePage(
                modifier = modifier,
                onGoToQuiz = {
                    navController.navigate(Routes.CategoriesScreen)
                },
                viewModel
            )
        }
        composable<Routes.CategoriesScreen> {
            CategoriesPage(
                modifier = modifier,
                navController = navController,
                viewModel
            )
        }
        composable<Routes.QuizScreen> {
            QuizPage(
                modifier = modifier,
                category = it.toRoute<Routes.QuizScreen>().category,
                navController = navController,
            )
        }
        composable<Routes.ResultScreen> {
            ResultPage(
                modifier = modifier,
                correctAnswers = it.toRoute<Routes.ResultScreen>().correctAnswers,
                wrongAnswers = it.toRoute<Routes.ResultScreen>().wrongAnswers,
                totalQuestions = it.toRoute<Routes.ResultScreen>().totalQuestions,
                navController,
                viewModel
            )
        }
    }
}



