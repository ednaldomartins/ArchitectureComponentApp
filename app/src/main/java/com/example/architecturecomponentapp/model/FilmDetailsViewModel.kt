package com.example.architecturecomponentapp.model

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*

import kotlinx.coroutines.*

import com.example.architecturecomponentapp.data.dao.FilmDao
import com.example.architecturecomponentapp.data.database.remote.FilmsApi
import com.example.architecturecomponentapp.data.entity.FilmData

class FilmDetailsViewModel (private val databaseDao: FilmDao, app: Application) :
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

    /*
     *  variavel para controlar o ciclo de vida da FilmDetailsActivity. sempre que a activity for
     *  criada ou recriada ela deve setar os valores do seu lifecyle na variavel da viewodel, para
     *  que a viewmodel possa ter controle sobre o ciclo de vida dela. Quando a activity for
     *  destruída a viewmodel pode deixar de observar a lifecycle da activity e so sera adicionada
     *  ao observer de uma nova activity construida, visto que cada activity tem seu ciclo de vida.
     */
    private var _lifecycle: Lifecycle? = null
    val lifecycle: Lifecycle? get() = _lifecycle
    fun setLifecycle(lf: Lifecycle) {
        _lifecycle = lf
        _lifecycle?.addObserver(this)
    }

    private var countRequest = 0
    companion object {
        private const val MAX_REQUEST: Int = 5
        private const val DELAY_REQUEST: Long = 5000
    }

    // dados do filme da API.
    private var _requestFilm = MutableLiveData<FilmsJson.FilmJson>()
    // adaptar resultado para FilmData
    val responseFilmJson: LiveData<FilmsJson.FilmJson> get() = _requestFilm

    // recupera apenas um unico filme da API, para exibir suas informacoes na activity de detalhes
    fun requestFilmApiService (filmId: Long) {
        uiCoroutineScope.launch {
            //receber a chamada da API sem bloquear a thread princial
            val getCallDeferred = FilmsApi.retrofitService.callFilmApi(filmId)
            try {
                val requestResult = getCallDeferred.await()
                //atualizando valor do _requestFilm
                _requestFilm.value = requestResult
            } catch (t: Throwable) {
                //  se nao atingiu o limite de tentativas...
                if (countRequest++ < MAX_REQUEST) {
                    Log.e("ERRO - FILM REQUEST", t.message!!)
                    Toast.makeText( getApplication(), "Erro na comunicação com o serviço ($countRequest).", Toast.LENGTH_SHORT).show()
                    //  esperar 5 segundos ate a proxima tentativa
                    delay(DELAY_REQUEST)
                    requestFilmApiService(filmId)
                }
                else
                    Toast.makeText( getApplication(), "Limite de tentativas excedido.", Toast.LENGTH_SHORT).show()
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

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onResultDetailsActivity () {
        //  O filme pode nao ter sido recuperado da API e ainda ser null
        _film?.let {
            val inDatabase = this.isFavoriteFilm(it.id)
            // se estiver marcado como favorito e nao estiver no database
            if ( _isFavorite!! && !inDatabase ) {
                insertFilm(it)
            }
            // se nao estiver marcado como favorito e estiver no database
            else if (!_isFavorite!! && inDatabase) {
                //  pegar film diretamente do database para ter o mesmo objeto.
                val deleteFilm = this.getFilm(it.id)
                deleteFilm(deleteFilm)
            }
        }

        //  remover observer.
        _lifecycle?.removeObserver(this)
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