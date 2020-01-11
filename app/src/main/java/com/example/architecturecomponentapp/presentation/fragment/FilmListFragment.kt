package com.example.architecturecomponentapp.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.example.architecturecomponentapp.R
import com.example.architecturecomponentapp.data.database.local.FilmDatabase
import com.example.architecturecomponentapp.data.entity.FilmData
import com.example.architecturecomponentapp.model.*
import com.example.architecturecomponentapp.presentation.activity.FilmDetailsActivity
import com.example.architecturecomponentapp.presentation.adapter.FilmListAdapter

class FilmListFragment: Fragment(),
    FilmListAdapter.OnFilmClickListener,
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var mFilmList: RecyclerView
    private lateinit var filmListAdapter: FilmListAdapter
    private lateinit var swipeRefresh: SwipeRefreshLayout

    private lateinit var filmViewModel: FilmViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate o layout desse fragment
        val view = inflater.inflate(R.layout.fragment_api_film_list, container, false)
        initViews(view)

        // recuperar fonte de dados
        val application = requireNotNull(this.activity).application
        val dataSource = FilmDatabase.getInstance(application).filmDao
        val filmViewModelFactory = FilmViewModelFactory(dataSource, application)
        filmViewModel = ViewModelProviders.of(activity!!, filmViewModelFactory).get(FilmViewModel::class.java)

        // configurando RecyclerView
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        mFilmList.layoutManager = layoutManager
        mFilmList.setHasFixedSize(true)

        filmViewModel.filmsDataBase?.observe(this, Observer {
            filmViewModel.setPresentationDatabase(it)
        })

        filmViewModel.presentationFilmList?.observe(this, Observer {
            // configurando adapter do RecyclerView
            filmListAdapter = FilmListAdapter(
                context = activity,
                filmListJson = Array( it.size) { FilmsJson.FilmJson(genres = arrayOf(), productionCompanies = arrayOf()) },
                filmListData = it,
                onFilmClickListener = this)
            mFilmList.adapter = filmListAdapter
        })

        return view
    }

    private fun initViews(v: View) {
        mFilmList = v.findViewById(R.id.film_list_api_recycle_view)
        swipeRefresh = v.findViewById(R.id.film_list_api_layout)
        swipeRefresh.setOnRefreshListener(this)
    }


    override fun onRefresh() {
        filmViewModel.loadFilmDatabase()
        swipeRefresh.isRefreshing = false
    }

    override fun onFilmClick(filmId: Long?, position: Int) {
        val intent: Intent = Intent(activity, FilmDetailsActivity::class.java)
        val film = filmViewModel.getFilm(filmId!!)
        intent.putExtra("film", film)
        intent.putExtra("favorite", true)
        // inicia activity com resquestCode = 1 -> abrir a partir do DB
        startActivityForResult( intent, 1 )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // resultCode = 2 -> o filme foi removido dos favoritos
        if (requestCode == 1 && resultCode == 2) {
            val film = data!!.extras?.getSerializable("film") as FilmData
            filmViewModel.deleteFilm(film)
        }
    }
}
