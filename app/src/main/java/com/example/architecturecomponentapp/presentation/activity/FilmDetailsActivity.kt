package com.example.architecturecomponentapp.presentation.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_film_details.*

import com.example.architecturecomponentapp.R
import com.example.architecturecomponentapp.data.database.local.FilmDatabase
import com.example.architecturecomponentapp.util.Api
import com.example.architecturecomponentapp.data.entity.FilmData
import com.example.architecturecomponentapp.model.FilmDetailsViewModel
import com.example.architecturecomponentapp.model.FilmViewModelFactory

class FilmDetailsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var favoriteButton: Button

    //private lateinit var film: FilmData
    private lateinit var viewModel: FilmDetailsViewModel
    //private var isFavorite: Boolean? = null
    //private var resultCode = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_details)

        initViews()
        initViewModel()
        setViewModel()

        // recuperar filme clicado na ListView
//        film = intent.extras?.getSerializable("film") as FilmData
//        if (isFavorite == null)
//            isFavorite = intent.getBooleanExtra("favorite", false)

        if (viewModel.isFavorite!!) {
            film_details_favorite.background = getDrawable(R.drawable.ic_star_favorite_32dp)
            // enviar dados de apresentacao a apartir do DB
            submitDetails( viewModel.film!! )
        }
        else {
            film_details_favorite.background = getDrawable(R.drawable.ic_star_not_favorite_32dp)
            // aplicar os dados recebido na activity de detalhes
            submitDetails( viewModel.film!! )
        }
    }

    private fun submitDetails(filmData: FilmData) {
        // configurando poster
        if (filmData.posterPath != "") {
            val imgUri = Uri.parse( Api.URL_IMAGE + filmData.posterPath )
            Glide.with(this.film_details_poster_path.context).load(imgUri).into(this.film_details_poster_path)
        }
        else
            film_details_poster_path.setImageDrawable(getDrawable(R.drawable.ic_local_movies_24dp))

        // configurandp data
        val date = viewModel.film!!.releaseDate
        if (date.length == 10)
            film_detail_release_date.text = ("${date.subSequence(8,10)}/${date.subSequence(5,7)}/${date.subSequence(0,4)}")
        else
            film_detail_release_date.text = resources.getString(R.string.detail_date)

        film_detail_runtime.text = ( (filmData.runtime/60).toString() + "h" + (filmData.runtime%60).toString() + "m" )
        film_detail_genres.text = filmData.genres
        film_detail_original_language.text = ( filmData.originalLanguage.toUpperCase() )
        film_detail_popularity.text = ( resources.getString(R.string.detail_popularity) + " " + filmData.popularity )
        film_detail_average.text = (" ${filmData.voteAverage} ")
        film_details_title.text = filmData.title
        film_detail_overview.text = filmData.overview
        film_detail_production_companies.text = filmData.productionCompanies
        film_detail_budget.text = (resources.getString(R.string.detail_budget) + " " + filmData.budget + resources.getString(R.string.detail_dolar) )
        film_detail_revenue.text = (resources.getString(R.string.detail_revenue) + " " + filmData.revenue + resources.getString(R.string.detail_dolar) )
        film_detail_homepage.text = filmData.homepage
    }

    private fun initViews() {
        favoriteButton = film_details_favorite
        favoriteButton.setOnClickListener(this)
    }

    private fun initViewModel() {
        // recuperar fonte de dados
        val application = requireNotNull(this).application
        val dataSource = FilmDatabase.getInstance(application).filmDao
        val filmViewModelFactory = FilmViewModelFactory(dataSource, application, this.lifecycle)
        viewModel = ViewModelProviders.of(this, filmViewModelFactory).get(FilmDetailsViewModel::class.java)
    }

    private fun setViewModel() {
        if (viewModel.isFavorite == null) viewModel.setIsFavorite( intent.getBooleanExtra("favorite", false) )
        if (viewModel.film == null) {
            val film = intent.extras?.getSerializable("film") as FilmData
            viewModel.setFilm( film )
        }
    }

    override fun onClick(v: View?) {
        v?.let {
            if (v.id == favoriteButton.id) {
                if ( !viewModel.isFavorite!! ) {
                    viewModel.setIsFavorite( true )
                    favoriteButton.background = getDrawable( R.drawable.ic_star_favorite_32dp )
                    // 1 = inserir no DB
                    viewModel.setResult( 1 )
                    setResult(viewModel.result, intent.putExtra("film", viewModel.film))
                }
                else {
                    viewModel.setIsFavorite( false )
                    favoriteButton.background = getDrawable( R.drawable.ic_star_not_favorite_32dp )
                    // 2 = remover do DB
                    viewModel.setResult( 2 )
                    setResult(viewModel.result, intent.putExtra("film", viewModel.film))
                }
            }
        }
    }
}
