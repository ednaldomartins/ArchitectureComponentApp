package com.example.architecturecomponentapp

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.architecturecomponentapp.data.dao.FilmDao
import com.example.architecturecomponentapp.data.database.local.FilmDatabase
import com.example.architecturecomponentapp.domain.entity.FilmData
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.IOException

@Config(manifest = "src/main/AndroidManifest.xml", sdk = [26])
@RunWith(RobolectricTestRunner::class)
class FilmDatabaseTest {

    private lateinit var filmDao: FilmDao
    private lateinit var db: FilmDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, FilmDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        filmDao = db.filmDao
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetFilm() {
        //  inserindo filme teste
        val film = FilmData(id = 200L)
        filmDao.insertFilm(film)

        // tentar pegar filme teste inserido no DB
        val getFilm = filmDao.get(200L)
        assertEquals(getFilm.id, film.id)
    }

    @Test
    @Throws(Exception::class)
    fun insertAndClearDatabase() {
        // inserindo filme teste
        val film = FilmData(id = 2L)
        filmDao.insertFilm(film)
        assertEquals(filmDao.getFilmsCount(), 1)

        // limpar database
        filmDao.clear()
        assertEquals(filmDao.getFilmsCount(), 0)
    }

    @Test
    @Throws(Exception::class)
    fun insertAndUpdateFilm() {
        // inserindo filme teste
        val film = FilmData(id = 20L, title = "SEM NOME")
        filmDao.insertFilm(film)
        assertEquals(filmDao.get(20L).title, "SEM NOME")

        // replace
        val getFilm = filmDao.get(20L)
        getFilm.title = "COM NOME"
        filmDao.updateFilm(getFilm)
        assertEquals( filmDao.get(20L).title, "COM NOME" )
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }
}