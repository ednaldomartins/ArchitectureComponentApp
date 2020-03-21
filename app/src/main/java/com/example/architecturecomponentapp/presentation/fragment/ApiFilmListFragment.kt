package com.example.architecturecomponentapp.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.example.architecturecomponentapp.R
import com.example.architecturecomponentapp.domain.viewmodel.FilmApiViewModel
import com.example.architecturecomponentapp.presentation.adapter.FilmListAdapter
import com.example.architecturecomponentapp.util.FilmApiStatus

class ApiFilmListFragment: BaseFilmListFragment() {

    /********************************************************
     *  variaveis herdadas de BaseFilmListFragment:         *
     *      mFilmRecyclerView: RecyclerView                 *
     *      filmListAdapter: FilmListAdapter                *
     *      mSwipeRefreshLayout: SwipeRefreshLayout         *
     *      filmViewModelFactory: FilmViewModelFactory      *
     *******************************************************/

    private lateinit var mStatusImageView: ImageView

    private lateinit var filmViewModel: FilmApiViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // recuperar view da super classe, e instanciar views adicionais desse fragment
        val view = super.onCreateView(inflater, container, savedInstanceState)
        filmViewModel = ViewModelProvider(activity!!, super.filmViewModelFactory).get(
            FilmApiViewModel::class.java)
        initViews(view!!)
        super.setViewModel(filmViewModel)

        // chamar lista de filmes da api
        filmViewModel.setPresentation()
        connectionStatus()

        // observador da requiscao
        filmViewModel.responseFilmList.observe(viewLifecycleOwner, Observer {
            // atulizar estados dos botoes de alterar paginas
            refreshPageButton(it.page, it.totalPages)
            // configurando adapter do RecyclerView
            it.movies?.let { list ->
                filmListAdapter = FilmListAdapter(context = activity, filmListJson = list, onFilmClickListener = this)
                mFilmRecyclerView.adapter = filmListAdapter
            }
        })

        return view
    }

    //  view para imageview que mostra status da conexao
    private fun initViews(v: View) {
        mStatusImageView = v.findViewById(R.id.film_list_status)
    }

    //  atualizar pagina e no super parar animacao do onrefresh
    override fun onRefresh() {
        filmViewModel.setPresentation()
        super.onRefresh()
    }

    //  se o texto for limpado e a query atual nao, entao limpa query e chama pagina de destaques
    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText == "" && filmViewModel.query != "") {
            // limpar consulta
            filmViewModel.setSearch(newText)
            filmViewModel.setPresentation(1)
        }
        return true
    }

    //  enviar submissao feita pelo usuario para a pesquisa
    override fun onQueryTextSubmit(query: String?): Boolean {
        filmViewModel.setSearch(query!!)
        filmViewModel.setPresentation(1)
        // esconder teclado
        mSearchView.clearFocus()
        return true
    }

    //  ao fechar o menu chamar pagina de destaques
    override fun onOptionsMenuClosed(menu: Menu) {
        filmViewModel.setPresentation(1)
        super.onOptionsMenuClosed(menu)
    }

    //  setar imagem de status da conexao sempre que o estado da conexao mudar
    private fun connectionStatus () {
        filmViewModel.status.observe(viewLifecycleOwner, Observer {
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

}
