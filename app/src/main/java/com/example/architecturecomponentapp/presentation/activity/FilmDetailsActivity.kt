package com.example.architecturecomponentapp.presentation.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_film_details.*

import com.example.architecturecomponentapp.R
import com.example.architecturecomponentapp.data.database.local.FilmDatabase
import com.example.architecturecomponentapp.data.database.remote.Api
import com.example.architecturecomponentapp.data.entity.FilmData
import com.example.architecturecomponentapp.model.FilmViewModel
import com.example.architecturecomponentapp.model.FilmViewModelFactory
import com.example.architecturecomponentapp.presentation.adapter.FilmAdapter

class FilmDetailsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var favoriteButton: Button

    private lateinit var filmViewModel: FilmViewModel
    private lateinit var film: FilmData
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_details)

        initViews()
        // instanciar viewModel
        initViewModel()
        // recuperar id do filme clicado na ListView
        val id = intent.getLongExtra("filmId", 0)
        // localizar film na API usando o id
        filmViewModel.requestFilmApiService(id)
        // quando os dados forem recuperados da API através das co-rotinas apresentar os dados na tela
        filmViewModel.responseFilmJson.observe(this, Observer { json ->
            // aplicar os dados recebido na activity de detalhes
            film = FilmAdapter.adaptJsonToData( json )

            // verificar se esta nos favoritos do database do usuario
            isFavorite = filmViewModel.isFavoriteFilm(id)
            film_details_favorite.background =
                if(isFavorite) getDrawable(R.drawable.ic_star_favorite_32dp)
                else getDrawable(R.drawable.ic_star_not_favorite_32dp)

            if (film.posterPath != "") {
                val imgUri = Uri.parse( Api.URL_IMAGE + film.posterPath )
                Glide.with(this.film_details_poster_path.context).load(imgUri).into(this.film_details_poster_path)
            }

            film_detail_release_date.text = film.releaseDate
            // adaptar de minutos para horas de filme
            film_detail_runtime.text = ( (film.runtime/60).toString() + "h" + (film.runtime%60).toString() + "m" )
            film_detail_genres.text = film.genres
            film_detail_original_language.text = ( film.originalLanguage.toUpperCase() )
            film_detail_popularity.text = (film.popularity + "  Visualizações.")
            film_detail_average.text = (" ${film.voteAverage} ")

            film_details_title.text = film.title
            film_detail_overview.text = film.overview
            film_detail_production_companies.text = film.productionCompanies
            film_detail_budget.text = ("Despesas: " + film.budget + "US$")
            film_detail_revenue.text = ("Receita: " + film.revenue + "US$")
            film_detail_homepage.text = film.homepage
        })
    }

    fun initViews() {
        favoriteButton = film_details_favorite
        favoriteButton.setOnClickListener(this)
    }

    fun initViewModel () {
        // recuperar fonte de dados
        val application = requireNotNull(this).application
        val dataSource = FilmDatabase.getInstance(application).filmDao
        val filmViewModelFactory = FilmViewModelFactory(dataSource, application)
        filmViewModel = ViewModelProviders.of(this, filmViewModelFactory).get(FilmViewModel::class.java)
    }

    override fun onClick(v: View?) {
        v?.let {
            if (v.id == favoriteButton.id) {
                // se nao for favorito, entao colocar como favorito e guardar no Database
                if ( !isFavorite ) {
                    filmViewModel.insertFilm(film)
                    favoriteButton.background = getDrawable( R.drawable.ic_star_favorite_32dp )
                }
                // senao remover do DB
                else {
                    // primeiro recuperar o filme do DB Room, para remove-lo por Objeto FilmData como parametro
                    val favoritFilm = filmViewModel.getFilm(film.id)
                    filmViewModel.deleteFilm(favoritFilm)
                    favoriteButton.background = getDrawable( R.drawable.ic_star_not_favorite_32dp )
                }
            }
        }
    }
}
