package com.example.onlinequizapp

import android.os.Bundle
import android.text.Layout
import android.view.View
import android.widget.Adapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onlinequizapp.databinding.ActivityMainBinding
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var quizModelList: MutableList<QuizModel>
    lateinit var adapter: QuizListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        quizModelList = mutableListOf()
        getDataFromFirebase()
    }

    private fun setupRecyclerView() {
        binding.progressBar.visibility =
            View.GONE  // for stopping the buffering after getting the data from firebase

        adapter = QuizListAdapter(quizModelList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }


    private fun getDataFromFirebase() {
        binding.progressBar.visibility =
            View.VISIBLE  // for showing the buffering before getting the data from firebase

        // now fetching the data from firebase after setting up the firebase console
        FirebaseDatabase.getInstance().reference
            .child("quizzes")
            .limitToFirst(5)  // Limit data to 5 items for faster load
            .get()
            .addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    for (snapshot in dataSnapshot.children) {
                        val quizModel = snapshot.getValue(QuizModel::class.java)
                        if (quizModel != null) {
                            quizModelList.add(quizModel)
                        }

                    }
                }

                else {
                    // Handle case where no data exists
                }
                setupRecyclerView()
            }

            .addOnFailureListener {
                // Handle failure (e.g., no internet connection)
            }


//       //dummy data
//
//        val listQuestionModel = mutableListOf<QuestionModel>()
//        listQuestionModel.add(QuestionModel("what is android?", mutableListOf("Language","OS","Product","None"),"OS"))
//        listQuestionModel.add(QuestionModel("who owns android?", mutableListOf("Apple","Google","Samsung","Microsoft"),"Google"))
//        listQuestionModel.add(QuestionModel("which assistant android uses?", mutableListOf("Siri","Cortana","Google Assistant","Alexa"),"Google Assistant"))
//        listQuestionModel.add(QuestionModel("Which company initially developed Android??", mutableListOf("Google","Apple","HTC","Samsung"),"Google"))
//        listQuestionModel.add(QuestionModel("What is the primary use of the Android SDK??", mutableListOf("Managing hardware","Creating games","Designing websites","Building apps"),"Building apps"))
//        listQuestionModel.add(QuestionModel("Which of the following is NOT a version of Android??", mutableListOf("Thunderbolt","Lollipop","KitKat","Cupcake"),"Thunderbolt"))
//        listQuestionModel.add(QuestionModel("What is the default web browser in Android??", mutableListOf("Edge","Firefox","Safari","Chrome"),"Chrome"))
//        listQuestionModel.add(QuestionModel("What is the Android version codename for 8.0?", mutableListOf("Pie","Oreo","Lollipop","Nougat"),"Oreo"))
//        listQuestionModel.add(QuestionModel("Which year was Android first released??", mutableListOf("2013","2010","2007","2005"),"2007"))

        //         quizModelList.add(QuizModel("1","Programming","All about the basic programming","10", listQuestionModel))
////        quizModelList.add(QuizModel("2","Computer","All  the computer question","20",))
////        quizModelList.add(QuizModel("3","Geography","Boost your geographic Knowledge","15"))



    }
}