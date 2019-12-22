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
    var id: Long = 0L,

    @ColumnInfo(name = "title")
    @Json(name = "title")
    var title: String = "",

    @ColumnInfo(name = "release_date")
    @Json(name = "release_date")
    var releaseData: String = "-1"
) : Serializable