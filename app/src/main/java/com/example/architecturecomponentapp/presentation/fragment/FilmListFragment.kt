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

import com.example.architecturecomponentapp.R
import com.example.architecturecomponentapp.data.database.FilmDatabase
import com.example.architecturecomponentapp.model.FilmViewModel
import com.example.architecturecomponentapp.model.FilmViewModelFactory

class FilmListFragment: Fragment() {

    private lateinit var textView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate o layout desse fragment
        val view = inflater.inflate(R.layout.fragment_film_list, container, false)
        initViews(view)

        // recuperar fonte de dados
        val application = requireNotNull(this.activity).application
        val dataSource = FilmDatabase.getInstance(application).filmDao
        val filmViewModelFactory = FilmViewModelFactory(dataSource, application)
        val filmViewModel = ViewModelProviders.of(this, filmViewModelFactory).get(FilmViewModel::class.java)

        Log.e("TESTE", "fragment no onCreateView List")
        filmViewModel.databaseDao.filmListSortedById().observe(this, Observer {
            Log.e("TESTE", "fragment no Observer List")
            //teste pegando apenas o nome do ultimo filme adicionado
            textView.text = it.last().name
        })

        return view
    }

    private fun initViews(v: View) {
        Log.e("TESTE", "fragment in initViews")
        textView = v.findViewById(R.id.film_list_text)
    }
}
