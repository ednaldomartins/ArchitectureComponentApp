package com.example.architecturecomponentapp.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.example.architecturecomponentapp.model.*
import com.example.architecturecomponentapp.presentation.activity.FilmDetailsActivity
import com.example.architecturecomponentapp.presentation.adapter.FilmListAdapter

class DataFilmListFragment: BaseFilmListFragment(), FilmListAdapter.OnFilmClickListener {

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
        filmViewModel = ViewModelProviders.of(activity!!, super.filmViewModelFactory).get(FilmDataViewModel::class.java)

        //  Quando houver alteracao no database, a lista de apresentacao deve ser atualizada
        filmViewModel.filmsDataBase?.observe(this, Observer {
            filmViewModel.setPresentationDatabase(it)
        })

        //  Quando a lista de apresentacao for atualizada, o recyclerview tambem deve ser atualizado
        filmViewModel.presentationFilmList?.observe(this, Observer {
            // configurando adapter do RecyclerView
            filmListAdapter = FilmListAdapter(
                context = activity,
                filmListJson = Array( it.size) { FilmsJson.FilmJson(genres = arrayOf(), productionCompanies = arrayOf()) },
                filmListData = it,
                onFilmClickListener = this)
            mFilmRecyclerView.adapter = filmListAdapter
        })

        return view
    }

    override fun onRefresh() {
        filmViewModel.loadFilmDatabase()
        super.onRefresh()   //isRefreshing = false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        //  se o texto da busca for limpo, entao deve recarregar a apresentacao a partir do database
        if (newText == "")
            filmViewModel.loadFilmDatabase()

        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        filmViewModel.searchFilmDatabase(query!!)
        // esconder teclado
        mSearchView.clearFocus()
        return true
    }

    override fun onFilmClick(filmId: Long?) {
        val intent = Intent(activity, FilmDetailsActivity::class.java)
        intent.putExtra("filmId", filmId)
        intent.putExtra("favorite", true)
        // inicia activity com resquestCode = 1 -> abrir a partir do DB
        startActivity( intent )
    }

}
