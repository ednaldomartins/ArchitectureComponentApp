package com.example.architecturecomponentapp.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.architecturecomponentapp.R
import com.example.architecturecomponentapp.data.entity.Film

class FilmListAdapter (var filmList: List<Film>, context: Context?)
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
        val film: Film = filmList.get(position)
        //falta a capa do filme
        //holder.filmPoster.setImageResource(R.drawable.ic_movie_black_24dp)
        holder.filmTitle.text = film.title
        holder.filmReleaseDate.text = film.releaseData
        holder.filmPopularity.text = film.popularity
        holder.filmVoteAverage.text = film.voteAverage.toString()
    }

    class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var filmPoster: ImageView
        var filmTitle: TextView
        var filmReleaseDate: TextView
        var filmPopularity: TextView
        var filmVoteAverage: TextView

        init {
            filmPoster = v.findViewById(R.id.adapter_film_list_image_film)
            filmTitle = v.findViewById(R.id.adapter_film_list_text_title)
            filmPopularity = v.findViewById(R.id.adapter_film_list_text_popularity)
            filmReleaseDate = v.findViewById(R.id.adapter_film_list_text_release_date)
            filmVoteAverage = v.findViewById(R.id.adapter_film_list_text_vote_average)
        }
    }
}