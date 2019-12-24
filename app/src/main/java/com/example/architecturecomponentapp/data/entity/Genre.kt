package com.example.architecturecomponentapp.data.entity

import com.squareup.moshi.Json

class Genre (
    @Json(name = "id")
    var id: Long = 0L,

    @Json(name = "name")
    var name: String = ""
)