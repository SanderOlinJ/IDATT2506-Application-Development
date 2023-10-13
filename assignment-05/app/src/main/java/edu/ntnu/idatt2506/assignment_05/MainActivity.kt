package edu.ntnu.idatt2506.assignment_05

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONArray


const val URL = "https://bigdata.idi.ntnu.no/mobil/tallspill.jsp"

class MainActivity : AppCompatActivity() {

    private lateinit var startButton: Button
    private lateinit var nameField: EditText
    private lateinit var cardNumberField: EditText
    private lateinit var errorField: TextView
    private val network: HttpWrapper = HttpWrapper(URL)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startButton = findViewById(R.id.start_playing_button)
        nameField = findViewById(R.id.name)
        cardNumberField = findViewById(R.id.card_number)
        errorField = findViewById(R.id.error)

        startButton.setOnClickListener {
            if (checkValidInput()) {
                performRequest(requestParameters())
            }
        }
    }

    private fun checkValidInput(): Boolean {
        val name = nameField.text.toString()
        val cardNumber = cardNumberField.text.toString()

        if (name.isEmpty() || cardNumber.isEmpty()) {
            errorField.text = getString(R.string.empty_fields_error)
            errorField.visibility = View.VISIBLE

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                errorField.visibility = View.GONE
            }, 5000)
            Log.d("checkValidInput()","Validation fail!")

            return false
        } else {
            errorField.visibility = View.GONE
            Log.d("checkValidInput()","Validation success!")

            return true
        }
    }

    private fun requestParameters(): Map<String, String> {
        val name = nameField.text.toString()
        val cardNumber = cardNumberField.text.toString()
        return mapOf(
            "navn" to name,
            "kortnummer" to cardNumber,
        )
    }

    private fun performRequest(parameterList: Map<String, String>) {
        CoroutineScope(Dispatchers.IO).launch {
            val response: String = try {
                network.get(parameterList)
            } catch (e: Exception) {
                Log.e("performRequest()", e.message!!)
                e.toString()
            }
            Log.d("performRequest()", response)
            MainScope().launch {
                errorField.text = formatJsonString(response)
            }
        }
    }

    private fun formatJsonString(str: String): String {
        return try {
            JSONArray(str).toString(4)
        } catch (e: Exception) {
            Log.e("formatJsonString()", e.toString())
            e.message!!
        }
    }
}