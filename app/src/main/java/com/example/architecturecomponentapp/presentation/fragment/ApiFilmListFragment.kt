package com.example.architecturecomponentapp.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.architecturecomponentapp.R
import com.example.architecturecomponentapp.data.database.local.FilmDatabase
import com.example.architecturecomponentapp.model.FilmViewModel
import com.example.architecturecomponentapp.model.FilmViewModelFactory
import com.example.architecturecomponentapp.presentation.activity.FilmDetailsActivity
import com.example.architecturecomponentapp.presentation.adapter.FilmListAdapter

class ApiFilmListFragment: Fragment(), FilmListAdapter.OnFilmClickListener{

    private lateinit var mFilmRecylerViewApi: RecyclerView
    private lateinit var filmListAdapter: FilmListAdapter

    private lateinit var filmViewModel: FilmViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate o layout desse fragment
        val view = inflater.inflate(R.layout.fragment_api_film_list, container, false)
        initViews(view)

        // recuperar fonte de dados
        val application = requireNotNull(this.activity).application
        val dataSource = FilmDatabase.getInstance(application).filmDao
        val filmViewModelFactory = FilmViewModelFactory(dataSource, application)
        //estou usando a referencia da activity pra pegar a busca
        filmViewModel = ViewModelProviders.of(activity!!, filmViewModelFactory).get(FilmViewModel::class.java)

        // configurando RecyclerView
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        mFilmRecylerViewApi.layoutManager = layoutManager
        mFilmRecylerViewApi.setHasFixedSize(true)

        // chamada da lista de films da api
        filmViewModel.requestFilmListApiService()

        filmViewModel.responseFilmList.observe(this, Observer {
            // configurando adapter do RecyclerView
            it.movies?.let { list ->
                filmListAdapter = FilmListAdapter(context = activity, filmListJson = list, onFilmClickListener = this)
                mFilmRecylerViewApi.adapter = filmListAdapter
            }
        })

        return view
    }

    private fun initViews(v: View) {
        mFilmRecylerViewApi = v.findViewById(R.id.film_list_api_recycle_view)
    }

    override fun onFilmClick(filmId: Long?, position: Int) {
        Log.e("ONCLICKFILM", "ID = $filmId, position = $position")
        val intent: Intent = Intent(activity, FilmDetailsActivity::class.java)
        intent.putExtra("filmId", filmId)
        startActivity( intent )
    }

}