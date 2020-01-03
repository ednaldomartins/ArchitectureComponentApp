package com.example.architecturecomponentapp.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import kotlinx.coroutines.*

import com.example.architecturecomponentapp.data.dao.FilmDao
import com.example.architecturecomponentapp.data.database.remote.FilmsApi
import com.example.architecturecomponentapp.data.entity.FilmData

class FilmViewModel (val databaseDao: FilmDao, app: Application) : AndroidViewModel (app) {

    // Coroutines
    private var viewModelJob = Job()
    private val uiCoroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    // dados do filme da API.
    private var rFilm = MutableLiveData<FilmsJson.FilmJson>()
    // adaptar resultado para FilmData
    val responseFilmJson: LiveData<FilmsJson.FilmJson> get() = rFilm
    // lista de filmes da API
    private var rFilmList = MutableLiveData<FilmsJson>()
    val responseFilmList: LiveData<FilmsJson> get() = rFilmList

    private var listGenre: Array<Genres.Genre>? = null

    init {
        uiCoroutineScope.launch {
            //val request= FilmsApi.retrofitService.callGenreMovieApi()
            //listGenre = request.await().genreList
        }
    }

    fun requestFilmApiService (filmId: Long) {
        uiCoroutineScope.launch {
            //receber a chamada da API sem bloquear a thread princial
            val getCallDeferred = FilmsApi.retrofitService.callFilmApi(filmId)
            try {
                val requestResult = getCallDeferred.await()
                //atualizando valor do rFilm
                rFilm.value = requestResult
            }
            catch (t: Throwable) {
                val string = t.message
                Log.e("ERRO - FILM REQUEST", string!!)
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
                val string = t.message
                Log.e("ERRO - REQUEST", string!!)
            }
        }
    }

    // inserir filme chamando funcao de suspensao
    fun insertFilm(
        id: Long,
        title: String,
        releaseDate: String,
        genresString: String,
        homepage: String,
        originalLanguage: String,
        overview: String,
        popularity: String,
        posterPath: String,
        status:String,
        revenue: Long,
        budget: Long,
        runtime: Int,
        voteAverage: Float,
        companiesString: String)
    {
        uiCoroutineScope.launch {
            insert( FilmData(
                id = id,
                title = title,
                releaseDate = releaseDate,
                genres = genresString,
                homepage = homepage,
                originalLanguage = originalLanguage,
                overview = overview,
                popularity = popularity,
                posterPath = posterPath,
                status = status,
                revenue = revenue,
                budget = budget,
                runtime = runtime,
                voteAverage = voteAverage,
                productionCompanies = companiesString
            ) )
        }
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