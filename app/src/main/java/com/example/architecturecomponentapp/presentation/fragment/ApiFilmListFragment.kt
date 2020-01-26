package com.example.architecturecomponentapp.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.example.architecturecomponentapp.R
import com.example.architecturecomponentapp.model.FilmApiViewModel
import com.example.architecturecomponentapp.presentation.activity.FilmDetailsActivity
import com.example.architecturecomponentapp.presentation.adapter.FilmListAdapter
import com.example.architecturecomponentapp.util.FilmApiStatus

class ApiFilmListFragment: BaseFilmListFragment(), FilmListAdapter.OnFilmClickListener, View.OnClickListener {

    /********************************************************
     *  variaveis herdadas de BaseFilmListFragment:         *
     *      mFilmRecyclerView: RecyclerView                 *
     *      filmListAdapter: FilmListAdapter                *
     *      mSwipeRefreshLayout: SwipeRefreshLayout         *
     *      filmViewModelFactory: FilmViewModelFactory      *
     *******************************************************/

    private lateinit var mStatusImageView: ImageView

    private lateinit var filmViewModel: FilmApiViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // recuperar view da super classe, e instanciar views adicionais desse fragment
        val view = super.onCreateView(inflater, container, savedInstanceState)
        filmViewModel = ViewModelProviders.of(activity!!, super.filmViewModelFactory).get(FilmApiViewModel::class.java)
        initViews(view!!)

        // chamar lista de filmes da api
        filmViewModel.requestFilmListApiService()
        connectionStatus()

        // observador da requiscao
        filmViewModel.responseFilmList.observe(this, Observer {
            // atulizar estados dos botoes de alterar paginas
            refreshPageButton(it.page, it.totalPages)
            // configurando adapter do RecyclerView
            it.movies?.let { list ->
                filmListAdapter = FilmListAdapter(context = activity, filmListJson = list, onFilmClickListener = this)
                mFilmRecyclerView.adapter = filmListAdapter
            }
        })

        return view
    }

    private fun initViews(v: View) {
        mStatusImageView = v.findViewById(R.id.film_list_api_status)
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
        filmViewModel.requestFilmListApiService()
        super.onRefresh()
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText == "" && filmViewModel.query != "") {
            // limpar consulta
            filmViewModel.setSearch(newText)
            filmViewModel.requestFilmListApiService(1)
        }
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        filmViewModel.setSearch(query!!)
        filmViewModel.requestFilmListApiService(1)
        // esconder teclado
        mSearchView.clearFocus()
        return true
    }

    override fun onOptionsMenuClosed(menu: Menu?) {
        filmViewModel.requestFilmListApiService(1)
        super.onOptionsMenuClosed(menu)
    }

    private fun connectionStatus () {
        filmViewModel.status.observe(this, Observer {
            if (it == FilmApiStatus.LOADING ) {
                // ocultar recyclerview e mostrar imagem de carregamento
                mFilmRecyclerView.visibility = View.GONE
                mStatusImageView.visibility = View.VISIBLE
                mStatusImageView.setImageResource( R.drawable.ic_api_request_128dp )
            }
            else if (it == FilmApiStatus.ERRO) {
                // ocultar recyclerview e mostrar imagem de erro no carregamento
                mFilmRecyclerView.visibility = View.GONE
                mStatusImageView.visibility = View.VISIBLE
                mStatusImageView.setImageResource( R.drawable.ic_offline_128dp )
            }
            else if (it == FilmApiStatus.DONE) {
                // ocultar imagem de carregamento e mostrar recyclerview
                mStatusImageView.visibility  = View.GONE
                mFilmRecyclerView.visibility = View.VISIBLE
            }
        })
    }

    /**
     * funcao para controlar o click nos botoes da barra inferior de navegacao de paginas.
     * Sendo possivel ate o momento passar, voltar, ir para a primeira, e ir para ultima pagina.
     */
    override fun onClick(v: View?) {
        v?.let{
            when(it.id) {
                R.id.film_list_api_button_first_page -> {
                    filmViewModel.requestFilmListApiService(1)
                }
                R.id.film_list_api_button_before_page -> {
                    filmViewModel.requestFilmListApiService( filmViewModel.actualPage - 1 )
                }
                R.id.film_list_api_button_next_page -> {
                    filmViewModel.requestFilmListApiService( filmViewModel.actualPage + 1 )
                }
                R.id.film_list_api_button_last_page -> {
                    filmViewModel.requestFilmListApiService( filmViewModel.totalPages )
                }
            }
        }
    }

    override fun onFilmClick(filmId: Long?) {
        val intent = Intent(activity, FilmDetailsActivity::class.java)
        // verificar se esta nos favoritos do database do usuario
        intent.putExtra("filmId", filmId)
        //  isFavorite = true | false ?
        val isFavorite = filmViewModel.isFavoriteFilm(filmId!!)
        intent.putExtra("favorite", isFavorite)
        startActivity( intent )
    }

}