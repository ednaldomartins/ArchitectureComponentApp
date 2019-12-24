package com.example.architecturecomponentapp.data.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
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
/*
    @TypeConverters(Genre::class)
    @ColumnInfo(name = "genres")
    @Json(name = "genres")
    var genres: Genre?,
*/
    @ColumnInfo(name = "homepage")
    @Json(name = "homepage")
    var homepage: String = "www.google.com",

    @ColumnInfo(name = "original_language")
    @Json(name = "original_language")
    var originalLanguage: String = "",

    @ColumnInfo(name = "overview")
    @Json(name = "overview")
    var overview: String = "",

    @ColumnInfo(name = "popularity")
    @Json(name = "popularity")
    var popularity: String = "",

    @ColumnInfo(name = "poster_path")
    @Json(name = "poster_path")
    var posterPath: String = "",
/*
    @TypeConverters(ProductionCompany::class)
    @ColumnInfo(name = "production_company")
    @Json(name = "production_company")
    var productionCompany: ProductionCompany?,
*/
    @ColumnInfo(name = "status")
    @Json(name = "status")
    var status: String = "",

    @ColumnInfo(name = "vote_average")
    @Json(name = "vote_average")
    var voteAverage: Float = .0f
) : Serializable