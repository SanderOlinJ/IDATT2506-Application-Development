package edu.ntnu.idatt2506.assignment_04.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import edu.ntnu.idatt2506.assignment_04.data_object.Movie
import edu.ntnu.idatt2506.assignment_04.R

class DetailFragment : Fragment() {
    private lateinit var image: ImageView
    private lateinit var description: TextView
    private lateinit var currentMovie: Movie

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.detail_fragment, container, false)
        image = view.findViewById(R.id.image)
        description = view.findViewById(R.id.description)

        if (savedInstanceState != null) {
            currentMovie = savedInstanceState.getParcelable("currentMovie") ?: Movie("", 0, "")
            showMovieDetails(currentMovie)
        }

        return view
    }

    fun showMovieDetails(movie: Movie) {
        currentMovie = movie
        image.setImageResource(movie.image)
        description.text = movie.description
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the currentMovie to restore it later
        outState.putParcelable("currentMovie", currentMovie)
    }
}