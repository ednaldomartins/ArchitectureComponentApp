package com.example.architecturecomponentapp.model

import com.squareup.moshi.Json

class Genres (
    @Json(name = "genres")
    var genreList: Array<Genre>?
)
{
    class Genre {
        @Json(name = "id")
        var id: Int = -1

        @Json(name = "name")
        var name: String = ""
    }
}