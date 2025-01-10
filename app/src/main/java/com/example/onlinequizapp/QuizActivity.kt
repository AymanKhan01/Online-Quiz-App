package com.example.onlinequizapp

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.onlinequizapp.databinding.ActivityQuizBinding
import com.example.onlinequizapp.databinding.ScoreDialogBinding

class QuizActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        var questionModelList: List<QuestionModel> = listOf()
        var time: String = ""

    }

    lateinit var binding: ActivityQuizBinding
    var currentQuestionIndex = 0;

    var selectedAnswer = ""   // for storing the selected answer
    var score =0;               // for checking the score by selected answer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btn0.setOnClickListener(this@QuizActivity)
            btn1.setOnClickListener(this@QuizActivity)
            btn2.setOnClickListener(this@QuizActivity)
            btn3.setOnClickListener(this@QuizActivity)
            nextBtn.setOnClickListener(this@QuizActivity)
        }

        loadQuestions()
        startTimer()
    }

    private fun startTimer() {
        val totalTimeInMillis = time.toInt() * 60 * 1000L
        object : CountDownTimer(totalTimeInMillis, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                val minutes = seconds / 60
                val remainingSeconds = seconds % 60
                binding.timerIndicatorTextview.text =
                    String.format("%02d:%02d", minutes, remainingSeconds)
            }

            override fun onFinish() {
                //Finish The Quiz

            }
        }.start()
    }

    private fun loadQuestions() {
    selectedAnswer = ""
        if (currentQuestionIndex == questionModelList.size){  // whenever we are in last question
            finishQuiz()                                      // to protect from crashing the app
            return                                            // we finish the quiz
        }
        binding.apply {
            questionIndicatorTextview.text =
                "Question ${currentQuestionIndex + 1}/ ${questionModelList.size} "
            questionProgressIndicator.progress =
                (currentQuestionIndex.toFloat() / questionModelList.size.toFloat() * 100).toInt()

            questionTextview.text = questionModelList[currentQuestionIndex].question
            btn0.text = questionModelList[currentQuestionIndex].option[0]
            btn1.text = questionModelList[currentQuestionIndex].option[1]
            btn2.text = questionModelList[currentQuestionIndex].option[2]
            btn3.text = questionModelList[currentQuestionIndex].option[3]


        }
    }

    override fun onClick(view: View?) {
        binding.apply {
            btn0.setBackgroundColor(getColor(R.color.gray))       // yeh hai taki option select
            btn1.setBackgroundColor(getColor(R.color.gray))       //karte time sirf single option hi
            btn2.setBackgroundColor(getColor(R.color.gray))       //highlight ho ske
            btn3.setBackgroundColor(getColor(R.color.gray))

        }

                val clickedBtn = view as Button
        if (clickedBtn.id == R.id.next_btn) {
            if(selectedAnswer.isEmpty()){  //taki users question bin select kiy skip na kar ske
                Toast.makeText(applicationContext,"Please select your answer to continue",Toast.LENGTH_SHORT).show()
                return;
            }
            //next button is clicked
            if(selectedAnswer == questionModelList[currentQuestionIndex].correct){   // for checking the slected answer is right or not
                score++     // store the total answer for last score
                Log.i("Score of quiz", "Correct answer selected. Score is: $score")
                }
            currentQuestionIndex++
            loadQuestions()
        } else {
            //option button is clicked

            selectedAnswer = clickedBtn.text.toString()     // for storing the selected answwer
            clickedBtn.setBackgroundColor(getColor(R.color.orange))     // for highlighting the option you choose
        }
      }

        private fun finishQuiz(){
                val totalQuestions = questionModelList.size
                val percentage = (( score.toFloat() /totalQuestions.toFloat() ) * 100).toInt()
            val dialogBinding = ScoreDialogBinding.inflate(layoutInflater)

            dialogBinding.apply {
                scoreProgressIndicator.progress = percentage
                scoreProgressText.text = "$percentage %"
                if(percentage>60){
                    scoreTitle.text = "Congrats! You have passed the quiz"
                scoreTitle.setTextColor(Color.BLUE)

                } else{
                    scoreTitle.text = "Oops! You failed"
                    scoreTitle.setTextColor(Color.RED)
                }
                scoreSubtitle.text = " $score out of $totalQuestions are correct"
                finishBtn.setOnClickListener{
                    finish()
                }
            }
            AlertDialog.Builder(this)
                .setView(dialogBinding.root)
                .setCancelable(false)
                .show()

        }
    }
