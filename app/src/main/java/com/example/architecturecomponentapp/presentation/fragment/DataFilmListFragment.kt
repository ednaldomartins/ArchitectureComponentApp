package com.example.architecturecomponentapp.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.example.architecturecomponentapp.data.entity.FilmData
import com.example.architecturecomponentapp.model.*
import com.example.architecturecomponentapp.presentation.activity.FilmDetailsActivity
import com.example.architecturecomponentapp.presentation.adapter.FilmListAdapter

class DataFilmListFragment: BaseFilmListFragment(),
    FilmListAdapter.OnFilmClickListener {

    /********************************************************
     *  variaveis herdadas de BaseFilmListFragment:         *
     *      mFilmRecyclerView: RecyclerView                 *
     *      filmListAdapter: FilmListAdapter                *
     *      mSwipeRefreshLayout: SwipeRefreshLayout         *
     *      filmViewModel: FilmViewModel                    *
     *******************************************************/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // recuperar view da super classe
        val view = super.onCreateView(inflater, container, savedInstanceState)

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
            mFilmRecyclerView.adapter = filmListAdapter

        })

        return view
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
