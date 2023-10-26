package edu.ntnu.idatt2506.assignment_07.managers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Provides a centralized management for database operations related to movies, directors, and actors.
 *
 * @property context The context from which the database manager is invoked.
 */
open class DatabaseManager(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        // Database version and name
        const val DATABASE_NAME = "MovieDatabase"
        const val DATABASE_VERSION = 1

        const val ID = "_id"

        // Movie table and its columns
        const val TABLE_MOVIE = "MOVIE"
        const val MOVIE_TITLE = "title"
        const val MOVIE_ID = "movie_id"

        // Director table and its columns
        const val TABLE_DIRECTOR = "DIRECTOR"
        const val DIRECTOR_NAME = "name"
        const val DIRECTOR_ID = "director_id"

        // Actor table and its columns
        const val TABLE_ACTOR = "ACTOR"
        const val ACTOR_NAME = "name"
        const val ACTOR_ID = "actor_ID"

        // Table to associate movies with actors
        const val TABLE_MOVIE_ACTOR = "MOVIE_ACTOR"

        // Join statements to link movies and actors
        val JOIN_MOVIE_ACTOR = arrayOf(
            "$TABLE_MOVIE.$ID=$TABLE_MOVIE_ACTOR.$MOVIE_ID",
            "$TABLE_ACTOR.$ID=$TABLE_MOVIE_ACTOR.$ACTOR_ID"

        )
    }

    /**
     * Called when the database is created for the first time.
     * This method creates the required tables.
     */
    override fun onCreate(
        db: SQLiteDatabase)
    {
        db.execSQL(
            """create table $TABLE_MOVIE (
						$ID integer primary key autoincrement, 
						$MOVIE_TITLE text unique not null,
                        $DIRECTOR_ID numeric,
                        FOREIGN KEY($DIRECTOR_ID) REFERENCES $TABLE_DIRECTOR($ID)
						);"""
        )
        db.execSQL(
            """create table $TABLE_DIRECTOR (
						$ID integer primary key autoincrement, 
						$DIRECTOR_NAME text unique not null
						);"""
        )
        db.execSQL(
            """create table $TABLE_ACTOR (
						$ID integer primary key autoincrement, 
						$ACTOR_NAME text unique not null
						);"""
        )
        db.execSQL(
            """create table $TABLE_MOVIE_ACTOR (
                        $ID integer primary key autoincrement,
                        $MOVIE_ID numeric,
                        $ACTOR_ID numeric,
                        FOREIGN KEY($MOVIE_ID) REFERENCES $TABLE_MOVIE($ID), 
						FOREIGN KEY($ACTOR_ID) REFERENCES $TABLE_ACTOR($ID)
                ); """
        )
    }

    /**
     * Called when the database version changes.
     * This method drops the existing tables and calls [onCreate] to recreate them.
     */
    override fun onUpgrade(
        db: SQLiteDatabase,
        arg1: Int,
        arg2: Int)
    {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MOVIE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DIRECTOR")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ACTOR")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MOVIE_ACTOR")
        onCreate(db)
    }

    /**
     * Drops all tables, effectively clearing the entire database.
     */
    fun clear() {
        writableDatabase.use { onUpgrade(it, 0, 0) }
    }

    /**
     * Inserts a movie entry, its director, and associated actors.
     * If any of these entities already exists, they will not be duplicated.
     */
    fun insert(
        movie: String,
        director: String,
        actors: List<String>)
    {
        writableDatabase.use { database ->
            val movieId = insertValueIfNotExists(database, TABLE_MOVIE, MOVIE_TITLE, movie)
            val directorId =
                insertValueIfNotExists(database, TABLE_DIRECTOR, DIRECTOR_NAME, director)

            val updateValues = ContentValues().apply {
                put(DIRECTOR_ID, directorId)
            }
            database
                .update(TABLE_MOVIE, updateValues, "$ID = ?", arrayOf(movieId.toString()))

            val actorIds = actors.map { actorName ->
                insertValueIfNotExists(database, TABLE_ACTOR, ACTOR_NAME, actorName)
            }

            linkMovieAndActors(database, movieId, actorIds)
        }
    }

    /**
     * Associates the given movie with a list of actors.
     */
    private fun linkMovieAndActors(
        database: SQLiteDatabase,
        movieId: Long,
        actorIds: List<Long>)
    {
        val values = ContentValues()
        actorIds.forEach { actorId ->
            values.clear()
            values.put(MOVIE_ID, movieId)
            values.put(ACTOR_ID, actorId)
            database.insert(TABLE_MOVIE_ACTOR, null, values)
        }
    }

    /**
     * Inserts a value into a table if it does not already exist.
     * Returns the ID of the existing or newly inserted entry.
     */
    private fun insertValueIfNotExists(
        database: SQLiteDatabase,
        table: String,
        field: String,
        value: String
    ): Long {
        query(database, table, arrayOf(ID, field), "$field='$value'").use { cursor ->
            // insert if value doesn't exist
            return if (cursor.count != 0) {
                cursor.moveToFirst()
                cursor.getLong(0)
            } else {
                insertValue(database, table, field, value)
            }
        }
    }

    /**
     * Inserts a value into a table and returns the ID of the newly inserted entry.
     */
    private fun insertValue(
        database: SQLiteDatabase,
        table: String,
        field: String,
        value: String
    ): Long {
        val values = ContentValues()
        values.put(field, value.trim())
        return database.insert(table, null, values)
    }

    /**
     * Performs a query on the given table and columns, optionally with a selection criteria.
     * Returns the results as a list of strings.
     */
    fun performQuery(
        table: String,
        columns: Array<String>,
        selection: String? = null
    ): ArrayList<String> {
        assert(columns.isNotEmpty())
        readableDatabase.use { database ->
            query(database, table, columns, selection).use { cursor ->
                return readFromCursor(cursor, columns.size)
            }
        }
    }

    /**
     * Performs a raw SQL query using given SELECT, FROM, and WHERE clauses.
     * Returns the results as a list of strings.
     */
    fun performRawQuery(
        select: Array<String>,
        from: Array<String>,
        join: Array<String>,
        where: String? = null
    ): ArrayList<String> {
        val query = StringBuilder("SELECT ")
        for ((i, field) in select.withIndex()) {
            query.append(field)
            if (i != select.lastIndex) query.append(", ")
        }

        query.append(" FROM ")
        for ((i, table) in from.withIndex()) {
            query.append(table)
            if (i != from.lastIndex) query.append(", ")
        }

        query.append(" WHERE ")
        for ((i, link) in join.withIndex()) {
            query.append(link)
            if (i != join.lastIndex) query.append(" and ")
        }

        if (where != null) query.append(" and $where")

        readableDatabase.use { db ->
            db.rawQuery("$query;", null).use { cursor ->
                return readFromCursor(cursor, select.size)
            }
        }
    }

    /**
     * Converts a cursor into a list of strings, based on the number of columns to read.
     */
    private fun readFromCursor(
        cursor: Cursor,
        numberOfColumns: Int
    ): ArrayList<String> {
        val result = ArrayList<String>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val item = StringBuilder("")
            for (i in 0 until numberOfColumns) {
                item.append("${cursor.getString(i)} ")
            }
            result.add("$item")
            cursor.moveToNext()
        }
        return result
    }

    /**
     * A helper function to perform a standard query on the database.
     */
    private fun query(
        database: SQLiteDatabase,
        table: String,
        columns: Array<String>,
        selection: String?
    ): Cursor {
        return database.query(table, columns, selection,
            null, null, null, null, null)
    }
}
