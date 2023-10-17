package edu.ntnu.idatt2506.assignment_06_host

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val receivedMessageTextView = findViewById<TextView>(R.id.received_message)
        val sentMessageTextView = findViewById<TextView>(R.id.sent_message)
        Server(receivedMessageTextView, sentMessageTextView).start()
    }
}
