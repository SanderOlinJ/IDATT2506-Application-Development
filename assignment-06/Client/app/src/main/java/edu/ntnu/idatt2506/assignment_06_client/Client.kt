package edu.ntnu.idatt2506.assignment_06_client

import android.util.Log
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    private var server: Socket? = null

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
                withContext(Dispatchers.IO) {
                    Socket(SERVER_IP, SERVER_PORT).use { socket: Socket ->
                        server = socket
                        receivedMessage = "Connected to host:\n$socket"
                        sendToServer("Hello Host!")
                        readFromServer()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                receivedMessage = e.message
            }
        }
    }
    private fun readFromServer() {
        try {
            val reader =
                BufferedReader(InputStreamReader(server!!.getInputStream()))
            while (true) {
                val message = reader.readLine()
                if (message == null) {
                    server = null
                    Log.d("readFromServer()", "Server has disconnected.")
                    break
                }
                Log.d("readFromServer()", "Server said:\n$message")
                receivedMessage = message
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("readFromServer()", "Error reading from server: ${e.message}")
        }
    }
    fun sendToServer(message: String) {
        try {
            if (server != null && !server!!.isClosed) {
                val writer = PrintWriter(server!!.getOutputStream(), true)
                writer.println(message)
                Log.d("sendToServer()", "Sent to server:\n$message")
                sentMessage = message
            } else {
                Log.e("sendToServer()", "Server not connected")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("sendToServer()", "Error sending message: ${e.message}")
        }
    }
}