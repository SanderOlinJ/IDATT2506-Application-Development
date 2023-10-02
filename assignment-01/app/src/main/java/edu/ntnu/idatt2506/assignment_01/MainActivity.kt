package edu.ntnu.idatt2506.assignment_01

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.d("Assignment", "onCreateOptionsMenu called")
        menuInflater.inflate(R.menu.options_menu_layout, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_first_name -> {
                Log.w("Assignment", "First name clicked.")
                true
            }
            R.id.item_last_name -> {
                Log.e("Assignment", "Last name clicked.")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
