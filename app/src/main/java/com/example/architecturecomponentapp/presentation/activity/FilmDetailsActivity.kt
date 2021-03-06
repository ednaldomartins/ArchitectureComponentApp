package com.example.architecturecomponentapp.presentation.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import kotlinx.android.synthetic.main.activity_film_details.*

import com.bumptech.glide.Glide

import com.example.architecturecomponentapp.R
import com.example.architecturecomponentapp.data.database.local.FilmDatabase
import com.example.architecturecomponentapp.util.Api
import com.example.architecturecomponentapp.domain.entity.FilmData
import com.example.architecturecomponentapp.domain.viewmodel.FilmDetailsViewModel
import com.example.architecturecomponentapp.domain.viewmodel.FilmViewModelFactory
import com.example.architecturecomponentapp.presentation.adapter.FilmAdapter

class FilmDetailsActivity : AppCompatActivity(), View.OnClickListener, LifecycleOwner {
    // view
    private lateinit var favoriteButton: Button
    private lateinit var homepageText: TextView
    // data
    private lateinit var viewModel: FilmDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_details)
        //  iniciar view e viewmodel
        initViews()
        initViewModel()
    }

    override fun onResume() {
        super.onResume()

        if (viewModel.isFavorite!!) {
            film_details_favorite.background = getDrawable(R.drawable.ic_star_favorite_32dp)
            // enviar dados de apresentacao a apartir do DB
            submitDetails( viewModel.film!! )
        }
        else {
            film_details_favorite.background = getDrawable(R.drawable.ic_star_not_favorite_32dp)
            // se for diferente de nulo entao a activity ja passou pelo estado OnStop e tem uma
            // instancia de filme ja guarda na viewmodel. entao pode submeter diretamente
            if (viewModel.film != null) {
                submitDetails( viewModel.film!! )
            }
            else {
                viewModel.responseFilmJson.observe(this, Observer {
                    viewModel.setFilm ( FilmAdapter.adaptJsonToData(it) )
                    // aplicar os dados recebido na activity de detalhes
                    submitDetails( viewModel.film!! )
                })
            }
        }

    }

    private fun initViews() {
        favoriteButton = film_details_favorite
        favoriteButton.setOnClickListener(this)
        favoriteButton.isClickable = false
        homepageText = film_detail_homepage
        homepageText.setOnClickListener(this)
    }

    private fun initViewModel() {
        // recuperar fonte de dados
        val application = requireNotNull(this).application
        val dataSource = FilmDatabase.getInstance(application).filmDao
        val filmViewModelFactory =
            FilmViewModelFactory(
                dataSource,
                application
            )
        viewModel = ViewModelProvider(this, filmViewModelFactory).get(FilmDetailsViewModel::class.java)

        //  se film for nulo, vamos recuperar o film
        if (viewModel.film == null) {
            //  seta os valores na viewmodel
            viewModel.setId( intent.getLongExtra("filmId", -1L ) )
            viewModel.setIsFavorite( viewModel.isFavoriteFilm( viewModel.filmId!! ) )
            //  verificar se esta no banco de dados
            if ( viewModel.isFavorite!! )
                viewModel.setFilm( viewModel.getFilmDatabase( viewModel.filmId!! ) )
            else
                viewModel.requestFilmApiService( viewModel.filmId!! )
        }
        //  enviar o lifecycle da nova activity construida/reconstruida
        viewModel.setLifecycle(this.lifecycle)
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
        //  tornar botão favorito clicavel apenas quando o filme for buscado com sucesso
        favoriteButton.isClickable = (viewModel.film!!.id != -1L)

    }

    override fun onClick(v: View?) {
        v?.let {
            when {
                v.id == favoriteButton.id -> {
                    if (!viewModel.isFavorite!!) {
                        viewModel.setIsFavorite(true)
                        favoriteButton.background = getDrawable(R.drawable.ic_star_favorite_32dp)
                    } else {
                        viewModel.setIsFavorite(false)
                        favoriteButton.background =
                            getDrawable(R.drawable.ic_star_not_favorite_32dp)
                    }
                }
                v.id == homepageText.id -> {
                    //  criar intent
                    val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse( homepageText.text.toString() ))
                    //  verificar se ha activities que possam responder a intent
                    val activities: List<ResolveInfo> =
                        packageManager.queryIntentActivities(webIntent, PackageManager.MATCH_DEFAULT_ONLY)
                    //  chamar activity se houver disponivel
                    if (activities.isNotEmpty())
                        startActivity(webIntent)
                }
            }
        }
    }
}
