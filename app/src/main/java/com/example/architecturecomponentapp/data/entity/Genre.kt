package com.example.architecturecomponentapp.data.entity

import androidx.room.TypeConverter
import com.squareup.moshi.Json

@Json(name = "genre")
class Genre {
    companion object {
        @TypeConverter
        @Json(name = "name")
        fun getName(name: String): String {
            return name ?: ""
        }
    }
}