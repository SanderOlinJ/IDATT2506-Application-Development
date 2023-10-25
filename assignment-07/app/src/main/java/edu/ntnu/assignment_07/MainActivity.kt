package edu.ntnu.assignment_07

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import edu.ntnu.assignment_07.managers.FileManager
import edu.ntnu.assignment_07.ui.theme.Assignment07Theme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fileManager = FileManager(this)
        val movies = fileManager.readFileFromResFolder(R.raw.movies)
        fileManager.write(movies)
    }
}
