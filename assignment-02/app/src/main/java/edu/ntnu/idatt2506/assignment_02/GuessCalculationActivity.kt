package edu.ntnu.idatt2506.assignment_02

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class GuessCalculationActivity : Activity() {

    private var firstNumber: Int = 3
    private var secondNumber: Int = 5
    private var guessAnswer: Int = 8
    private var correctAnswer: Int = -1
    private var upperLimit: Int = 10


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calculation_activity)
    }

    fun onAddClicked(view: View) {
        getGuessAnswerAndUpperLimit()
        if (checkCalculationGuess(true)) {
            Toast.makeText(this, getString(R.string.correct), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "${getString(R.string.wrong)} $correctAnswer", Toast.LENGTH_SHORT).show()
        }
        generateNewRandomNumbers()
    }

    fun onMultiplyClicked(view: View) {
        getGuessAnswerAndUpperLimit()
        if (checkCalculationGuess(false)) {
            Toast.makeText(this, getString(R.string.correct), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "${getString(R.string.wrong)} $correctAnswer", Toast.LENGTH_SHORT).show()
        }

        generateNewRandomNumbers()
    }

    private fun getGuessAnswerAndUpperLimit(){
        val answerEditText = findViewById<EditText>(R.id.answer_limit_input)
        val upperLimitEditText = findViewById<EditText>(R.id.upper_limit_input)
        val answerText = answerEditText.text.toString()
        val upperLimitText = upperLimitEditText.text.toString()

        if (answerText.isEmpty() || upperLimitText.isEmpty()) {
            Log.e("getGuessAnswerAndUpperLimit()",
                "Error fetching guess answer and/or upper limit")
        }
        guessAnswer = answerText.toInt()
        upperLimit = upperLimitText.toInt()
    }

    private fun checkCalculationGuess(boolean: Boolean): Boolean {
        if (boolean) {
            correctAnswer = firstNumber + secondNumber
            return guessAnswer == correctAnswer
        }
        correctAnswer = firstNumber * secondNumber
        return guessAnswer == correctAnswer
    }

    private fun generateNewRandomNumbers() {
        val randomNumberIntent = Intent(this, GenerateRandomNumberActivity::class.java)
        randomNumberIntent.putExtra("upper_limit", upperLimit)
        randomNumberIntent.putExtra("amount_of_numbers", 2)
        startActivityForResult(randomNumberIntent, 1)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK || requestCode != 1) {
            Log.e("onActivityResult()", "Error fetching result")
            return
        }
        val randomNumbers = data?.getIntArrayExtra("random_numbers")
        if (randomNumbers == null || randomNumbers.size != 2) {
            Log.e("onActivityResult()", "Did not receive correct result")
            return
        }
        firstNumber = randomNumbers[0]
        secondNumber = randomNumbers[1]

        setNewNumbers()
    }

    @SuppressLint("SetTextI18n")
    private fun setNewNumbers() {
        val firstNumberTextView = findViewById<TextView>(R.id.first_number)
        val secondNumberTextView = findViewById<TextView>(R.id.second_number)

        Log.w("First Number", firstNumber.toString())
        firstNumberTextView.text = "$firstNumber"
        secondNumberTextView.text = "$secondNumber"
    }
}