package edu.ntnu.idatt2506.assignment_03

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText

class MainActivity : Activity() {

    private val listOfFriends = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }

    fun onAddFriendClicked(view: View) {
        val nameEditText = findViewById<EditText>(R.id.name_of_friend)
        val friendName = nameEditText.text.toString()

        val birthdayEditText = findViewById<EditText>(R.id.birthday_of_friend)
        val friendBirthday = birthdayEditText.text.toString()

        listOfFriends.add("$friendName - $friendBirthday")
        nameEditText.text.clear()
        birthdayEditText.text.clear()
    }

    fun onShowFriendsClicked(view: View) {
        val intent = Intent(this, ShowFriendsActivity::class.java)
        intent.putExtra("liftOfFriends", listOfFriends)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val updatedList = data?.getStringArrayListExtra("updatedList")
            Log.d("onActivityResult", "RESULT OK")
            if (updatedList != null) {
                Log.d("onActivityResult", "List of Friends cleared")
                listOfFriends.clear()
                listOfFriends.addAll(updatedList)
            }
        }
    }
}
