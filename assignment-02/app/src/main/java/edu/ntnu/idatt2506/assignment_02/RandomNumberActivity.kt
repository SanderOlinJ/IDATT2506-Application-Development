package edu.ntnu.idatt2506.assignment_02

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast

class RandomNumberActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.random_number_activity)
    }

    fun onGenerateNumberClicked(view: View?) {
        val intent = Intent(this, GenerateRandomNumberActivity::class.java)
        intent.putExtra("upper_limit", 100)
        startActivityForResult(intent,1)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK || requestCode != 1) {
            Log.e("onActivityResult()", "Error fetching result")
            return
        }

        val randomNumber = data.getIntArrayExtra("random_numbers")
        if (randomNumber == null || randomNumber.size != 1) {
            Log.e("onActivityResult()", "Did not receive correct result")
            return
        }
        Toast.makeText(this, "Random number: ${randomNumber[0]}",
            Toast.LENGTH_SHORT).show()
        val textView = findViewById<View>(R.id.textView) as TextView
        textView.text = "Random number: ${randomNumber[0]}"
    }

    fun onCalculateClicked(view: View) {
        val intent = Intent(this, GuessCalculationActivity::class.java)
        startActivity(intent)
    }
}