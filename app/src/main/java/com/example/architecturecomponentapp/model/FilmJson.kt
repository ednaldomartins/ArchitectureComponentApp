package com.example.architecturecomponentapp.model

import androidx.annotation.NonNull
import com.squareup.moshi.Json
import java.io.Serializable

class FilmJson (
    @NonNull
    @Json(name = "id")
    var id: Long = -1L,

    @Json(name = "title")
    var title: String = "Título do Filme",

    @Json(name = "release_date")
    var releaseData: String = "????-??-??",

    @Json(name = "genre_ids")
    var genres: Array<String>?,

    @Json(name = "homepage")
    var homepage: String = "www.google.com",

    @Json(name = "original_language")
    var originalLanguage: String = "",

    @Json(name = "overview")
    var overview: String = "",

    @Json(name = "popularity")
    var popularity: String = "",

    @Json(name = "poster_path")
    var posterPath: String = "",

    @Json(name = "status")
    var status: String = "",

    @Json(name = "vote_average")
    var voteAverage: Float = .0f,

    @Json(name = "production_company")
    var productionCompany: Array<String>?
) : Serializable