package edu.ntnu.assignment_07.service

import android.content.Context
import edu.ntnu.assignment_07.managers.DatabaseManager

class Database(context: Context) : DatabaseManager(context) {

    init {
        try {
            this.clear()
            this.insert("Fury", "David Ayer",
                listOf("Brad Pitt", "Shia LaBeouf"))
            this.insert("Inglorious Basterds", "Quentin Tarantino",
                listOf("Brad Pitt", "Christoph Waltz"))
            this.insert("Scott Pilgrim vs. The World", "Edgar Wright",
                listOf("Michael Cera", "Mary Elizabeth Winstead"))
            this.insert("The Nice Guys", "Shane Black",
                listOf("Ryan Gosling", "Russell Crowe"))
            this.insert("Overlord", "Julius Avery",
                listOf("Wyatt Russell", "Jovan Adepo"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val allMovies: ArrayList<String>
        get() = performQuery(TABLE_MOVIE, arrayOf(MOVIE_NAME))

    val allDirectors: ArrayList<String>
        get() = performQuery(TABLE_DIRECTOR, arrayOf(DIRECTOR_NAME))

    val allActors: ArrayList<String>
        get() = performQuery(TABLE_ACTOR, arrayOf(ACTOR_NAME))

    val allMoviesAndDirectors: ArrayList<String>
        get() {
            val select = arrayOf("$TABLE_MOVIE.$MOVIE_NAME", "$TABLE_DIRECTOR.$DIRECTOR_NAME")
            val from = arrayOf(TABLE_DIRECTOR, TABLE_MOVIE)
            val join = arrayOf("$TABLE_MOVIE.$DIRECTOR_ID=$TABLE_DIRECTOR.$ID")

            return performRawQuery(select, from, join)
        }

    fun getMoviesByDirector(director: String): ArrayList<String> {
        val select = arrayOf("$TABLE_MOVIE.$MOVIE_NAME")
        val from = arrayOf(TABLE_DIRECTOR, TABLE_MOVIE)
        val join = arrayOf("$TABLE_MOVIE.$DIRECTOR_ID = $TABLE_DIRECTOR.$ID")
        val where = "$TABLE_DIRECTOR.$DIRECTOR_NAME='$director'"
        return performRawQuery(select, from, join, where)
    }

    fun getDirectorsByMovie(movie: String): ArrayList<String> {
        val select = arrayOf("$TABLE_DIRECTOR.$DIRECTOR_NAME")
        val from = arrayOf(TABLE_DIRECTOR, TABLE_MOVIE)
        val join = arrayOf("$TABLE_MOVIE.$DIRECTOR_ID=$TABLE_DIRECTOR.$ID")
        val where = "$TABLE_MOVIE.$MOVIE_NAME='$movie'"

        return performRawQuery(select, from, join, where)
    }

    fun getActorsByMovie(movie: String): ArrayList<String> {
        val select = arrayOf("$TABLE_ACTOR.$ACTOR_NAME")
        val from = arrayOf(TABLE_MOVIE, TABLE_ACTOR, TABLE_MOVIE_ACTOR)
        val join = JOIN_MOVIE_ACTOR
        val where = "$TABLE_MOVIE.$MOVIE_NAME='$movie'"

        return performRawQuery(select, from, join, where)
    }

    fun getMoviesByActor(actor: String): ArrayList<String> {
        val select = arrayOf("$TABLE_MOVIE.$MOVIE_NAME")
        val from = arrayOf(TABLE_MOVIE, TABLE_ACTOR, TABLE_MOVIE_ACTOR)
        val join = JOIN_MOVIE_ACTOR
        val where = "$TABLE_ACTOR.$ACTOR_NAME='$actor'"

        return performRawQuery(select, from, join, where)
    }

    fun getActorsByDirector(director: String): ArrayList<String> {
        val select = arrayOf("$TABLE_ACTOR.$ACTOR_NAME")
        val from = arrayOf(TABLE_MOVIE, TABLE_DIRECTOR, TABLE_MOVIE_ACTOR, TABLE_ACTOR)
        val join = arrayOf(
            "$TABLE_MOVIE.$DIRECTOR_ID=$TABLE_DIRECTOR.$ID",
            "$TABLE_MOVIE.$ID=$TABLE_MOVIE_ACTOR.$MOVIE_ID",
            "$TABLE_ACTOR.$ID=$TABLE_MOVIE_ACTOR.$ACTOR_ID"
        )
        val where = "$TABLE_DIRECTOR.$DIRECTOR_NAME='$director'"

        return performRawQuery(select, from, join, where)
    }

}