package com.example.architecturecomponentapp.model

import com.squareup.moshi.Json
import java.io.Serializable

class FilmsJson (
    @Json(name = "results")
    var movies: Array<FilmJson>? = null
)
{
    class FilmJson (
        @Json(name = "id")
        var id: Long = -1L,

        @Json(name = "title")
        var title: String = "Título do Filme",

        @Json(name = "release_date")
        var releaseDate: String = "aaaa-mm-dd",

        @Json(name = "genres")
        var genres: Array<Genres.Genre>?,

        @Json(name = "homepage")
        var homepage: String = "www.google.com",

        @Json(name = "original_language")
        var originalLanguage: String = "",

        @Json(name = "overview")
        var overview: String = "",

        @Json(name = "popularity")
        var popularity: String = "0",

        @Json(name = "poster_path")
        var posterPath: String = "",

        @Json(name = "status")
        var status: String = "",

        @Json(name = "revenue")
        var revenue: Long = -1,

        @Json(name = "budget")
        var budget: Long = -1,

        @Json(name = "runtime")
        var runtime: Int = -1,

        @Json(name = "vote_average")
        var voteAverage: Float = .0f,

        @Json(name = "production_company")
        var productionCompanies: Array<ProductionCompanies.ProductionCompany>?
    ) : Film, Serializable
}