package edu.ntnu.idatt2506.assignment_07.service

import android.content.Context
import edu.ntnu.idatt2506.assignment_07.managers.DatabaseManager

/**
 * Database class provides methods for querying information about movies, directors, and actors.
 * This class extends DatabaseManager to provide specific movie-related operations.
 * During initialization, a set of movies, actors, and directors are inserted into the database.
 *
 * @property context Context in which the database is being operated on.
 */
class Database(context: Context) : DatabaseManager(context) {

    init {
        try {
            this.clear()
            // Insert sample movies with their directors and actors.
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

    /** Returns a list of all movie names in the database. */
    val allMovies: ArrayList<String>
        get() = performQuery(TABLE_MOVIE, arrayOf(MOVIE_NAME))

    /** Returns a list of all director names in the database. */
    val allDirectors: ArrayList<String>
        get() = performQuery(TABLE_DIRECTOR, arrayOf(DIRECTOR_NAME))

    /** Returns a list of all actor names in the database. */
    val allActors: ArrayList<String>
        get() = performQuery(TABLE_ACTOR, arrayOf(ACTOR_NAME))

    /**
     * Returns a list of movie names along with their respective directors.
     */
    val allMoviesAndDirectors: ArrayList<String>
        get() {
            val select = arrayOf("$TABLE_MOVIE.$MOVIE_NAME", "$TABLE_DIRECTOR.$DIRECTOR_NAME")
            val from = arrayOf(TABLE_DIRECTOR, TABLE_MOVIE)
            val join = arrayOf("$TABLE_MOVIE.$DIRECTOR_ID=$TABLE_DIRECTOR.$ID")

            return performRawQuery(select, from, join)
        }

    /**
     * Returns a list of movie names directed by the specified director.
     *
     * @param director The name of the director.
     */
    fun getMoviesByDirector(director: String): ArrayList<String> {
        val select = arrayOf("$TABLE_MOVIE.$MOVIE_NAME")
        val from = arrayOf(TABLE_DIRECTOR, TABLE_MOVIE)
        val join = arrayOf("$TABLE_MOVIE.$DIRECTOR_ID = $TABLE_DIRECTOR.$ID")
        val where = "$TABLE_DIRECTOR.$DIRECTOR_NAME='$director'"
        return performRawQuery(select, from, join, where)
    }

    /**
     * Returns a list of directors who directed the specified movie.
     *
     * @param movie The name of the movie.
     */
    fun getDirectorByMovie(movie: String): ArrayList<String> {
        val select = arrayOf("$TABLE_DIRECTOR.$DIRECTOR_NAME")
        val from = arrayOf(TABLE_DIRECTOR, TABLE_MOVIE)
        val join = arrayOf("$TABLE_MOVIE.$DIRECTOR_ID=$TABLE_DIRECTOR.$ID")
        val where = "$TABLE_MOVIE.$MOVIE_NAME='$movie'"

        return performRawQuery(select, from, join, where)
    }

    /**
     * Returns a list of actors who acted in the specified movie.
     *
     * @param movie The name of the movie.
     */
    fun getActorsByMovie(movie: String): ArrayList<String> {
        val select = arrayOf("$TABLE_ACTOR.$ACTOR_NAME")
        val from = arrayOf(TABLE_MOVIE, TABLE_ACTOR, TABLE_MOVIE_ACTOR)
        val join = JOIN_MOVIE_ACTOR
        val where = "$TABLE_MOVIE.$MOVIE_NAME='$movie'"

        return performRawQuery(select, from, join, where)
    }

    /**
     * Returns a list of movies where the specified actor has acted.
     *
     * @param actor The name of the actor.
     */
    fun getMoviesByActor(actor: String): ArrayList<String> {
        val select = arrayOf("$TABLE_MOVIE.$MOVIE_NAME")
        val from = arrayOf(TABLE_MOVIE, TABLE_ACTOR, TABLE_MOVIE_ACTOR)
        val join = JOIN_MOVIE_ACTOR
        val where = "$TABLE_ACTOR.$ACTOR_NAME='$actor'"

        return performRawQuery(select, from, join, where)
    }

    /**
     * Returns a list of actors who have worked with the specified director.
     *
     * @param director The name of the director.
     */
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