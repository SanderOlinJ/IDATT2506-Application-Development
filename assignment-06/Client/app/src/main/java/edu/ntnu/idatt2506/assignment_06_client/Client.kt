package edu.ntnu.idatt2506.assignment_06_client

import android.util.Log
import android.widget.TextView
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class Client (
    private val receivedMessageTextView: TextView,
    private val sentMessageTextView: TextView,
    private val SERVER_IP: String = "10.0.2.2",
    private val SERVER_PORT: Int = 12345,
) {

    private var receivedMessage: String? = ""
        set(str) {
            MainScope().launch { receivedMessageTextView.text = str }
            field = str
        }

    private var sentMessage: String? = ""
        set(str) {
            MainScope().launch { sentMessageTextView.text = str }
            field = str
        }


    fun start() {
        CoroutineScope(Dispatchers.IO).launch {
            receivedMessage = "Connecting to host..."
            try {
                Socket(SERVER_IP, SERVER_PORT).use { socket: Socket ->
                    receivedMessage = "Connected to host:\n$socket"
                    delay(5000)
                    readFromServer(socket)
                    delay(5000)
                    sendToServer(socket, "Hello Host!")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                receivedMessage = e.message
            }
        }
    }
    private fun readFromServer(socket: Socket) {
        val reader =
            BufferedReader(InputStreamReader(socket.getInputStream()))
        val message = reader.readLine()
        Log.d("readFromServer()", "Server said:\n$message")
        receivedMessage = message
    }
    private fun sendToServer(socket: Socket, message: String) {
        val writer = PrintWriter(socket.getOutputStream(), true)
        writer.println(message)
        Log.d("sendToServer()", "Sent to server:\n$message")
        sentMessage = message
    }
}