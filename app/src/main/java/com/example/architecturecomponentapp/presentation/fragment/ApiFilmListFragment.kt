package com.example.architecturecomponentapp.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.architecturecomponentapp.R
import com.example.architecturecomponentapp.data.database.local.FilmDatabase
import com.example.architecturecomponentapp.data.entity.FilmData
import com.example.architecturecomponentapp.model.FilmViewModel
import com.example.architecturecomponentapp.model.FilmViewModelFactory
import com.example.architecturecomponentapp.presentation.activity.FilmDetailsActivity
import com.example.architecturecomponentapp.presentation.adapter.FilmAdapter
import com.example.architecturecomponentapp.presentation.adapter.FilmListAdapter

class ApiFilmListFragment: Fragment(), FilmListAdapter.OnFilmClickListener, SearchView.OnQueryTextListener{

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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_main, menu)
        val menuSearchItem: MenuItem? = menu?.findItem(R.id.search_menu)
        val searchView: SearchView = menuSearchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        //return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        Toast.makeText(activity, "buscando por $query", Toast.LENGTH_LONG ).show()
        filmViewModel.searchFilmListApiService(query!!)
        return true
    }

    override fun onFilmClick(filmId: Long?, position: Int) {
        val intent: Intent = Intent(activity, FilmDetailsActivity::class.java)
        // verificar se esta nos favoritos do database do usuario
        val isFavorite = filmViewModel.isFavoriteFilm(filmId!!)
        if (isFavorite) {
            val film = filmViewModel.getFilm(filmId)
            intent.putExtra("film", film)
            intent.putExtra("favorite", true)
            // inicia activity com resquestCode = 1 -> abrir a partir do DB
            startActivityForResult( intent, 1 )
        }
        else {
            // localizar film na API usando o id
            filmViewModel.requestFilmApiService(filmId)
            // apos os dados serem recuperados da API instanciar film
            var film: FilmData? = null
            filmViewModel.responseFilmJson.value?.let { film = FilmAdapter.adaptJsonToData(it) }
            film?.let {
                intent.putExtra("film", film)
                intent.putExtra("favorite", false)
                // inicia activity com resquestCode = 2 -> abrir a partir da API
                startActivityForResult( intent, 2 )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // resultCode = 1 -> o filme foi adicionado aos favoritos
        if (requestCode == 2 && resultCode == 1) {
            val film = data!!.extras?.getSerializable("film") as FilmData
            filmViewModel.insertFilm(film)
        }
        // resultCode = 2 -> o filme foi removido dos favoritos
        else if (requestCode == 1 && resultCode == 2) {
            val film = data!!.extras?.getSerializable("film") as FilmData
            filmViewModel.deleteFilm(film)
        }
    }

}