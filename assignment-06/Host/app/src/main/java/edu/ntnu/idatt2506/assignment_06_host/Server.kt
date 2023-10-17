package edu.ntnu.idatt2506.assignment_06_host

import android.util.Log
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket

class Server(
    private val receivedMessageTextView: TextView,
    private val sentMessageTextView: TextView,
    private val PORT: Int = 12345,
) {

    private var client: Socket? = null

    private var receivedMessage: String? = ""
        set(str) {
            MainScope().launch {
                receivedMessageTextView.text = str
                field = str
            }
        }

    private var sentMessage: String? = ""
        set(str) {
            MainScope().launch {
                sentMessageTextView.text = str
                field = str
            }
        }

    fun start() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                receivedMessage = "Starting Host ..."
                withContext(Dispatchers.IO) {
                    ServerSocket(PORT).use { serverSocket: ServerSocket ->
                        receivedMessage = "ServerSocket created, waiting for client connection...."
                        client = serverSocket.accept()
                        receivedMessage = "Client connected to:\n$client"
                        sendToClient("Welcome Client!")
                        readFromClient()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                receivedMessage = e.message
            }
        }
    }

    private fun readFromClient() {
        val reader = BufferedReader(InputStreamReader(client?.getInputStream()))
        try {
            while (true) {
                val message = reader.readLine()
                if (message == null) {
                    client = null
                    Log.d("readFromClient()", "Client has disconnected.")
                    break
                }
                Log.d("readFromClient()", "Client said:\n$message")
                receivedMessage = message
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("readFromClient()", "Error reading from client: ${e.message}")
        }
    }

    fun sendToClient(message: String) {
        try {
            if (client != null && client!!.isConnected) {
                val writer = PrintWriter(client!!.getOutputStream(), true)
                Log.d("sendToClient()", message)
                writer.println(message)
                Log.d("sendToClient()", "Sent to client:\n$message")
                sentMessage = message
            } else {
                Log.e("sendToClient()", "Client not connected")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("sendToClient()", "Error sending message: ${e.message}")
        }
    }
}