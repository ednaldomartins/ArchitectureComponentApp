package com.example.architecturecomponentapp.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ednaldomartins.architecturecomponentapp.data.dao.FilmDao
import com.ednaldomartins.architecturecomponentapp.data.entity.Film
import kotlinx.coroutines.*

class FilmViewModel (val databaseDao: FilmDao, app: Application) : AndroidViewModel (app) {

    private var viewModelJob = Job()
    private val uiCoroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val films = databaseDao.filmList()

    //TEMP
    var lastFilm = MutableLiveData<Film?>()

    init {
        listFilms()
    }

    // captura ultimo filme chamando funcao de suspencao
    // DEVE SER ALTERADO PARA MOSTRAR LISTA COMPLETA QUANDO EU FIZER O VIEW
    private fun listFilms() {
        uiCoroutineScope.launch { lastFilm.value = getLastFilmFromDatabase() }
    }

    // funcao de suspensao para retornar ultimo filme inserido
    private suspend fun getLastFilmFromDatabase(): Film? {
        return withContext(Dispatchers.IO){ databaseDao.getLastFilm() }
    }

    // inserir filme chamando funcao de suspensao
    fun insertFilm(name: String, year: Short) {
        uiCoroutineScope.launch {
            val film = Film(name = name, year = year)
            insert(film)
            lastFilm.value = getLastFilmFromDatabase()
        }
    }

    //funcao de suspensao para inserir film no BD
    private suspend fun insert (film: Film) {
        withContext(Dispatchers.IO) { databaseDao.insertFilm(film) }
    }

    override fun onCleared() {
        super.onCleared()
        // cancelar raiz das coroutines
        viewModelJob.cancel()
    }
}