package edu.ntnu.idatt2506.assignment_06_host

import android.util.Log
import android.widget.TextView
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket

class Server(
    private val receivedMessageTextView: TextView,
    private val sentMessageTextView: TextView,
    private val PORT: Int = 12345
) {


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
                ServerSocket(PORT).use { serverSocket: ServerSocket ->
                    receivedMessage = "ServerSocket created, waiting for client connection...."
                    serverSocket.accept().use { clientSocket: Socket ->
                        receivedMessage = "Client connected to:\n$clientSocket"
                        sendToClient(clientSocket, "Welcome Client!")
                        readFromClient(clientSocket)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                receivedMessage = e.message
            }
        }
    }

    private fun readFromClient(socket: Socket) {
        val reader =
            BufferedReader(InputStreamReader(socket.getInputStream()))
        val message = reader.readLine()
        Log.d("readFromClient()", "Client said:\n$message")
        receivedMessage = message
    }
    private fun sendToClient(socket: Socket, message: String) {
        val writer = PrintWriter(socket.getOutputStream(), true)
        writer.println(message)
        Log.d("sendToClient()", "Sent to client:\n$message")
        sentMessage = message
    }
}