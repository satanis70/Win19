package com.example.win19.services

import com.example.win19.model.QuestionModel
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitService {
    @GET("/win19/questions.json")
    fun getQuestions():Call<QuestionModel>
}