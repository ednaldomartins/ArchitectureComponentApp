package com.example.architecturecomponentapp.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import kotlinx.coroutines.*

import com.example.architecturecomponentapp.data.dao.FilmDao
import com.example.architecturecomponentapp.data.entity.FilmData
import com.example.architecturecomponentapp.util.FilmApiStatus


class FilmDataViewModel (databaseDao: FilmDao, app: Application) : AndroidViewModel(app) {

    // Coroutines
    private var viewModelJob = Job()
    private val uiCoroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    // connection status
    private val _status = MutableLiveData<FilmApiStatus>()
    // get() connection  status
    val status: LiveData<FilmApiStatus> get() = _status

    /**
     *  filmsDatabase tem acesso a lista de filmes do database local.
     *  filmsDatabase deve ser usado para qualquer acesso a esse database local de filmes,
     *  por parte de qualquer fragments ou activities atraves do seu get().
     *  mementoPresentationFilmList foi criado para manter uma copia do estado atual do
     *  database de filmes. O database local de filmes retona um LiveData da lista de filmes, que so pode
     *  pode ser acessado atraves do observador. sempre que o observador sinalizar uma modificacao
     *  nesse database, ele deve repassar o "it" referente a essa lista de filmes, para que o tanto
     *  o memento, quanto o proprio presentationFilmList sejam atualizados. A principal funcao do
     *  memento aqui e manter uma copia da lista de filmes do database mesmo apos uma busca, sendo
     *  assim, caso o presentationFilmList precise reapresentar a lista do database, ele possa fazer
     *  isso realizando uma copia do memento.
     *  presentationFilmList deve apenas apresentar a lista de filmes do database na tela
     *  pelo ViewHolder, seja atraves do proprio "it" referente a lista de filmes database, ou
     *  pela newList criada atraves do filtro de pesquisa, que recebe essa newLista apos ser
     *  realizado esse filtro nos filmes do database, atraves do mementoPresentationFilmList.
     */
    // lista de filmes direto do database
    private var _filmsDatabase: LiveData<List<FilmData>>? = databaseDao.filmList()
    // get do database
    val filmsDataBase: LiveData<List<FilmData>>? get() = _filmsDatabase
    // mantem o estado atual do database para fazer buscas baseado no database, e nao na lista de apresentacao
    private var mementoPresentationFilmList: MutableLiveData<List<FilmData>>? = MutableLiveData()
    // a lista de apresentacao pode ser modificada baseado na busca, o memento so se altera apos alteracao no database
    var presentationFilmList: MutableLiveData<List<FilmData>>? = MutableLiveData()


    fun loadFilmDatabase () {
        presentationFilmList?.value = mementoPresentationFilmList?.value
    }

    // armazenar item presentes no database em lista multaveis para apresentacao de dados
    fun setPresentationDatabase(it: List<FilmData>) {
        uiCoroutineScope.launch {
            // memento para backup do estado atual do database e presentation para viewholder
            mementoPresentationFilmList?.postValue( it )
            presentationFilmList?.postValue( it )
        }
    }

    // buscar filmes no database local
    fun searchFilmDatabase (query: String) {
        uiCoroutineScope.launch {
            // o memento deve ser verificado para ver se esta nulo
            mementoPresentationFilmList?.value?.let {
                // caso nao esteja vazio, criaremos um vetor que ira criar uma nova lista de apresentacao
                val newList: MutableList<FilmData>? = mutableListOf()
                // se contem parte ou toda a palavra deve adicionar a nova lista
                for (i in 0 until it.size) {
                    if ( it[i].title.toLowerCase().contains(query.toLowerCase()) )
                        newList?.add(it[i])
                    // lista de apresentacao recebe a nova lista que foi buscada
                }
                presentationFilmList?.value = newList
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // cancelar raiz das coroutines
        viewModelJob.cancel()
    }
}