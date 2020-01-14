package com.example.architecturecomponentapp.presentation.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.example.architecturecomponentapp.R
import com.example.architecturecomponentapp.data.database.local.FilmDatabase
import com.example.architecturecomponentapp.model.FilmViewModel
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

    protected lateinit var filmViewModel: FilmViewModel
    protected lateinit var filmListAdapter: FilmListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate o layout desse fragment
        val view = inflater.inflate(R.layout.fragment_api_film_list, container, false)
        initViews(view)
        setHasOptionsMenu(true)

        // recuperar fonte de dados
        val application = requireNotNull(this.activity).application
        val dataSource = FilmDatabase.getInstance(application).filmDao
        val filmViewModelFactory = FilmViewModelFactory(dataSource, application)
        filmViewModel = ViewModelProviders.of(activity!!, filmViewModelFactory).get(FilmViewModel::class.java)

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
        filmViewModel.loadFilmDatabase()
        mSwipeRefreshLayout.isRefreshing = false
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_main, menu)
        val mMenuSearchItem: MenuItem? = menu?.findItem(R.id.search_menu)
        mSearchView = mMenuSearchItem?.actionView as SearchView
        mSearchView.setOnQueryTextListener(this)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

}
