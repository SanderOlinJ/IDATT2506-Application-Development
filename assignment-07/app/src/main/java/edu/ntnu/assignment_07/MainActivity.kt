package edu.ntnu.assignment_07

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import edu.ntnu.assignment_07.databinding.ActivityMainBinding
import edu.ntnu.assignment_07.managers.FileManager
import edu.ntnu.assignment_07.service.Database
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var db: Database
    private lateinit var activityMain: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMain.root)

        val fileManager = FileManager(this)
        val movies = fileManager.readFileFromResFolder(R.raw.movies)
        fileManager.write(movies)

        db = Database(this)
    }

    private fun showResults(list: ArrayList<String>, sortingBy: String) {
        val res = StringBuffer("")
        for (s in list) res.append("$s\n")
        activityMain.result.text = res
        activityMain.sortingBy.text = sortingBy
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.settings, menu)
        menu.add(0, 1, 0, "All movies")
        menu.add(0, 2, 0, "All directors")
        menu.add(0, 3, 0, "All actors")
        menu.add(0, 4, 0, "All movies and directors")
        menu.add(0, 5, 0, "Actors in 'Fury'")
        menu.add(0, 6, 0, "Movies with Brad Pitt")
        menu.add(0, 7, 0, "Movies by Edgar Wright")
        menu.add(0,8,0, "Actors who have worked with 'Shane Black'")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> startActivity(Intent("inft2501.leksjon_07.SettingsActivity"))
            1             -> showResults(db.allMovies, "All movies:")
            2             -> showResults(db.allDirectors, "All directors:")
            3             -> showResults(db.allActors, "All actors:")
            4             -> showResults(db.allMoviesAndDirectors, "All movies w/ directors:")
            5             -> showResults(db.getActorsByMovie("Fury"), "Actors in Fury:")
            6             -> showResults(db.getMoviesByActor("Brad Pitt"),
                "Movies w/ Brad pitt")
            7             -> showResults(db.getMoviesByDirector("Edgar Wright"),
                "Movies by Edgar Wright")
            8             -> showResults(db.getActorsByDirector("Shane Black"),
                "Actors who have worked w/ Shane Black")
            else          -> return false
        }
        return super.onOptionsItemSelected(item)
    }
}
