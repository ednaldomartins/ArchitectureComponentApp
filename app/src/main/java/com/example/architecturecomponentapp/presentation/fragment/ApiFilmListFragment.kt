package com.example.architecturecomponentapp.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.architecturecomponentapp.R
import com.example.architecturecomponentapp.data.database.local.FilmDatabase
import com.example.architecturecomponentapp.model.FilmViewModel
import com.example.architecturecomponentapp.model.FilmViewModelFactory

class ApiFilmListFragment: Fragment() {

    private lateinit var text: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate o layout desse fragment
        val view = inflater.inflate(R.layout.fragment_api_film_list, container, false)
        initViews(view)

        // recuperar fonte de dados
        val application = requireNotNull(this.activity).application
        val dataSource = FilmDatabase.getInstance(application).filmDao
        val filmViewModelFactory = FilmViewModelFactory(dataSource, application)
        val filmViewModel = ViewModelProviders.of(this, filmViewModelFactory).get(FilmViewModel::class.java)

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)

        Log.e("TESTE", "fragment no onCreateView List")
        filmViewModel.databaseDao.filmList().observe(this, Observer {
            Log.e("TESTE", "fragment no Observer List")

        })

        return view
    }

    private fun initViews(v: View) {
        Log.e("TESTE", "fragment in initViews")
        text = v.findViewById(R.id.api_film_list_text)
    }
}