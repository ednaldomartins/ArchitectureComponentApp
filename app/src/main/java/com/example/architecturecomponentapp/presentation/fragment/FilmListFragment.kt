package com.example.architecturecomponentapp.presentation.fragment

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
import com.example.architecturecomponentapp.presentation.adapter.FilmListAdapter

class FilmListFragment: Fragment() {

    private lateinit var mFilmList: RecyclerView
    private lateinit var filmListAdapter: FilmListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate o layout desse fragment
        val view = inflater.inflate(R.layout.fragment_film_list, container, false)
        initViews(view)

        // recuperar fonte de dados
        val application = requireNotNull(this.activity).application
        val dataSource = FilmDatabase.getInstance(application).filmDao
        val filmViewModelFactory = FilmViewModelFactory(dataSource, application)
        val filmViewModel = ViewModelProviders.of(this, filmViewModelFactory).get(FilmViewModel::class.java)

        // configurando RecyclerView
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        mFilmList.layoutManager = layoutManager
        mFilmList.setHasFixedSize(true)


        Log.e("TESTE", "fragment no onCreateView List")
        filmViewModel.databaseDao.filmList().observe(this, Observer {
            Log.e("TESTE", "fragment no Observer List")
            // configurando adapter do RecyclerView
            filmListAdapter = FilmListAdapter(it, activity)
            mFilmList.adapter = filmListAdapter
        })

        return view
    }

    private fun initViews(v: View) {
        Log.e("TESTE", "fragment in initViews")
        mFilmList = v.findViewById(R.id.film_list_recycle_view)
    }
}
