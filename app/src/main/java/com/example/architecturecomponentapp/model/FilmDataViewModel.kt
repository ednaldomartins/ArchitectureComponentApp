package com.example.architecturecomponentapp.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import kotlinx.coroutines.*

import com.example.architecturecomponentapp.data.dao.FilmDao
import com.example.architecturecomponentapp.data.entity.FilmData

class FilmDataViewModel (databaseDao: FilmDao, app: Application) : AndroidViewModel(app) {

    // Coroutines
    private var viewModelJob = Job()
    private val uiCoroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    /**
     *  filmsDatabase tem acesso a lista de filmes do database local.
     *  filmsDatabase deve ser usado para qualquer acesso a esse database local de filmes,
     *  por parte de qualquer fragments ou activities atraves do seu get().
     *  _mementoPresentationFilmList foi criado para manter uma copia do estado atual do
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
     *  realizado esse filtro nos filmes do database, atraves do _mementoPresentationFilmList.
     */
    //  lista de filmes direto do database
    private var _filmsDatabase: LiveData<List<FilmData>>? = databaseDao.filmList()
    //  get do database
    val filmsDataBase: LiveData<List<FilmData>>? get() = _filmsDatabase
    //  mantem o estado atual do database para fazer buscas baseado no database, e nao na lista de apresentacao
    private var _mementoPresentationFilmList: MutableLiveData<List<FilmData>>? = MutableLiveData()
    //  a lista de apresentacao pode ser modificada baseado na busca, o memento so se altera apos alteracao no database
    var presentationFilmList: MutableLiveData<List<FilmData>>? = MutableLiveData()

    //  pagina atual
    private var _actualPage: Int = 1
    val actualPage: Int get() = _actualPage
    //  total de paginas
    private var _totalPages: Int = 1
    val totalPages: Int get() = _totalPages

    companion object {
        //  limite de view para viewholder
        private const val PRESENTATION_LIST_SIZE: Int = 10
    }

    fun setPresentationDatabase () {
        _mementoPresentationFilmList?.value =  _filmsDatabase?.value
        setPresentation()
    }

    //  buscar filmes no database local
    fun searchFilmDatabase (query: String) {
        uiCoroutineScope.launch {
            // o memento deve ser verificado para ver se esta nulo
            _filmsDatabase?.value?.let {
                // caso nao esteja vazio, criaremos um vetor que ira criar uma nova lista de apresentacao
                val newList: MutableList<FilmData>? = mutableListOf()
                // se contem parte ou toda a palavra deve adicionar a nova lista
                for (i in 0 until it.size) {
                    if ( it[i].title.toLowerCase().contains(query.toLowerCase()) )
                        newList?.add(it[i])
                    // lista de apresentacao recebe a nova lista que foi buscada
                }
                _mementoPresentationFilmList?.value = newList
                setPresentation(page = 1)
            }
        }
    }

    fun setPresentation(page: Int = _actualPage) {
        _actualPage = validatePage(page)
        _mementoPresentationFilmList?.value?.let {
            //  exemplo: se tiver 25 filmes na lista, entao teremos 2+1=3 paginas. 10 na primeira e segunda, e 5 na terceira.
            _totalPages = ((it.size-1)/(PRESENTATION_LIST_SIZE)) + 1
            //  calcular tamanho da sub-lista (tamanho = ultimo item a ser exibido na pagina)
            val sizeSubList: Int =
                //  se nao for a ultima pagina, entao... exemplo: SIZE = 10 * _acutualPage = 2 = 20
                if (_actualPage != totalPages) PRESENTATION_LIST_SIZE*_actualPage
                //  sendo a ultima, entao... ultimo = it.size:
                else it.size
            //  setar sub-lista
            presentationFilmList?.postValue( it.subList( (_actualPage-1)*PRESENTATION_LIST_SIZE, sizeSubList) )
        }
    }

    private fun validatePage(page: Int) =  when {
        (page < 1) -> 1
        (page > _totalPages) -> totalPages
        else -> page
    }

    override fun onCleared() {
        super.onCleared()
        // cancelar raiz das coroutines
        viewModelJob.cancel()
    }
}