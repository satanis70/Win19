package com.example.win19

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.win19.databinding.ActivityMainBinding
import com.example.win19.model.Question
import com.example.win19.model.QuestionModel
import com.example.win19.services.RetrofitService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var questionList = ArrayList<Question>()
    private val rightAnswer: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CoroutineScope(Dispatchers.IO).launch{
            val api = Retrofit.Builder()
                .baseUrl("http://49.12.202.175/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrofitService::class.java)
            val request = api.getQuestions().awaitResponse()
            if (request.isSuccessful){
                questionList.addAll(request.body()!!.questions)
                CoroutineScope(Dispatchers.Main).launch {
                    Log.d("LIST", questionList.toString())
                    showNextQuiz()
                }
            }
        }

    }

    fun showNextQuiz(){
        binding.textViewQuestion.text = questionList[0].question
        binding.buttonAnswer1.text = questionList[0].answer1.name
        binding.buttonAnswer2.text = questionList[0].answer2.name
        binding.buttonAnswer3.text = questionList[0].answer3.name
        binding.buttonAnswer4.text = questionList[0].answer4.name
    }

}