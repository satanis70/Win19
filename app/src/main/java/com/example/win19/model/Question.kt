package com.example.win19.model


import com.google.gson.annotations.SerializedName

data class Question(
    val answer1: Answer1,
    val answer2: Answer1,
    val answer3: Answer1,
    val answer4: Answer1,
    val question: String
)