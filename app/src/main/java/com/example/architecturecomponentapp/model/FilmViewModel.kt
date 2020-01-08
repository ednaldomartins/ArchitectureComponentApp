package com.example.architecturecomponentapp.model

import android.app.Application
import android.util.Log
import android.widget.Toast
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
    private var _requestFilm = MutableLiveData<FilmsJson.FilmJson>()
    // adaptar resultado para FilmData
    val responseFilmJson: LiveData<FilmsJson.FilmJson> get() = _requestFilm
    // lista de filmes da API
    private var _requestFilmList = MutableLiveData<FilmsJson>()
    val responseFilmList: LiveData<FilmsJson> get() = _requestFilmList
    // lista de generos existentes na API
    private var listGenre: Array<Genres.Genre>? = null

    fun requestGenreListApi () {
        uiCoroutineScope.launch {
            val request= FilmsApi.retrofitService.callGenreMovieApi()
            listGenre = request.await().genreList
        }
    }

    // recupera apenas um unico filme da API, para exibir suas informacoes na activity de detalhes
    fun requestFilmApiService (filmId: Long) {
        uiCoroutineScope.launch {
            //receber a chamada da API sem bloquear a thread princial
            val getCallDeferred = FilmsApi.retrofitService.callFilmApi(filmId)
            try {
                val requestResult = getCallDeferred.await()
                //atualizando valor do _requestFilm
                _requestFilm.value = requestResult
            }
            catch (t: Throwable) {
                val string = t.message
                Log.e("ERRO - FILM REQUEST", string!!)
            }
        }
    }

    // recupera uma lista de filmes da API
    fun requestFilmListApiService () {
        uiCoroutineScope.launch {
            //receber a chamada da API sem bloquear a thread princial
            val getCallDeferred = FilmsApi.retrofitService.callPopularMovieListApi()
            try {
                val listResult = getCallDeferred.await()
                _requestFilmList.value = listResult
            }
            catch (t: Throwable) {
                val string = t.message
                Log.e("ERRO - REQUEST", string!!)
            }
        }
    }

    // recupera uma lista de filmes da API baseado na busca do usuario
    fun searchFilmListApiService (search: String) {
        uiCoroutineScope.launch {
            //receber a chamada da API sem bloquear a thread princial
            val getCallDeferred = FilmsApi.retrofitService.callSearchMovieList(search)
            try {
                val listResult = getCallDeferred.await()
                _requestFilmList.value = listResult

                if (listResult.movies?.size == 0)
                    Toast.makeText(getApplication(), "A busca não retornou nenhum filme.",Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(getApplication(), "A busca retornou com sucesso.",Toast.LENGTH_LONG).show()

            }
            catch (t: Throwable) {
                val string = t.message
                Log.e("ERRO - REQUEST", string!!)
                Toast.makeText(getApplication(), "ERRO: não foi possível buscar por filme pesquisado.",Toast.LENGTH_LONG).show()
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

    // inserir film chamando função de suspensao
    fun insertFilm(filmData: FilmData) {
        uiCoroutineScope.launch {
            insert(filmData)
        }
    }

    //funcao de suspensao para inserir filmData no BD
    private suspend fun insert (filmData: FilmData) {
        withContext(Dispatchers.IO) { databaseDao.insertFilm(filmData) }
    }

    fun isFavoriteFilm (id: Long) : Boolean {
        var result: Boolean = false
        runBlocking {
            withContext(Dispatchers.IO) {
                result = databaseDao.getFavorite(id)
            }
        }
        return result
    }

    // deletar film chamando função de suspensao
    fun deleteFilm(filmData: FilmData) {
        uiCoroutineScope.launch {
            delete(filmData)
        }
    }

    private suspend fun delete(film: FilmData) {
        withContext(Dispatchers.IO) { databaseDao.deleteFilm(film) }
    }

    fun getFilm (id: Long): FilmData {
        return runBlocking {
            withContext(Dispatchers.IO) {
                databaseDao.get(id)
            }
        }
    }

    // metodo para limpar o DB
    fun clearDatabase () {
        uiCoroutineScope.launch { withContext(Dispatchers.IO) { databaseDao.clear() } }
    }

    override fun onCleared() {
        super.onCleared()
        // cancelar raiz das coroutines
        viewModelJob.cancel()
    }
}