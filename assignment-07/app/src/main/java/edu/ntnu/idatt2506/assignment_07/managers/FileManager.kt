package edu.ntnu.idatt2506.assignment_07.managers

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.io.*

/**
 * Manages file operations for movies stored in a JSON file.
 *
 * @property activity The activity from which the file manager is invoked.
 */
class FileManager(private val activity: AppCompatActivity) {

    // The name of the file where movies are stored.
    private val filename: String = "movies.json"

    // Directory where the file is to be located
    // "data/data/edu.ntnu.idatt2506.assignment_07/files"
    private var dir: File = activity.filesDir

    // Reference to the file:
    // "data/data/edu.ntnu.idatt2506.assignment_07/files/movies.json"
    private var file: File = File(dir, filename)

    /**
     * Writes a string to the 'movies.json' file.
     *
     * @param str The string content to write.
     */
    fun write(str: String) {
        PrintWriter(file).use { writer ->
            writer.println(str)
        }
    }

    /**
     * Reads the content of a file located in the app's resources folder.
     *
     * @param fileId The resource ID of the file to read.
     * @return The content of the file as a string.
     */
     fun readFileFromResFolder(fileId: Int): String {
        val content = StringBuffer("")
        Log.d("readFileFromResFolder", "Reading file")
        try {
            val inputStream: InputStream = activity.resources.openRawResource(fileId)
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                var line = reader.readLine()
                while (line != null) {
                    Log.d("readFileFromResFolder", line)
                    content.append(line)
                    content.append("\n")
                    line = reader.readLine()
                }

            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        Log.d("readFileFromResFolder", "Reading complete")
        return content.toString()
    }
}