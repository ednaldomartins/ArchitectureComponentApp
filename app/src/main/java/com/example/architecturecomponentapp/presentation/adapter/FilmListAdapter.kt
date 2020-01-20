package com.example.architecturecomponentapp.presentation.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.architecturecomponentapp.R
import com.example.architecturecomponentapp.util.Api
import com.example.architecturecomponentapp.data.entity.FilmData
import com.example.architecturecomponentapp.model.FilmsJson

class FilmListAdapter (
    private var context: Context?,
    private var filmListJson: Array<FilmsJson.FilmJson>,
    private var filmListData: List<FilmData>? = null,
    private var onFilmClickListener: OnFilmClickListener? = null
    )
    : RecyclerView.Adapter<FilmListAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder{
        //converter para FilmJson se a lista de filmes vier no tipo FilmData
        filmListData?.let {
            for (i in it.indices)
                filmListJson[i] = FilmAdapter.adaptDataToJson(it[i])
        }

        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_film_list, parent, false)

        return MyViewHolder(view, onFilmClickListener)
    }

    override fun getItemCount(): Int {
        return filmListJson.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val film: FilmsJson.FilmJson = filmListJson[position]
        // guardar o ID para se caso o item seja clicado, sera feito uma requisicao a patir do ID.
        holder.filmId = film.id ?: -1

        // se tivermos um caminho de uma foto salva, entao...
        if (!film.posterPath.isNullOrEmpty()) {
            val imgUri = Uri.parse( Api.URL_IMAGE + film.posterPath )
            Glide.with(holder.filmPoster.context).load(imgUri).into(holder.filmPoster)
        }
        else
            holder.filmPoster.setImageDrawable(context?.getDrawable(R.drawable.ic_local_movies_24dp))

        // nome do filme. alterar para filmes com nome muito grande para nao passar de duas linhas
        if (film.title?.length ?: 0 > 35)
            holder.filmTitle.text = ("${film.title?.subSequence(0,35)}...")
        else
            holder.filmTitle.text = film.title

        // alterar o formato da data para dd/mm/aaaa
        val date = film.releaseDate ?: ""
        if (date.length == 10) {
            holder.filmReleaseDate.text =
                ("${date.subSequence(8,10)}/${date.subSequence(5,7)}/${date.subSequence(0,4)}")
        }
        else holder.filmReleaseDate.text = "dd/mm/aaaa"

        // vizualizacoes do filme na API
        holder.filmPopularity.text = film.popularity ?: "0"
        // media obtida pela API
        holder.filmVoteAverage.text = film.voteAverage?.toString() ?: "0"
    }

    class MyViewHolder(v: View, var onFilmClickListener: OnFilmClickListener? = null) : RecyclerView.ViewHolder(v), View.OnClickListener {
        var filmId: Long? = null
        var filmPoster: ImageView = v.findViewById(R.id.adapter_film_list_image_film)
        var filmTitle: TextView = v.findViewById(R.id.adapter_film_list_text_title)
        var filmReleaseDate: TextView = v.findViewById(R.id.adapter_film_list_text_release_date)
        var filmPopularity: TextView = v.findViewById(R.id.adapter_film_list_text_popularity)
        var filmVoteAverage: TextView = v.findViewById(R.id.adapter_film_list_text_vote_average)

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onFilmClickListener?.onFilmClick(filmId)
        }
    }

    interface OnFilmClickListener {
        fun onFilmClick(filmId: Long?)
    }
}