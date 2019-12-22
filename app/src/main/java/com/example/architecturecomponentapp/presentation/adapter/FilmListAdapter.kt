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
        return filmList.size!!
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val film: Film = filmList.get(position)
        //falta a capa do filme
        holder.filmImageView.setImageResource(R.drawable.ic_movie_black_24dp)
        holder.filmName.text = film.title
        holder.filmYear.text = film.releaseData
    }

    class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        lateinit var filmImageView: ImageView
        lateinit var filmName: TextView
        lateinit var filmYear: TextView

        init {
            filmImageView = v.findViewById(R.id.adapter_film_list_image_film)
            filmName = v.findViewById(R.id.adapter_film_list_text_name)
            filmYear = v.findViewById(R.id.adapter_film_list_text_year)
        }
    }
}