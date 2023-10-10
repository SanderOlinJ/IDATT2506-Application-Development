package edu.ntnu.idatt2506.assignment_04

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import edu.ntnu.idatt2506.assignment_04.data_object.Movie
import edu.ntnu.idatt2506.assignment_04.fragment.DetailFragment
import edu.ntnu.idatt2506.assignment_04.fragment.ListFragment

class MainActivity : AppCompatActivity(), ListFragment.MovieSelectedListener {

    private var selectedItem = 0
    private lateinit var detailFragment: DetailFragment
    private val items = listOf(
        Movie("The Nice Guys", R.drawable.the_nice_guys,
            "In 1970s Los Angeles, a mismatched pair of private eyes investigate " +
                    "a missing girl and the mysterious death of a porn star."),
        Movie("Scott Pilgrim", R.drawable.scott_pilgrim,
            "In a magically realistic version of Toronto, a young man must defeat his " +
                    "new girlfriend's seven evil exes one by one in order to win her heart."),
        Movie("Fury", R.drawable.fury,
            "A grizzled tank commander makes tough decisions as he and his crew " +
                    "fight their way across Germany in April, 1945."),
        Movie("Overlord", R.drawable.overlord,
            "A small group of American soldiers find horror behind enemy lines" +
                    " on the eve of D-Day.")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            val listFragment = ListFragment()
            detailFragment = DetailFragment()

            supportFragmentManager
                .beginTransaction()
                .add(R.id.list, listFragment)
                .add(R.id.detail, detailFragment)
                .commit()
        } else {
        selectedItem = savedInstanceState.getInt("selectedItem", 0)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("selectedItem", selectedItem)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun getMovieList(): List<Movie> {
        return items
    }

    override fun onMovieSelected(movie : Movie) {
        detailFragment.showMovieDetails(movie)
        selectedItem = items.indexOf(movie)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.previous -> navigateToSelectedItem(-1)
            R.id.next -> navigateToSelectedItem(1)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigateToSelectedItem(offset: Int) {
        val newIndex = selectedItem + offset
        if (newIndex >= 0 && newIndex < items.size) {
            selectedItem = newIndex
            detailFragment.showMovieDetails(items[selectedItem])
        }
    }
}