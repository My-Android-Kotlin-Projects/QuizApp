package com.example.quizapp.pages

import kotlinx.serialization.Serializable

sealed interface Routes{
    @Serializable
    data object WelcomeScreen: Routes

    @Serializable
    data class QuizScreen(val category: String): Routes

    @Serializable
    data object CategoriesScreen: Routes

    @Serializable
    data class ResultScreen(val correctAnswers: Int, val wrongAnswers: Int, val totalQuestions: Int): Routes
}