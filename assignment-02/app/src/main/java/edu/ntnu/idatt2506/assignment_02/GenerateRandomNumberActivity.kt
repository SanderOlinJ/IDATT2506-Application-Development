package edu.ntnu.idatt2506.assignment_02

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class GenerateRandomNumberActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val upperLimit = intent.getIntExtra("upper_limit", 1000)
        val amountOfNumbers = intent.getIntExtra("amount_of_numbers", 1)
        val randomNumbers = (1..amountOfNumbers).map { (0..upperLimit).random() }
        val resultIntent = Intent()
        resultIntent.putExtra("random_numbers", randomNumbers.toIntArray())
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}