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
import com.example.architecturecomponentapp.model.FilmViewModelFactory
import com.example.architecturecomponentapp.presentation.adapter.FilmListAdapter

/**
 * Classe base para os outros fragments que apresentam lista de filmes usando RecyclerView
 */
open class BaseFilmListFragment :
    Fragment(),
    SwipeRefreshLayout.OnRefreshListener,
    SearchView.OnQueryTextListener {

    protected lateinit var mFilmRecyclerView: RecyclerView
    protected lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    protected lateinit var mSearchView: SearchView

    protected lateinit var mButtonFirstPage: Button
    protected lateinit var mButtonBeforePage: Button
    protected lateinit var mButtonNextPage: Button
    protected lateinit var mButtonLastPage: Button
    protected lateinit var mNumberPage: TextView

    //protected lateinit var filmViewModel: FilmApiViewModel
    protected lateinit var filmViewModelFactory: FilmViewModelFactory
    protected lateinit var filmListAdapter: FilmListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate o layout desse fragment
        val view = inflater.inflate(R.layout.fragment_api_film_list, container, false)
        initViews(view)
        setHasOptionsMenu(true)

        // recuperar fonte de dados
        val application = requireNotNull(this.activity).application
        val dataSource = FilmDatabase.getInstance(application).filmDao
        filmViewModelFactory = FilmViewModelFactory(dataSource, application)
        //filmViewModel = ViewModelProviders.of(activity!!, filmViewModelFactory).get(FilmApiViewModel::class.java)

        // configurando RecyclerView
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        mFilmRecyclerView.layoutManager = layoutManager
        mFilmRecyclerView.setHasFixedSize(true)

        return view
    }

    private fun initViews(v: View) {
        mFilmRecyclerView = v.findViewById(R.id.film_list_api_recycle_view)
        mSwipeRefreshLayout = v.findViewById(R.id.film_list_api_layout)
        mSwipeRefreshLayout.setOnRefreshListener(this)
    }

    override fun onRefresh() {
        mSwipeRefreshLayout.isRefreshing = false
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_main, menu)
        val mMenuSearchItem: MenuItem? = menu?.findItem(R.id.search_menu)
        mSearchView = mMenuSearchItem?.actionView as SearchView
        mSearchView.setOnQueryTextListener(this)
        super.onCreateOptionsMenu(menu, inflater)
    }

    protected fun refreshPageButton(actualPage: Int, totalPages: Int) {
        // setando o numero atual da pagina na apresentacao
        mNumberPage.text = actualPage.toString()
        /** sempre que a requisicao muda, a pagina atual podera ser alterada, entao...*/
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

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

}
