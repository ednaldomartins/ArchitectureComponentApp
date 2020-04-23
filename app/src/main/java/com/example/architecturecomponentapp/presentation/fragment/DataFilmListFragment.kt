package com.example.architecturecomponentapp.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.example.architecturecomponentapp.domain.entity.FilmsJson
import com.example.architecturecomponentapp.domain.viewmodel.FilmDataViewModel
import com.example.architecturecomponentapp.presentation.adapter.FilmListAdapter

class DataFilmListFragment: BaseFilmListFragment() {

    /********************************************************
     *  variaveis herdadas de BaseFilmListFragment:         *
     *      mFilmRecyclerView: RecyclerView                 *
     *      filmListAdapter: FilmListAdapter                *
     *      mSwipeRefreshLayout: SwipeRefreshLayout         *
     *      filmViewModelFactory: FilmViewModelFactory      *
     *******************************************************/

    private lateinit var filmViewModel: FilmDataViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // recuperar view da super classe
        val view = super.onCreateView(inflater, container, savedInstanceState)

        filmViewModel = ViewModelProvider(activity!!, super.filmViewModelFactory).get(FilmDataViewModel::class.java)
        super.setViewModel(filmViewModel)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //  Quando houver alteracao no database, a lista de apresentacao deve ser atualizada
//        filmViewModel.filmsDataBase?.observe(viewLifecycleOwner, Observer {
//            filmViewModel.setPresentationDatabase()
//        })

        //  Quando a lista de apresentacao for atualizada, o recyclerview tambem deve ser atualizado
        filmViewModel.presentationFilmList?.observe(viewLifecycleOwner, Observer {
            refreshPageButton(filmViewModel.actualPage, filmViewModel.totalPages)
            // configurando adapter do RecyclerView
            filmListAdapter = FilmListAdapter(
                context = activity,
                filmListJson = Array( it.size) { FilmsJson.FilmJson(genres = arrayOf(), productionCompanies = arrayOf()) },
                filmListData = it,
                onFilmClickListener = this)
            mFilmRecyclerView.adapter = filmListAdapter
        })

    }

    override fun onResume() {
        //  se estado do recyclerView nao for nulo recupera-lo
        if (filmViewModel.recyclerViewState != null) {
            mFilmRecyclerView.layoutManager?.onRestoreInstanceState(filmViewModel.recyclerViewState)
        } else {
            if (filmViewModel.presentationFilmList?.value != null)
                filmViewModel.setPresentation()
            else{
                filmViewModel.filmsDataBase?.observe(viewLifecycleOwner, Observer {
                    filmViewModel.setPresentationDatabase()
                })
            }
        }

        super.onResume()
    }

    override fun onStop() {
        mFilmRecyclerView.layoutManager?.let {
            filmViewModel.setRecyclerViewState(it.onSaveInstanceState())
        }
        super.onStop()
    }

    //  chamar a lista do database padrao e chamar o super para parar a animacao
    override fun onRefresh() {
        filmViewModel.setPresentationDatabase()
        super.onRefresh()   //isRefreshing = false
    }

    //  se o texto da busca for limpo, entao deve recarregar a apresentacao a partir do database
    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != "")
            filmViewModel.searchFilmDatabase(newText!!)
        else
            filmViewModel.setPresentationDatabase()

        return true
    }

    //  enviar query pesquisava pelo usuario para buscar por filme no database que contenha a ela
    override fun onQueryTextSubmit(query: String?): Boolean {
        // esconder teclado
        mSearchView.clearFocus()
        return true
    }

}
