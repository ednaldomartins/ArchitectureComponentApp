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
import com.example.architecturecomponentapp.data.database.remote.Api
import com.example.architecturecomponentapp.model.FilmsJson

class FilmListAdapter (private var filmList: List<FilmsJson.FilmJson>,private var context: Context?)
    : RecyclerView.Adapter<FilmListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder{
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_film_list, parent, false)

        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filmList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val film: FilmsJson.FilmJson = filmList[position]

        // se tivermos um caminho de uma foto salva, entao...
        if (film.posterPath != "") {
            val imgUri = Uri.parse( Api.URL_IMAGE + film.posterPath )
            Glide.with(holder.filmPoster.context).load(imgUri).into(holder.filmPoster)
        }

        // nome do filme. alterar para filmes com nome muito grande para nao passar de duas linhas
        if (film.title.length > 35)
            holder.filmTitle.text = ("${film.title.subSequence(0,35)}...")
        else
            holder.filmTitle.text = film.title

        // alterar o formato da data para dd/mm/aaaa
        val date = film.releaseData
        holder.filmReleaseDate.text =
            ("${date.subSequence(8,10)}/${date.subSequence(5,7)}/${date.subSequence(0,4)}")

        // vizualizacoes do filme na API
        holder.filmPopularity.text = film.popularity
        // media obtida pela API
        holder.filmVoteAverage.text = film.voteAverage.toString()
    }

    class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var filmPoster: ImageView = v.findViewById(R.id.adapter_film_list_image_film)
        var filmTitle: TextView = v.findViewById(R.id.adapter_film_list_text_title)
        var filmReleaseDate: TextView = v.findViewById(R.id.adapter_film_list_text_release_date)
        var filmPopularity: TextView = v.findViewById(R.id.adapter_film_list_text_popularity)
        var filmVoteAverage: TextView = v.findViewById(R.id.adapter_film_list_text_vote_average)
    }
}