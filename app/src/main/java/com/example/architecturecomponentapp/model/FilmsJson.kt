package com.example.architecturecomponentapp.model

import androidx.annotation.NonNull
import com.squareup.moshi.Json
import java.io.Serializable

class FilmsJson (
    @Json(name = "results")
    var movies: List<FilmJson>? = null
)
{
    class FilmJson (
        @Json(name = "id")
        var id: Long = -1L,

        @Json(name = "title")
        var title: String = "Título do Filme",

        @Json(name = "release_date")
        var releaseData: String = "????-??-??",

        @Json(name = "genres")
        var genres: Genres?,

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

        @Json(name = "revenue")
        var revenue: Int = -1,

        @Json(name = "budget")
        var budget: Int = -1,

        @Json(name = "runtime")
        var runtime: Int = -1,

        @Json(name = "vote_average")
        var voteAverage: Float = .0f,

        @Json(name = "production_company")
        var productionCompanies: ProductionCompanies?
    ) : Film, Serializable
}