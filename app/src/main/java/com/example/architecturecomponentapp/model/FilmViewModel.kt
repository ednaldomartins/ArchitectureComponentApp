package com.example.architecturecomponentapp.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import kotlinx.coroutines.*

import com.example.architecturecomponentapp.data.dao.FilmDao
import com.example.architecturecomponentapp.data.database.remote.FilmsApi
import com.example.architecturecomponentapp.data.entity.FilmData

class FilmViewModel (val databaseDao: FilmDao, app: Application) : AndroidViewModel (app) {

    // Threads
    private var viewModelJob = Job()
    private val uiCoroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    // filme
    private var rFilm = MutableLiveData<FilmData>()
    val responseFilmData: LiveData<FilmData> get() = rFilm
    // lista de filmes da API
    private var rFilmList = MutableLiveData<FilmsJson>()
    val responseFilmList: LiveData<FilmsJson> get() = rFilmList

    private var listGenre: Array<Genres.Genre>? = null

    init {
        uiCoroutineScope.launch {
            val request= FilmsApi.retrofitService.callGenreMovieApi()
            listGenre = request.await().genreList
        }
    }

    fun requestFilmApiService () {
        uiCoroutineScope.launch {
            //receber a chamada da API sem bloquear a thread princial
            val getCallDeferred = FilmsApi.retrofitService.callFilmApi("550")//550 = clube da luta
            try {
                val listResult = getCallDeferred.await()
                rFilm.value = listResult
            }
            catch (t: Throwable) {
                rFilm.value = FilmData(idDB = -1,title = "sem retorno")
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
                //rFilmList.value = FilmsJson( LiveData<listOf( FilmData(title = "Sem retorno: ${t.message}"))> )
            }
        }
    }

    // inserir filme chamando funcao de suspensao
    fun insertFilm(name: String, date: String) {
        uiCoroutineScope.launch { insert( FilmData(title = name, releaseData = date) ) }
    }

    //funcao de suspensao para inserir filmData no BD
    private suspend fun insert (filmData: FilmData) {
        withContext(Dispatchers.IO) { databaseDao.insertFilm(filmData) }
    }

    override fun onCleared() {
        super.onCleared()
        // cancelar raiz das coroutines
        viewModelJob.cancel()
    }
}