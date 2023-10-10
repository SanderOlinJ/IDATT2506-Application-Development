package edu.ntnu.idatt2506.assignment_04.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import edu.ntnu.idatt2506.assignment_04.data_object.Movie
import edu.ntnu.idatt2506.assignment_04.R

class ListFragment : Fragment() {

    private lateinit var list: ListView
    private var listener: MovieSelectedListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.list_fragment, container, false)
        list = view.findViewById(R.id.items)
        val items = listener?.getMovieList() ?: emptyList()

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,
            items.map { it.title })
        list.adapter = adapter

        list.setOnItemClickListener { _, _, position, _ ->
            listener?.onMovieSelected(items[position])
        }
        return view
    }

    interface MovieSelectedListener {
        fun onMovieSelected(movie : Movie)
        fun getMovieList(): List<Movie>
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MovieSelectedListener) {
            listener = context
        }
    }

}