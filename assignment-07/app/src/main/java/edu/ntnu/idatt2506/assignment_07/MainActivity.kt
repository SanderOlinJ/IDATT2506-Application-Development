package edu.ntnu.idatt2506.assignment_07

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import edu.ntnu.idatt2506.assignment_07.databinding.ActivityMainBinding
import edu.ntnu.idatt2506.assignment_07.managers.AppPreferenceManager
import edu.ntnu.idatt2506.assignment_07.managers.FileManager
import edu.ntnu.idatt2506.assignment_07.service.Database
import java.util.ArrayList

/**
 * MainActivity is the primary activity of the application that displays
 * movie-related information based on user selections from the options menu.
 * The activity interacts with the Database class to fetch specific data,
 * utilizes the FileManager to read movie data from a resource file and
 * save it, and employs the AppPreferenceManager to manage and update UI preferences.
 */
class MainActivity : AppCompatActivity() {

    // Instance of the Database for database operations.
    private lateinit var db: Database

    // Binding instance for accessing the view elements.
    private lateinit var activityMain: ActivityMainBinding

    /**
     * Called when the activity is starting, initializes the activity views,
     * sets up the FileManager, AppPreferenceManager, and the Database.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using view binding and set the content view.
        activityMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMain.root)

        // Initialize FileManager and read movie data from resource folder.
        val fileManager = FileManager(this)
        val movies = fileManager.readFileFromResFolder(R.raw.movies)
        fileManager.write(movies)

        // Initialize AppPreferenceManager and update background color preference.
        AppPreferenceManager(this).updateBackgroundColor()

        // Initialize the database instance.
        db = Database(this)
    }

    /**
     * Called when the activity will start interacting with the user.
     * Updates the background color using AppPreferenceManager.
     */
    override fun onResume() {
        super.onResume()
        AppPreferenceManager(this).updateBackgroundColor()
    }

    /**
     * Formats and displays the results in the activity's TextView.
     *
     * @param list List of strings to be displayed.
     * @param sortingBy Information about the type of list being displayed.
     */
    private fun showResults(list: ArrayList<String>, sortingBy: String) {
        val res = StringBuffer("")
        for (s in list) res.append("$s\n")
        activityMain.result.text = res
        activityMain.sortingBy.text = sortingBy
    }

    /**
     * Initialize the contents of the activity's standard options menu.
     * Populates the menu with predefined options.
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.settings, menu)
        menu.add(0, 1, 0, "All movies")
        menu.add(0, 2, 0, "All directors")
        menu.add(0, 3, 0, "All actors")
        menu.add(0, 4, 0, "All movies and directors")
        menu.add(0, 5, 0, "Movies by 'Edgar Wright'")
        menu.add(0, 6, 0, "Director of 'Overlord'")
        menu.add(0, 7, 0, "Actors in 'Fury'")
        menu.add(0, 8, 0, "Movies with Brad Pitt")
        menu.add(0, 9, 0, "Actors who have worked with 'Shane Black'")
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Called when an item in the options menu is selected.
     * Defines actions to be taken for each menu item selection.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> startActivity(Intent(this, SettingsActivity::class.java))
            1             -> showResults(db.allMovies, "All movies:")
            2             -> showResults(db.allDirectors, "All directors:")
            3             -> showResults(db.allActors, "All actors:")
            4             -> showResults(db.allMoviesAndDirectors, "All movies w/ directors:")
            5             -> showResults(db.getMoviesByDirector("Edgar Wright"),
                "Movies by Edgar Wright")
            6             -> showResults(db.getDirectorByMovie("Overlord"),
                "Director of 'Overlord'")
            7             -> showResults(db.getActorsByMovie("Fury"), "Actors in Fury:")
            8             -> showResults(db.getMoviesByActor("Brad Pitt"),
                "Movies w/ Brad pitt")
            9             -> showResults(db.getActorsByDirector("Shane Black"),
                "Actors who have worked w/ Shane Black")
            else          -> return false
        }
        return super.onOptionsItemSelected(item)
    }
}
