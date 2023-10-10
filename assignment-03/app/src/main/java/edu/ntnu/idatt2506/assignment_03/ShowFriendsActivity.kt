package edu.ntnu.idatt2506.assignment_03

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView

class ShowFriendsActivity : Activity() {

    private lateinit var liftOfFriends: ArrayList<String>
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_friends)

        liftOfFriends = intent.getStringArrayListExtra("liftOfFriends") as ArrayList<String>

        val friendsListView = findViewById<ListView>(R.id.friendsListView)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, liftOfFriends)
        friendsListView.adapter = adapter

        friendsListView.setOnItemClickListener { _, _, position, _ ->
            showEditDialog(position)
        }
    }

    private fun showEditDialog(position: Int) {
        Log.d("ShowFriendsActivity", "Edit button clicked for position: $position")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Change friend details")

        val input = EditText(this)
        input.setText(liftOfFriends[position])
        builder.setView(input)

        builder.setPositiveButton("OK") { _, _ ->
            val editedFriend = input.text.toString()
            liftOfFriends[position] = editedFriend
            adapter.notifyDataSetChanged()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    fun onReturnButtonClick(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putStringArrayListExtra("updatedList", liftOfFriends)
        setResult(RESULT_OK, intent)
        finish()
    }
}