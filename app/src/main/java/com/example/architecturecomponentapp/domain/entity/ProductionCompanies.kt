package com.example.architecturecomponentapp.domain.entity

import com.squareup.moshi.Json

class ProductionCompanies(
    @Json(name = "production_companies")
    var productionList: Array<ProductionCompany>?
) {
    class ProductionCompany(
        @Json(name = "id")
        var id: Int = -1,

        @Json(name = "name")
        var name: String = "",

        @Json(name = "origin_country")
        var originCountry: String = ""
    )
}