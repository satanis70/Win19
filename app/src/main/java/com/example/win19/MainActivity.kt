package com.example.win19

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.win19.databinding.ActivityMainBinding
import com.example.win19.model.Question
import com.example.win19.services.RetrofitService
import com.onesignal.OneSignal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var questionList = ArrayList<Question>()
    private var currentPosition = 0
    private var score = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        OneSignal.initWithContext(this)
        OneSignal.setAppId("714b9f14-381d-4fc4-a93c-28d480557381")
        getData()
        binding.buttonNext.setOnClickListener {
            binding.buttonNext.isEnabled = false
            binding.buttonAnswer1.setBackgroundResource(R.color.buttons)
            binding.buttonAnswer2.setBackgroundResource(R.color.buttons)
            binding.buttonAnswer3.setBackgroundResource(R.color.buttons)
            binding.buttonAnswer4.setBackgroundResource(R.color.buttons)
            getData()
        }
    }

    private fun getData(){
        questionList.clear()
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
                    binding.textViewScore.text = "${currentPosition+1}/${questionList.size}"
                    if (currentPosition==questionList.size){
                        val intent = Intent(this@MainActivity, ScoreActivity::class.java)
                        intent.putExtra("score", score)
                        intent.putExtra("all", questionList.size)
                        startActivity(intent)
                    }
                    showNextQuiz(currentPosition)
                    currentPosition+=1
                }
            }
        }
    }

    private fun showNextQuiz(currentPosition: Int) {
        binding.textViewQuestion.text = questionList[currentPosition].question
        val button1 = binding.buttonAnswer1
        val button2 = binding.buttonAnswer2
        val button3 = binding.buttonAnswer3
        val button4 = binding.buttonAnswer4
        val buttonNext = binding.buttonNext
        button1.text = questionList[currentPosition].answer1.name
        button2.text = questionList[currentPosition].answer2.name
        button3.text = questionList[currentPosition].answer3.name
        button4.text = questionList[currentPosition].answer4.name

        button1.setOnClickListener {
            if (questionList[currentPosition].answer1.trueorfalse=="true"){
                button1.setBackgroundColor(Color.GREEN)
                button2.isClickable = false
                button3.isClickable = false
                button4.isClickable = false
                score+=1
                buttonNext.isEnabled = true
            } else {
                button1.setBackgroundColor(Color.RED)
                button2.isClickable = false
                button3.isClickable = false
                button4.isClickable = false
                buttonNext.isEnabled = true
            }
        }
        button2.setOnClickListener {
            if (questionList[currentPosition].answer2.trueorfalse=="true"){
                button1.isClickable = false
                button2.setBackgroundColor(Color.GREEN)
                button3.isClickable = false
                button4.isClickable = false
                score+=1
                buttonNext.isEnabled = true
            } else {
                button1.isClickable = false
                button2.setBackgroundColor(Color.RED)
                button3.isClickable = false
                button4.isClickable = false
                buttonNext.isEnabled = true
            }
        }
        button3.setOnClickListener {
            if (questionList[currentPosition].answer3.trueorfalse=="true"){
                button1.isClickable = false
                button2.isClickable = false
                button3.setBackgroundColor(Color.GREEN)
                button4.isClickable = false
                score+=1
                buttonNext.isEnabled = true
            }
            else {
                button1.isClickable = false
                button2.isClickable = false
                button3.setBackgroundColor(Color.RED)
                button4.isClickable = false
                buttonNext.isEnabled = true
            }
        }
        button4.setOnClickListener {
            if (questionList[currentPosition].answer4.trueorfalse=="true"){
                button1.isClickable = false
                button2.isClickable = false
                button3.isClickable = false
                button4.setBackgroundColor(Color.GREEN)
                score+=1
                buttonNext.isEnabled = true
            }
            else {
                button1.isClickable = false
                button2.isClickable = false
                button3.isClickable = false
                button4.setBackgroundColor(Color.RED)
                buttonNext.isEnabled = true
            }
        }
    }
}