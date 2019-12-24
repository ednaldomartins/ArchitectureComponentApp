package com.example.architecturecomponentapp.data.entity

import com.squareup.moshi.Json

class ProductionCompany (
    @Json(name = "id")
    var id: Long = -1L,

    @Json(name = "name")
    var name: String = "",

    @Json(name = "original_country")
    var originalCountry: String = ""
)