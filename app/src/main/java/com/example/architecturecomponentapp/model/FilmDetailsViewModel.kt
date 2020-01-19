package com.example.architecturecomponentapp.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.*

import kotlinx.coroutines.*

import com.example.architecturecomponentapp.data.dao.FilmDao
import com.example.architecturecomponentapp.data.database.remote.FilmsApi
import com.example.architecturecomponentapp.data.entity.FilmData

class FilmDetailsViewModel (private val databaseDao: FilmDao, app: Application, lifecycle: Lifecycle) :
    AndroidViewModel(app),
    LifecycleObserver
{
    // Coroutines
    private var viewModelJob = Job()
    private val uiCoroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    /**
     *  Essas variaveis sao uteis para auxiliar a Activity de detalhes, e também para decidir qual
     *  acao tomar no momento em que a activity de detalhes parar ( onStop() ), a partir do
     *  conhecimento que essa ViewModel tem do lifecycle da activity de detalhes. O Film sempre deve
     *  ser inserido ou removido no database quando a activity estiver prestes a ser destruida.
     *  Levando isso em conta, o onResult dos fragments tornam-se denecessarios.
     */
    private var _filmId: Long? = null
    val filmId: Long? get() = _filmId
    fun setId(i: Long) { _filmId = i }

    private var _isFavorite: Boolean? = null
    val isFavorite: Boolean? get() = _isFavorite
    fun setIsFavorite(f: Boolean) { _isFavorite = f }

    private var _film: FilmData? = null
    val film: FilmData? get() = _film
    fun setFilm(film: FilmData) {_film = film }

    // dados do filme da API.
    private var _requestFilm = MutableLiveData<FilmsJson.FilmJson>()
    // adaptar resultado para FilmData
    val responseFilmJson: LiveData<FilmsJson.FilmJson> get() = _requestFilm

    init {
        lifecycle.addObserver(this)
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
                Log.e("ERRO - FILM REQUEST", t.message!!)
                _requestFilm.value = FilmsJson.FilmJson(genres = arrayOf(), productionCompanies = arrayOf())
            }
        }
    }

    // retornar film buscado pelo ID
    fun getFilm (id: Long): FilmData {
        return runBlocking {
            withContext(Dispatchers.IO) {
                databaseDao.get(id)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onResultDetailsActivity () {
        val inDatabase = this.isFavoriteFilm(_film!!.id)
        // se estiver marcado como favorito e nao estiver no database
        if ( _isFavorite!! && !inDatabase ) {
            insertFilm(_film!!)
        }
        // se nao estiver marcado como favorito e estiver no database
        else if (!_isFavorite!! && inDatabase) {
            deleteFilm(_film!!)
        }
    }

    // inserir film chamando função de suspensao
    private fun insertFilm(filmData: FilmData) {
        // inserir imediatamente, pois o app ou a tela de detalhes pode esta sendo destruida.
        runBlocking { insert(filmData) }
    }

    //funcao de suspensao para inserir filmData no BD
    private suspend fun insert (filmData: FilmData) {
        withContext(Dispatchers.IO) { databaseDao.insertFilm(filmData) }
    }

    // deletar film chamando função de suspensao
    private fun deleteFilm(filmData: FilmData) {
        // inserir imediatamente, pois o app ou a tela de detalhes pode esta sendo destruida.
        runBlocking { delete(filmData) }
    }

    private suspend fun delete(film: FilmData) {
        withContext(Dispatchers.IO) { databaseDao.deleteFilm(film) }
    }

    // verificar se o filme esta no database
    private fun isFavoriteFilm (id: Long) : Boolean {
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