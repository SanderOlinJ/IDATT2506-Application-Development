package edu.ntnu.assignment_07.managers

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import java.io.*

/**
 * Just contains basic code snippets relevant for reading from/to different files
 */
class FileManager(private val activity: AppCompatActivity) {

    private val filename: String = "movies.json"
    private var dir: File = activity.filesDir
    private var file: File = File(dir, filename)
    private var externalDir: File? = activity.getExternalFilesDir(null)
    private var externalFile = File(externalDir, filename)


    fun write(str: String) {
        PrintWriter(file).use { writer ->
            writer.println(str)
        }
    }

    fun readLine(): String? {
        BufferedReader(FileReader(file)).use { reader ->
            return reader.readLine()
        }
    }

    /**
     * Open file: *res/raw/id.txt*
     *
     * @param fileId R.raw.filename
     */
     fun readFileFromResFolder(fileId: Int): String {
        val content = StringBuffer("")
        Log.d("readFileFromResFolder", "Reading file")
        try {
            val inputStream: InputStream = activity.resources.openRawResource(fileId)
            val reader = BufferedReader(InputStreamReader(inputStream)).use { reader ->
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