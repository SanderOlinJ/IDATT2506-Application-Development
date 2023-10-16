package edu.ntnu.idatt2506.assignment_05

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

const val URL = "https://bigdata.idi.ntnu.no/mobil/tallspill.jsp"

class MainActivity : AppCompatActivity() {

    private lateinit var submitInfoButton: Button
    private lateinit var nameField: EditText
    private lateinit var cardNumberField: EditText
    private lateinit var errorField: TextView
    private lateinit var numberGuess: EditText
    private lateinit var submitGuessButton: Button
    private val network: HttpWrapper = HttpWrapper(URL)
    private var messageFromServer: String = ""
    private var numberOfGuesses: Int = 0
    private var validInfo: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        submitInfoButton = findViewById(R.id.submit_info_button)
        nameField = findViewById(R.id.name)
        cardNumberField = findViewById(R.id.card_number)
        errorField = findViewById(R.id.error)
        numberGuess = findViewById(R.id.number_guess)
        submitGuessButton = findViewById(R.id.submit_guess_button)
        val scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.button_scale_animation)

        submitInfoButton.setOnClickListener { v ->
            submitInfo()
            v.startAnimation(scaleAnimation)
        }

        submitGuessButton.setOnClickListener { v ->
            submitGuess()
            v.startAnimation(scaleAnimation)
        }
    }

    private fun checkValidInput(): Boolean {
        val name = nameField.text.toString()
        val cardNumber = cardNumberField.text.toString()

        return if (name.isEmpty() || cardNumber.isEmpty()) {
            showMessage(getString(R.string.empty_fields_error), true)
            Log.d("checkValidInput()","Validation fail!")
            false
        } else {
            errorField.visibility = View.GONE
            Log.d("checkValidInput()","Validation success!")
            true
        }
    }

    private fun submitInfoParameters(): Map<String, String> {
        val name = nameField.text.toString().replace(" ", "")
        val cardNumber = cardNumberField.text.toString()
        Log.d("submitInfoParameters()", "Input name: $name")
        Log.d("submitInfoParameters()", "Input card number: $cardNumber")
        return mapOf(
            "navn" to name,
            "kortnummer" to cardNumber,
        )
    }

    private fun submitGuessParameters(): Map<String, String> {
        val guess = numberGuess.text.toString()
        Log.d("submitGuessParameters()", "User's guess: $guess")
        return mapOf(
            "tall" to guess
        )
    }

    private fun performRequest(parameterList: Map<String, String>, callback: (String?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response: String = try {
                network.get(parameterList)
            } catch (e: Exception) {
                Log.e("performRequest()", "Error from server:\n$e.message!!")
                e.toString()
            }
            Log.d("performRequest()", "Response from server:\n$response")
            messageFromServer = response
            MainScope().launch {
                messageFromServer = response
                callback(response)
            }
        }
    }

    private fun submitInfo() {
        clearMessage()
        if (!checkValidInput()) {
            return
        }
        performRequest(submitInfoParameters()) { response ->
            if (response != null) {
                val trimmedResponse = response.replace("null", "").trim()
                if (response.contains("Feil", ignoreCase = true)) {
                    Log.d("submitInfo()", "Response contains 'Feil': $response")
                    showMessage(trimmedResponse, true)
                    validInfo = false
                } else {
                    numberOfGuesses = 0
                    validInfo = true
                    showMessage(trimmedResponse, false)
                    Log.d("submitInfo()", "Received response from server: $response")
                }
            } else {
                Log.d("submitInfo()", "Failed to receive response from server")
            }
        }
    }

    private fun submitGuess() {
        clearMessage()
        if (!validInfo) {
            showMessage(getString(R.string.invalid_info), true)
            Log.d("submitGuess()","Validation fail!")
            return
        }
        if (numberOfGuesses >= 3) {
            showMessage(getString(R.string.exceeded_guesses), true)
            Log.d("submitGuess()","Validation fail!")
            validInfo = false
            return
        }

        performRequest(submitGuessParameters()) { response ->
            if (response == null) {
                Log.d("submitGuess()", "Failed to receive response from server")
                return@performRequest
            }

            if (response.contains("Tallet er ikke på riktig form")) {
                showMessage(getString(R.string.wrong_guess_format), true)
                Log.d("submitGuess()","Validation fail!")
                return@performRequest
            }

            val trimmedResponse = response.replace("null", "").trim()
            showMessage(trimmedResponse, false)

            numberOfGuesses++
            Log.d("submitGuess()", "Received response from server: $response")
        }
    }

    private fun clearMessage() {
        errorField.text = ""
        errorField.visibility = View.GONE
    }

    private fun showMessage(message: String, error: Boolean) {
        errorField.text = message
        errorField.visibility = View.VISIBLE

        if (error) {
            val textColor = ContextCompat.getColor(this, R.color.red)
            errorField.setTextColor(textColor)
        } else {
            val textColor = ContextCompat.getColor(this, R.color.green)
            errorField.setTextColor(textColor)
        }
    }
}