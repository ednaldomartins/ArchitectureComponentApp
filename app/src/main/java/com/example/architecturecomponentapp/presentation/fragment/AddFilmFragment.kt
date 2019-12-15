package com.example.architecturecomponentapp.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders

import com.example.architecturecomponentapp.R
import com.example.architecturecomponentapp.data.database.FilmDatabase
import com.example.architecturecomponentapp.model.FilmViewModel
import com.example.architecturecomponentapp.model.FilmViewModelFactory

import kotlinx.android.synthetic.main.fragment_add_film.*


class AddFilmFragment : Fragment(), View.OnClickListener {
    var filmViewModel: FilmViewModel? = null
    lateinit var addButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("TESTE", "fragment no onCreate")
        //arguments?.let { }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.e("TESTE", "fragment no onCreateView")

        // Inflate o layout desse fragment
        val view = inflater.inflate(R.layout.fragment_add_film, container, false)
        initViews(view)

        // recuperar a fonte de dados
        val application = requireNotNull(this.activity).application
        val dataSource = FilmDatabase.getInstance(application).filmDao
        val filmViewModelFactory = FilmViewModelFactory(dataSource, application)
        filmViewModel = ViewModelProviders.of(this, filmViewModelFactory).get(FilmViewModel::class.java)

        return view
    }

    override fun onStart() {
        super.onStart()
        Log.e("TESTE", "fragment no onStart")
    }

    private fun initViews(v: View) {
        Log.e("TESTE", "fragment in initViews")
        addButton = v.findViewById(R.id.add_film_button_add)
        addButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        Log.e("TESTE", "fragment no onClick")
        if (v?.id == R.id.add_film_button_add) {
             Log.e("TESTE", "fragment no onClick > IF")
             filmViewModel?.insertFilm(
                 add_film_edit_text_name.text.toString(),
                 add_film_edit_text_year.text.toString().toShort()
             )
        }

    }

}
