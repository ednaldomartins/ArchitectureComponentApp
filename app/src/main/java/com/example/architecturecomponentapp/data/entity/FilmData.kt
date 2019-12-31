package com.example.architecturecomponentapp.data.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

import java.io.Serializable

import com.example.architecturecomponentapp.model.Film

@Entity (tableName = "film_table")
data class FilmData (
    @NonNull @PrimaryKey(autoGenerate = true)
    var idDB: Long = -1L,

    @ColumnInfo(name = "id")
    var id: Long = -1L,

    @ColumnInfo(name = "title")
    var title: String = "Título do Filme",

    @ColumnInfo(name = "release_date")
    var releaseDate: String = "aaaa-mm-dd",

    @ColumnInfo(name = "genres")
    var genres: String = "",

    @ColumnInfo(name = "homepage")
    var homepage: String = "www.google.com",

    @ColumnInfo(name = "original_language")
    var originalLanguage: String = "",

    @ColumnInfo(name = "overview")
    var overview: String = "",

    @ColumnInfo(name = "popularity")
    var popularity: String = "0",

    @ColumnInfo(name = "poster_path")
    var posterPath: String = "",

    @ColumnInfo(name = "status")
    var status: String = "",

    @ColumnInfo(name = "revenue")
    var revenue: Long = -1,

    @ColumnInfo(name = "budget")
    var budget: Long = -1,

    @ColumnInfo(name = "runtime")
    var runtime: Int = -1,

    @ColumnInfo(name = "vote_average")
    var voteAverage: Float = .0f,

    @ColumnInfo(name = "production_companies")
    var productionCompanies: String = ""
) : Film, Serializable