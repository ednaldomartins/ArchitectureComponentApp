package com.example.architecturecomponentapp.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

import com.example.architecturecomponentapp.R

class FilmDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_details)

        val filmId: TextView = findViewById(R.id.film_detail_text_view_film_id)

        val id = intent.getStringExtra("filmId")
        filmId.text = id
    }
}
