package com.example.architecturecomponentapp

import android.content.Context
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.ednaldomartins.architecturecomponentapp.data.dao.FilmDao
import com.ednaldomartins.architecturecomponentapp.data.entity.Film
import com.example.architecturecomponentapp.data.database.FilmDatabase
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.io.IOException

/**
 * documentacao: http://d.android.com/tools/testing
 */
@RunWith(AndroidJUnit4::class)
class FilmeDatabaseTest {

    private lateinit var filmDao: FilmDao
    private lateinit var filmDatabase: FilmDatabase

    @Before
    fun createDatabase() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        filmDatabase = Room.databaseBuilder(context.applicationContext, FilmDatabase::class.java, "film_database")
            .allowMainThreadQueries()
            .build()
        filmDao = filmDatabase.filmDao
    }

    @Test
    @Throws(IOException::class)
    fun insertFilm() {
        val film = Film()
        filmDao.insertFilm(film)
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase () {
        filmDatabase.close()
    }
}
