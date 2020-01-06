package com.example.architecturecomponentapp

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.example.architecturecomponentapp.data.dao.FilmDao
import com.example.architecturecomponentapp.data.entity.FilmData
import com.example.architecturecomponentapp.data.database.local.FilmDatabase
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

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
        val film = FilmData()
        filmDao.insertFilm(film)
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase () {
        filmDatabase.close()
    }
}
