package com.example.architecturecomponentapp.presentation.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_film_details.*

import com.example.architecturecomponentapp.R
import com.example.architecturecomponentapp.data.database.remote.Api
import com.example.architecturecomponentapp.data.entity.FilmData

class FilmDetailsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var favoriteButton: Button

    //private lateinit var filmViewModel: FilmViewModel
    private lateinit var film: FilmData
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_details)

        initViews()
        // recuperar filme clicado na ListView
        film = intent.extras?.getSerializable("film") as FilmData
        isFavorite = intent.getBooleanExtra("favorite", false)
        if (isFavorite) {
            film_details_favorite.background = getDrawable(R.drawable.ic_star_favorite_32dp)
            // enviar dados de apresentacao a apartir do DB
            submitDetails( film )
        }
        else {
            film_details_favorite.background = getDrawable(R.drawable.ic_star_not_favorite_32dp)
            // aplicar os dados recebido na activity de detalhes
            submitDetails( film )
        }
    }

    private fun submitDetails(filmData: FilmData) {
        if (filmData.posterPath != "") {
            val imgUri = Uri.parse( Api.URL_IMAGE + filmData.posterPath )
            Glide.with(this.film_details_poster_path.context).load(imgUri).into(this.film_details_poster_path)
        }

        film_detail_release_date.text = filmData.releaseDate
        film_detail_runtime.text = ( (filmData.runtime/60).toString() + "h" + (filmData.runtime%60).toString() + "m" )
        film_detail_genres.text = filmData.genres
        film_detail_original_language.text = ( filmData.originalLanguage.toUpperCase() )
        film_detail_popularity.text = (filmData.popularity + "  Visualizações.")
        film_detail_average.text = (" ${filmData.voteAverage} ")
        film_details_title.text = filmData.title
        film_detail_overview.text = filmData.overview
        film_detail_production_companies.text = filmData.productionCompanies
        film_detail_budget.text = ("Despesas: " + filmData.budget + "US$")
        film_detail_revenue.text = ("Receita: " + filmData.revenue + "US$")
        film_detail_homepage.text = filmData.homepage
    }

    fun initViews() {
        favoriteButton = film_details_favorite
        favoriteButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        v?.let {
            if (v.id == favoriteButton.id) {
                if ( !isFavorite ) {
                    isFavorite = true
                    favoriteButton.background = getDrawable( R.drawable.ic_star_favorite_32dp )
                    // 1 = inserir no DB
                    setResult(1, intent.putExtra("film", film))
                }
                else {
                    isFavorite = false
                    favoriteButton.background = getDrawable( R.drawable.ic_star_not_favorite_32dp )
                    // 2 = remover do DB
                    setResult(2, intent.putExtra("film", film))
                }
            }
        }
    }
}
