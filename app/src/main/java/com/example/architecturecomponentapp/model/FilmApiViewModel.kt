package com.example.architecturecomponentapp.model

import android.app.Application
import android.util.Log.e
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import kotlinx.coroutines.*

import com.example.architecturecomponentapp.data.dao.FilmDao
import com.example.architecturecomponentapp.data.database.remote.FilmsApi
import com.example.architecturecomponentapp.util.FilmApiStatus
import com.squareup.moshi.JsonDataException

class FilmApiViewModel (private val databaseDao: FilmDao, app: Application) : AndroidViewModel (app) {

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
    // connection status
    private val _status = MutableLiveData<FilmApiStatus>()
    // get() connection  status
    val status: LiveData<FilmApiStatus> get() = _status
    // pagina atual
    private var _actualPage: Int = 1
    val actualPage: Int get() = _actualPage
    // total de paginas
    private var _totalPages: Int = 1
    val totalPages: Int get() = _totalPages
    // query da pagina da API
    private var _query: String = ""
    val query: String get() = _query

    init {
        uiCoroutineScope.launch {
            val request= FilmsApi.retrofitService.callGenreMovieApi()
            listGenre = try {
                request.await().genreList
            } catch (t: Throwable) {
                e("ERRO - FILM REQUEST", t.message!!)
                emptyArray()
            }
        }
    }

    // recupera uma lista de filmes da API
    fun requestFilmListApiService (page: Int = _actualPage) {
        /*
        *   atualizar a query: caso o usuario tenha enviado uma, a query sera atualizada para a
        *   nova query requisitada. quando o usuario apenas cancela uma pesquisa, ou a consulta
        *   vem da pagina inicial, o query = ""
        */
        val newPage = validatePage(page)
        if (_query == "")
            requestPopularFilmList(newPage)
        else
            requestSearchedFilmList(newPage)
    }

    // recuperar a lista de filmes mais populares da API
    private fun requestPopularFilmList(page: Int) {
        //receber a chamada da API sem bloquear a thread princial
        uiCoroutineScope.launch {
            val getCallDeferred = FilmsApi.retrofitService.callPopularMovieListApi(page)
            setRequestResult(getCallDeferred)
        }
    }

    // recupera uma lista de filmes da API baseado na busca do usuario
    private fun requestSearchedFilmList (page: Int = _actualPage) {
        uiCoroutineScope.launch {
            //receber a chamada da API sem bloquear a thread princial
            val getCallDeferred = FilmsApi.retrofitService.callSearchMovieList(page, query)
            setRequestResult(getCallDeferred)
        }
    }

    private suspend fun setRequestResult(callDeferred: Deferred<FilmsJson>) {
        try {
            _status.value = FilmApiStatus.LOADING
            // pegar lista de filmes da API pela busca
            val resultList = callDeferred.await()
            _requestFilmList.value = resultList
            // informacoes sobre numero de paginas
            _actualPage = resultList.page
            _totalPages = resultList.totalPages
            // normalizar status da requisicao
            _status.value = FilmApiStatus.DONE

            if (resultList.movies?.size == 0)
                Toast.makeText(getApplication(), "A busca não encontrou filmes.",Toast.LENGTH_LONG).show()

        }
        catch(t: JsonDataException) {
            e("ERRO - search REQUEST", t.message!!)
            Toast.makeText(getApplication(), "ERRO: problema com os dados dos filmes recuperados.",Toast.LENGTH_LONG).show()
            _status.value = FilmApiStatus.ERRO
            _requestFilmList.value = FilmsJson( emptyArray( ) )
        }
        catch (t: Throwable) {
            e("ERRO - search REQUEST", t.message!!)
            Toast.makeText(getApplication(), "ERRO: não foi possível buscar por filme pesquisado.",Toast.LENGTH_LONG).show()
            _status.value = FilmApiStatus.ERRO
            _requestFilmList.value = FilmsJson( emptyArray( ) )
        }
    }


    private fun validatePage(page: Int) =  when {
            (page < 1) -> 1
            (page > _totalPages) -> totalPages
            else -> page
    }

    fun setSearch (query: String = "") {
        // se nao receber consulta, entao aplica consulta limpa.
        _query = query
        //  resetar paginas a cada nova consulta
        _actualPage = 1
        _totalPages = 1
    }

    // verificar se o filme esta no database
    fun isFavoriteFilm (id: Long) : Boolean {
        var result = false
        runBlocking {
            withContext(Dispatchers.IO) {
                result = databaseDao.getFavorite(id)
            }
        }
        return result
    }

    override fun onCleared() {
        super.onCleared()
        // cancelar raiz das coroutines
        viewModelJob.cancel()
    }
}