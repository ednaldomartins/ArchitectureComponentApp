package com.example.architecturecomponentapp.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import kotlinx.coroutines.*

import com.example.architecturecomponentapp.data.dao.FilmDao
import com.example.architecturecomponentapp.data.database.remote.Api
import com.example.architecturecomponentapp.data.database.remote.FilmsApi
import com.example.architecturecomponentapp.data.entity.Film
import com.example.architecturecomponentapp.data.entity.MovieList

class FilmViewModel (val databaseDao: FilmDao, app: Application) : AndroidViewModel (app) {

    // Threads
    private var viewModelJob = Job()
    private val uiCoroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    // Film
    private var rFilm = MutableLiveData<Film>()
    val responseFilm: LiveData<Film> get() = rFilm
    // Film List
    //private var rFilmList = MutableLiveData<List<Film>>()
    //val responseFilmList: LiveData<List<Film>> get() = rFilmList
//movie
    private var rFilmList = MutableLiveData<MovieList>()
    val responseFilmList: LiveData<MovieList> get() = rFilmList

    fun requestFilmApiService () {
        uiCoroutineScope.launch {
            //receber a chamada da API sem bloquear a thread princial
            val getCallDeferred = FilmsApi.retrofitService.callFilmApi("550")//550 = clube da luta
            try {
                val listResult = getCallDeferred.await()
                rFilm.value = listResult
            }
            catch (t: Throwable) {
                rFilm.value = Film(-1,"sem retorno", "-1")
            }

        }
    }

    fun requestFilmListApiService () {
        uiCoroutineScope.launch {
            //receber a chamada da API sem bloquear a thread princial
            val getCallDeferred = FilmsApi.retrofitService.callPopularMovieListApi()
            try {
                val listResult = getCallDeferred.await()
                rFilmList.value = listResult
            }
            catch (t: Throwable) {
                //rFilmList.value = MovieList( LiveData<listOf( Film(title = "Sem retorno: ${t.message}"))> )
            }

        }
    }

    // inserir filme chamando funcao de suspensao
    fun insertFilm(name: String, date: String) {
        uiCoroutineScope.launch { insert( Film(title = name, releaseData = date) ) }
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