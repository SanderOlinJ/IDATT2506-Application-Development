package edu.ntnu.idatt2506.assignment_02

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View

class GuessCalculationActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calculation_activity)

        val intent = Intent(this, GenerateRandomNumberActivity::class.java)
        intent.putExtra("upper_limit", 15);
        startActivityForResult(intent,1)
    }

    fun onAddClicked(view: View) {}
    fun onMultiplyClicked(view: View) {}
}