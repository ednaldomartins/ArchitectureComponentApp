package com.example.architecturecomponentapp.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer

import com.example.architecturecomponentapp.R
import com.example.architecturecomponentapp.data.entity.FilmData
import com.example.architecturecomponentapp.presentation.activity.FilmDetailsActivity
import com.example.architecturecomponentapp.presentation.adapter.FilmAdapter
import com.example.architecturecomponentapp.presentation.adapter.FilmListAdapter
import com.example.architecturecomponentapp.util.FilmApiStatus

class ApiFilmListFragment: BaseFilmListFragment(),
    FilmListAdapter.OnFilmClickListener,
    View.OnClickListener {

    /********************************************************
     *  variaveis herdadas de BaseFilmListFragment:         *
     *      mFilmRecyclerView: RecyclerView                 *
     *      filmListAdapter: FilmListAdapter                *
     *      mSwipeRefreshLayout: SwipeRefreshLayout         *
     *      filmViewModel: FilmViewModel                    *
     *******************************************************/

    private lateinit var mStatusImageView: ImageView
    private lateinit var mButtonFirstPage: Button
    private lateinit var mButtonBeforePage: Button
    private lateinit var mButtonNextPage: Button
    private lateinit var mButtonLastPage: Button
    private lateinit var mNumberPage: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // recuperar view da super classe
        val view = super.onCreateView(inflater, container, savedInstanceState)
        initViews(view!!)
        // chamar lista de filmes da api
        filmViewModel.requestFilmListApiService()
        connectionStatus()

        // observador da requiscao
        filmViewModel.responseFilmList.observe(this, Observer {
            // atulizar estados dos botoes de alterar paginas
            refreshPageButton(it.page)
            // configurando adapter do RecyclerView
            it.movies?.let { list ->
                filmListAdapter = FilmListAdapter(context = activity, filmListJson = list, onFilmClickListener = this)
                mFilmRecyclerView.adapter = filmListAdapter
            }
        })

        return view
    }

    private fun initViews(v: View) {
        mStatusImageView = v.findViewById(R.id.film_list_api_status)
        // inicializacao dos botoes de navegacao de paginas
        mButtonFirstPage = v.findViewById(R.id.film_list_api_button_first_page)
        mButtonFirstPage.setOnClickListener(this)
        mButtonBeforePage = v.findViewById(R.id.film_list_api_button_before_page)
        mButtonBeforePage.setOnClickListener(this)
        mButtonNextPage = v.findViewById(R.id.film_list_api_button_next_page)
        mButtonNextPage.setOnClickListener(this)
        mButtonLastPage = v.findViewById(R.id.film_list_api_button_last_page)
        mButtonLastPage.setOnClickListener(this)
        // localizando o textview do numero da pagina atual
        mNumberPage = v.findViewById(R.id.film_list_api_number_page)
    }

    private fun refreshPageButton(actualPage: Long) {
        // setando o numero atual da pagina na apresentacao
        mNumberPage.text = actualPage.toString()
        /** sempre que a requisicao muda, a pagina atual podera ser alterada, entao...*/
        // se nao estiver visivel, entao tornar
        if (mButtonFirstPage.visibility != View.VISIBLE) {
            mButtonFirstPage.visibility = View.VISIBLE
            mButtonBeforePage.visibility = View.VISIBLE
        }
        else if (mButtonLastPage.visibility != View.VISIBLE) {
            mButtonLastPage.visibility = View.VISIBLE
            mButtonNextPage.visibility = View.VISIBLE
        }
        /* apos tornar os botoes visiveis, verificar: */
        // se a página 1 for a atual, entao bloquear os botoes de voltar pagina
        if (actualPage == 1L) {
            mButtonFirstPage.visibility = View.INVISIBLE
            mButtonBeforePage.visibility = View.INVISIBLE
        }
        // se a ultima pagina for a atual, entao bloquear os botoes de avancar pagina
        else if (actualPage == filmViewModel.totalPages) {
            mButtonLastPage.visibility = View.INVISIBLE
            mButtonNextPage.visibility = View.INVISIBLE
        }
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText == "" && filmViewModel.query != "") {
            // limpar consulta
            filmViewModel.setSearch(newText)
            filmViewModel.requestFilmListApiService(1)
        }
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        filmViewModel.setSearch(query!!)
        filmViewModel.requestFilmListApiService(1)
        // esconder teclado
        mSearchView.clearFocus()
        return true
    }

    override fun onOptionsMenuClosed(menu: Menu?) {
        Toast.makeText(activity, " teste", Toast.LENGTH_LONG).show()
        filmViewModel.requestFilmListApiService(1)
        super.onOptionsMenuClosed(menu)
    }

    private fun connectionStatus () {
        filmViewModel.status.observe(this, Observer {
            if (it == FilmApiStatus.LOADING ) {
                // ocultar recyclerview e mostrar imagem de carregamento
                mFilmRecyclerView.visibility = View.GONE
                mStatusImageView.visibility = View.VISIBLE
                mStatusImageView.setImageResource( R.drawable.ic_api_request_128dp )
            }
            else if (it == FilmApiStatus.ERRO) {
                // ocultar recyclerview e mostrar imagem de erro no carregamento
                mFilmRecyclerView.visibility = View.GONE
                mStatusImageView.visibility = View.VISIBLE
                mStatusImageView.setImageResource( R.drawable.ic_offline_128dp )
            }
            else if (it == FilmApiStatus.DONE) {
                // ocultar imagem de carregamento e mostrar recyclerview
                mStatusImageView.visibility  = View.GONE
                mFilmRecyclerView.visibility = View.VISIBLE
            }
        })
    }

    /**
     * funcao para controlar o click nos botoes da barra inferior de navegacao de paginas.
     * Sendo possivel ate o momento passar, voltar, ir para a primeira, e ir para ultima pagina.
     */
    override fun onClick(v: View?) {
        v?.let{
            when(it.id) {
                R.id.film_list_api_button_first_page -> {
                    filmViewModel.requestFilmListApiService(1)
                }
                R.id.film_list_api_button_before_page -> {
                    filmViewModel.requestFilmListApiService( filmViewModel.actualPage - 1 )
                }
                R.id.film_list_api_button_next_page -> {
                    filmViewModel.requestFilmListApiService( filmViewModel.actualPage + 1 )
                }
                R.id.film_list_api_button_last_page -> {
                    filmViewModel.requestFilmListApiService( filmViewModel.totalPages )
                }
            }
        }
    }

    override fun onFilmClick(filmId: Long?, position: Int) {
        val intent = Intent(activity, FilmDetailsActivity::class.java)
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
                // verificacao simples para saber se eh um objeto vazio. melhorar isso depois.
                if (it.id == -1L && it.releaseDate == "aaaa-mm-dd") {
                    Toast.makeText(activity, "verifique sua conecção com a internet.", Toast.LENGTH_LONG).show()
                }
                else {
                    intent.putExtra("film", film)
                    intent.putExtra("favorite", false)
                    // inicia activity com resquestCode = 2 -> abrir a partir da API
                    startActivityForResult( intent, 2 )
                }
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