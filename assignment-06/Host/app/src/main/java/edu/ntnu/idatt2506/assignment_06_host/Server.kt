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
import java.util.concurrent.ConcurrentHashMap

class Server(
    private val receivedMessageTextView: TextView,
    private val sentMessageTextView: TextView,
    private val PORT: Int = 12345,
) {

    private val clients = ConcurrentHashMap<Socket, PrintWriter>()
    private val scope = CoroutineScope(Dispatchers.IO)

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
        scope.launch {
            try {
                receivedMessage = "Starting Host ..."
                withContext(Dispatchers.IO) {
                    ServerSocket(PORT).use { serverSocket: ServerSocket ->
                        receivedMessage = "ServerSocket created, waiting for client connection...."
                        var clientIndex = 0
                        while (true) {
                            val clientSocket = serverSocket.accept()
                            receivedMessage = "Client connected to:\n$clientSocket"
                            val clientWriter =
                                PrintWriter(clientSocket.getOutputStream(), true)
                            clients[clientSocket] = clientWriter
                            sendToClient("Welcome Client!", clientWriter)
                            scope.launch {
                                readFromClient(clientSocket, clientIndex)
                            }
                            clientIndex++
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                receivedMessage = e.message
            }

            scope.launch {
                while (true) {
                    val disconnectedClients = clients.entries.filter {
                        it.key.isClosed || !it.key.isConnected }
                    for (entry in disconnectedClients) {
                        clients.remove(entry.key)
                        Log.d("handleDisconnectedClients()",
                            "Client has disconnected: ${entry.key}")
                    }
                    delay(5000)
                }
            }
        }
    }

    private fun readFromClient(client: Socket, clientIndex: Int) {
        val reader = BufferedReader(InputStreamReader(client.getInputStream()))
        try {
            while (true) {
                val message = reader.readLine()
                if (message == null) {
                    clients.remove(client)
                    Log.d("readFromClient()", "Client $clientIndex has disconnected.")
                    break
                }
                Log.d("readFromClient()", "Client $clientIndex said:\n$message")
                receivedMessage = "Client $clientIndex:\n$message"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("readFromClient()", "Error reading from client: ${e.message}")
        }
    }

    private fun sendToClient(message: String, clientWriter: PrintWriter) {
        try {
            clientWriter.println(message)
            Log.d("sendToClient()", "Sent to client:\n$message")
            sentMessage = message
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("sendToClient()", "Error sending message: ${e.message}")
        }
    }

    fun sendToClients(message: String) {
        for (clientWriter in clients.values) {
            sendToClient(message, clientWriter)
        }
    }
}