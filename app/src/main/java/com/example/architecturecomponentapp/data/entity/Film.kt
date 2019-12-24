package com.example.architecturecomponentapp.data.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.io.Serializable

@Entity (tableName = "film_table")
data class Film(
    @NonNull @PrimaryKey(autoGenerate = true)
    @Json(name = "id")
    var id: Long = -1L,

    @ColumnInfo(name = "title")
    @Json(name = "title")
    var title: String = "Título do Filme",

    @ColumnInfo(name = "release_date")
    @Json(name = "release_date")
    var releaseData: String = "????-??-??",

    @ColumnInfo(name = "genres")
    @Json(name = "genres")
    var genres: Genre?,

    @ColumnInfo(name = "homepage")
    @Json(name = "homepage")
    var homepage: String = "www.google.com",

    @ColumnInfo(name = "original_language")
    @Json(name = "original_language")
    var originalLanguage: String = "",

    @ColumnInfo(name = "overwiew")
    @Json(name = "overwiew")
    var overwiew: String = "",

    @ColumnInfo(name = "popularity")
    @Json(name = "popularity")
    var popularity: String = "",

    @ColumnInfo(name = "poster_path")
    @Json(name = "poster_path")
    var posterPath: String = "",

    @ColumnInfo(name = "production_company")
    @Json(name = "production_company")
    var productionCompany: ProductionCompany?,

    @ColumnInfo(name = "status")
    @Json(name = "status")
    var status: String = "",

    @ColumnInfo(name = "vote_average")
    @Json(name = "vote_average")
    var voteAverage: Float = .0f
) : Serializable