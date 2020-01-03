package com.example.architecturecomponentapp.presentation.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide

import com.example.architecturecomponentapp.R
import com.example.architecturecomponentapp.data.database.local.FilmDatabase
import com.example.architecturecomponentapp.data.database.remote.Api
import com.example.architecturecomponentapp.model.FilmViewModel
import com.example.architecturecomponentapp.model.FilmViewModelFactory
import com.example.architecturecomponentapp.presentation.adapter.FilmAdapter
import kotlinx.android.synthetic.main.activity_film_details.*

class FilmDetailsActivity : AppCompatActivity() {

    private lateinit var filmViewModel: FilmViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_details)
        // instanciar viewModel
        initViewModel()
        // recuperar id do filme clicado na ListView
        val id = intent.getLongExtra("filmId", 0)
        // localizar film na API usando o id
        filmViewModel.requestFilmApiService(id)
        // quando os dados forem recuperados da API através das co-rotinas apresentar os dados na tela
        filmViewModel.responseFilmJson.observe(this, Observer { json ->
            // aplicar os dados recebido na activity de detalhes
            //val filmJson = json
            //filmJson.let { film ->
            // se tivermos um caminho de uma foto salva, entao...

            val film = FilmAdapter.adaptJsonToData( json )
            if (film.posterPath != "") {
                val imgUri = Uri.parse( Api.URL_IMAGE + film.posterPath )
                Glide.with(this.film_details_poster_path.context).load(imgUri).into(this.film_details_poster_path)
            }

            this.film_detail_release_date.text = film.releaseDate
            // adaptar de minutos para horas de filme
            this.film_detail_runtime.text = ( (film.runtime/60).toString() + "h" + (film.runtime%60).toString() + "m" )
            this.film_detail_genres.text = film.genres
            this.film_detail_original_language.text = ( film.originalLanguage.toUpperCase() )
            this.film_detail_popularity.text = (film.popularity + "  Visualizações.")
            this.film_detail_average.text = (" ${film.voteAverage} ")
            //this.film_detail_status.text = film.status

            this.film_details_title.text = film.title
            this.film_detail_overview.text = film.overview
            this.film_detail_production_companies.text = film.productionCompanies
            this.film_detail_budget.text = ("Despesas: " + film.budget + "US$")
            this.film_detail_revenue.text = ("Receita: " + film.revenue + "US$")
            this.film_detail_homepage.text = film.homepage


        })

    }

    fun initViewModel () {
        // recuperar fonte de dados
        val application = requireNotNull(this).application
        val dataSource = FilmDatabase.getInstance(application).filmDao
        val filmViewModelFactory = FilmViewModelFactory(dataSource, application)
        filmViewModel = ViewModelProviders.of(this, filmViewModelFactory).get(FilmViewModel::class.java)
    }

}
