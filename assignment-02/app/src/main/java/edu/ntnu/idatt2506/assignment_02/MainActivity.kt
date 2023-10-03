package edu.ntnu.idatt2506.assignment_02

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }

    fun onGenerateNumberClicked(view: View?) {
        val intent = Intent(this, RandomNumberActivity::class.java)
        startActivity(intent)
    }

    fun onCalculateClicked(view: View) {
        val intent = Intent(this, GuessCalculationActivity::class.java)
        startActivity(intent)
    }

}