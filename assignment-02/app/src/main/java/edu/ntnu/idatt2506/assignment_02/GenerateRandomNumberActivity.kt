package edu.ntnu.idatt2506.assignment_02

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class GenerateRandomNumberActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val upperLimit = intent.getIntExtra("upper_limit", 1000)
        val randomNumber = (0..upperLimit).random()
        val resultIntent = Intent()
        resultIntent.putExtra("random_number", randomNumber)
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}