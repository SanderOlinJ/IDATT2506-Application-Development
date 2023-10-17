package edu.ntnu.idatt2506.assignment_06_host

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var sendButton: Button
    private lateinit var messageToSend: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val receivedMessageTextView = findViewById<TextView>(R.id.received_message)
        val sentMessageTextView = findViewById<TextView>(R.id.sent_message)
        val server = Server(receivedMessageTextView, sentMessageTextView)
        server.start()

        sendButton = findViewById(R.id.send_button)
        messageToSend = findViewById(R.id.message_to_send)

        sendButton.setOnClickListener {
            Log.d("onClick", messageToSend.text.toString())
            CoroutineScope(Dispatchers.IO).launch {
                server.sendToClient(messageToSend.text.toString())
            }
        }
    }
}
