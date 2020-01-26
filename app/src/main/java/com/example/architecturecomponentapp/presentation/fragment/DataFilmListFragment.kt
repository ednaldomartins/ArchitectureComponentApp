package com.example.architecturecomponentapp.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.architecturecomponentapp.R

import com.example.architecturecomponentapp.model.*
import com.example.architecturecomponentapp.presentation.activity.FilmDetailsActivity
import com.example.architecturecomponentapp.presentation.adapter.FilmListAdapter

class DataFilmListFragment: BaseFilmListFragment(), FilmListAdapter.OnFilmClickListener, View.OnClickListener {

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
        initViews(view!!)

        //  Quando houver alteracao no database, a lista de apresentacao deve ser atualizada
        filmViewModel.filmsDataBase?.observe(this, Observer {
            filmViewModel.setPresentationDatabase()
        })

        //  Quando a lista de apresentacao for atualizada, o recyclerview tambem deve ser atualizado
        filmViewModel.presentationFilmList?.observe(this, Observer {
            refreshPageButton(filmViewModel.actualPage, filmViewModel.totalPages)
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

    private fun initViews(v: View) {
        // inicializacao dos botoes de navegacao de paginas
        mButtonFirstPage = v.findViewById(R.id.film_list_api_button_first_page)
        mButtonFirstPage.setOnClickListener(this)
        mButtonBeforePage = v.findViewById(R.id.film_list_api_button_before_page)
        mButtonBeforePage.setOnClickListener(this)
        mButtonNextPage = v.findViewById(R.id.film_list_api_button_next_page)
        mButtonNextPage.setOnClickListener(this)
        mButtonLastPage = v.findViewById(R.id.film_list_api_button_last_page)
        mButtonLastPage.setOnClickListener(this)
        // localizando o textview do numero da pagina atual
        mNumberPage = v.findViewById(R.id.film_list_api_number_page)
    }

    override fun onRefresh() {
        filmViewModel.setPresentationDatabase()
        super.onRefresh()   //isRefreshing = false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        //  se o texto da busca for limpo, entao deve recarregar a apresentacao a partir do database
        if (newText == "")
            filmViewModel.setPresentationDatabase()

        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        filmViewModel.searchFilmDatabase(query!!)
        // esconder teclado
        mSearchView.clearFocus()
        return true
    }

    override fun onClick(v: View?) {
        v?.let{
            when(it.id) {
                R.id.film_list_api_button_first_page -> {
                    filmViewModel.setPresentation(1)
                }
                R.id.film_list_api_button_before_page -> {
                    filmViewModel.setPresentation( filmViewModel.actualPage - 1 )
                }
                R.id.film_list_api_button_next_page -> {
                    filmViewModel.setPresentation( filmViewModel.actualPage + 1 )
                }
                R.id.film_list_api_button_last_page -> {
                    filmViewModel.setPresentation( filmViewModel.totalPages )
                }
            }
        }
    }

    override fun onFilmClick(filmId: Long?) {
        val intent = Intent(activity, FilmDetailsActivity::class.java)
        intent.putExtra("filmId", filmId)
        intent.putExtra("favorite", true)
        // inicia activity com resquestCode = 1 -> abrir a partir do DB
        startActivity( intent )
    }

}
