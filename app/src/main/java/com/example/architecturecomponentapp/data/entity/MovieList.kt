package com.example.architecturecomponentapp.data.entity

import com.squareup.moshi.Json

class MovieList (
    @Json(name = "results")
    var movies: List<Film>? = null
)