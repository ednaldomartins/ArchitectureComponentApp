package com.example.architecturecomponentapp.presentation.fragment

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.example.architecturecomponentapp.R
import com.example.architecturecomponentapp.data.database.local.FilmDatabase
import com.example.architecturecomponentapp.domain.viewmodel.FilmListViewModel
import com.example.architecturecomponentapp.domain.viewmodel.FilmViewModelFactory
import com.example.architecturecomponentapp.presentation.adapter.FilmListAdapter

/**
 * Classe base para os outros fragments que apresentam lista de filmes usando RecyclerView
 */
open class BaseFilmListFragment :
    Fragment(),
    SwipeRefreshLayout.OnRefreshListener,
    SearchView.OnQueryTextListener, View.OnClickListener {

    //  view layout
    protected lateinit var mFilmRecyclerView: RecyclerView
    protected lateinit var mSearchView: SearchView
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    //  view page
    private lateinit var mButtonFirstPage: Button
    private lateinit var mButtonBeforePage: Button
    private lateinit var mButtonNextPage: Button
    private lateinit var mButtonLastPage: Button
    private lateinit var mNumberPage: TextView
    //  data
    protected lateinit var filmViewModelFactory: FilmViewModelFactory
    protected lateinit var filmListAdapter: FilmListAdapter
    private lateinit var viewModel: FilmListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate o layout desse fragment
        val view = inflater.inflate(R.layout.fragment_film_list, container, false)
        initViews(view)
        setHasOptionsMenu(true)

        //  criar frabrica de ViewModels
        val application = requireNotNull(this.activity).application
        val dataSource = FilmDatabase.getInstance(application).filmDao
        filmViewModelFactory =
            FilmViewModelFactory(
                dataSource,
                application
            )

        // configurando RecyclerView
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        mFilmRecyclerView.layoutManager = layoutManager
        mFilmRecyclerView.setHasFixedSize(true)

        return view
    }

    //  pegar referencia da viewmodel da classe filha para poder controlar os botoes de navegacao de pagina
    protected fun setViewModel (vm: FilmListViewModel) {
        this.viewModel = vm
    }

    //  views basicas para o funcionamento da lista de filmes
    private fun initViews(v: View) {
        mFilmRecyclerView = v.findViewById(R.id.film_list_recycle_view)
        mSwipeRefreshLayout = v.findViewById(R.id.film_list_layout)
        mSwipeRefreshLayout.setOnRefreshListener(this)
        // inicializacao dos botoes de navegacao de paginas
        mButtonFirstPage = v.findViewById(R.id.film_list_button_first_page)
        mButtonFirstPage.setOnClickListener(this)
        mButtonBeforePage = v.findViewById(R.id.film_list_button_before_page)
        mButtonBeforePage.setOnClickListener(this)
        mButtonNextPage = v.findViewById(R.id.film_list_button_next_page)
        mButtonNextPage.setOnClickListener(this)
        mButtonLastPage = v.findViewById(R.id.film_list_button_last_page)
        mButtonLastPage.setOnClickListener(this)
        // localizando o textview do numero da pagina atual
        mNumberPage = v.findViewById(R.id.film_list_number_page)
    }

    //  parar o refresh
    override fun onRefresh() {
        mSwipeRefreshLayout.isRefreshing = false
    }

    //  criacao do menu pelo fragment
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_main, menu)
        val mMenuSearchItem: MenuItem? = menu?.findItem(R.id.search_menu)
        mSearchView = mMenuSearchItem?.actionView as SearchView
        mSearchView.setOnQueryTextListener(this)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //  configuracao dos botoes de controle de pagina
    protected fun refreshPageButton(actualPage: Int, totalPages: Int) {
        // setando o numero atual da pagina na apresentacao
        mNumberPage.text = actualPage.toString()
        /* sempre que a requisicao muda, a pagina atual podera ser alterada, entao...*/
        // se nao estiver visivel, entao tornar
        if (mButtonFirstPage.visibility != View.VISIBLE) {
            mButtonFirstPage.visibility = View.VISIBLE
            mButtonBeforePage.visibility = View.VISIBLE
        }
        if (mButtonLastPage.visibility != View.VISIBLE) {
            mButtonLastPage.visibility = View.VISIBLE
            mButtonNextPage.visibility = View.VISIBLE
        }
        /* apos tornar os botoes visiveis, verificar: */
        // actualpage e totalpages == 0 por causa da API que pode retornar 0 paginas
        // se a página 1 for a atual, entao bloquear os botoes de voltar pagina
        if (actualPage == 1 || actualPage == 0) {
            mButtonFirstPage.visibility = View.INVISIBLE
            mButtonBeforePage.visibility = View.INVISIBLE
        }
        // se a ultima pagina for a atual, entao bloquear os botoes de avancar pagina
        if (actualPage == totalPages || totalPages == 0) {
            mButtonLastPage.visibility = View.INVISIBLE
            mButtonNextPage.visibility = View.INVISIBLE
        }
    }

    /**
     * funcao para controlar o click nos botoes da barra inferior de navegacao de paginas.
     * Sendo possivel ate o momento passar, voltar, ir para a primeira, e ir para ultima pagina.
     */
    override fun onClick(v: View?) {
        v?.let{
            when(it.id) {
                R.id.film_list_button_first_page -> {
                    viewModel.setPresentation(1)
                }
                R.id.film_list_button_before_page -> {
                    viewModel.setPresentation( viewModel.actualPage - 1 )
                }
                R.id.film_list_button_next_page -> {
                    viewModel.setPresentation( viewModel.actualPage + 1 )
                }
                R.id.film_list_button_last_page -> {
                    viewModel.setPresentation( viewModel.totalPages )
                }
            }
        }
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

}
