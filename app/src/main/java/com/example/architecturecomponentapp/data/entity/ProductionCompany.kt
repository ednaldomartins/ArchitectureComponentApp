package com.example.architecturecomponentapp.data.entity

import androidx.room.TypeConverter
import com.squareup.moshi.Json

class ProductionCompany {
    companion object {
        @TypeConverter
        @Json(name = "id")
        fun getId(id: Long): Long {
            return id ?: 0L
        }

        @TypeConverter
        @Json(name = "name")
        fun getName(name: String): String {
            return name ?: ""
        }

        @TypeConverter
        @Json(name = "original_country")
        fun getoriginalCountry(originalCountry: String): String {
            return originalCountry ?: ""
        }
    }
}