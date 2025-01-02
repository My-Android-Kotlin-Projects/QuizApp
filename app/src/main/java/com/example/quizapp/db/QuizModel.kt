package com.example.quizapp.db

data class QuizModel(
    val id: Int,
    val title: String,
    val subtitle: String,
    val time: String,
    val questionList: List<QuestionModel>
){

    constructor(): this(0, "", "", "", emptyList())
}
data class QuestionModel(
    val question: String,
    val options: List<String>,
    val correctAnswer: String
){
    constructor(): this("", emptyList(), "")
}
