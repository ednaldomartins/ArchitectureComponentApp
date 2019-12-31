package com.example.architecturecomponentapp.presentation.adapter

import com.example.architecturecomponentapp.data.entity.FilmData
import com.example.architecturecomponentapp.model.FilmsJson
import com.example.architecturecomponentapp.model.Genres
import com.example.architecturecomponentapp.model.ProductionCompanies

class FilmAdapter {
    companion object {
        fun adaptJsonToData (filmJson: FilmsJson.FilmJson) : FilmData{
            // converter array de genres para string
            val genres = filmJson.genres
            val genresString = convertGenreArrayToString(genres)
            // converter array de companies para string
            val companies = filmJson.productionCompanies
            val companiesString = convertCompanyArrayToString(companies)
            /*  apos a conversao, criamos o FilmData setando todos os paramentros do FilmJson
                mais as duas strings convertidas para serem apresentadas na tela de detalhes do filme,
                e também poder salvar o objeto FilmData no banco de dados Room.
             */
            return FilmData(
                id = filmJson.id,
                title = filmJson.title,
                releaseDate = filmJson.releaseDate,
                genres = genresString,
                homepage = filmJson.homepage,
                originalLanguage = filmJson.originalLanguage,
                overview = filmJson.overview,
                popularity = filmJson.popularity,
                posterPath = filmJson.posterPath,
                status = filmJson.status,
                revenue = filmJson.revenue,
                budget = filmJson.budget,
                runtime = filmJson.runtime,
                voteAverage = filmJson.voteAverage,
                productionCompanies = companiesString
            )
        }

        fun adaptDataToJson (filmData: FilmData) : FilmsJson.FilmJson {
            // converter de string para array de genres
            val genresString = filmData.genres
            val genres = convertStringToGenreArray(genresString)
            // converter de string para array de companies
            val companiesString = filmData.productionCompanies
            val companies = convertStringToCompanyArray(companiesString)
            /*  apos a conversao, criamos o FilmJson setando todos os paramentros do FilmData
                mais as duas arrays convertidas para serem apresentadas no RecyclerView.
             */
            return FilmsJson.FilmJson(
                id = filmData.id,
                title = filmData.title,
                releaseDate = filmData.releaseDate,
                genres = genres,
                homepage = filmData.homepage,
                originalLanguage = filmData.originalLanguage,
                overview = filmData.overview,
                popularity = filmData.popularity,
                posterPath = filmData.posterPath,
                status = filmData.status,
                revenue = filmData.revenue,
                budget = filmData.budget,
                runtime = filmData.runtime,
                voteAverage = filmData.voteAverage,
                productionCompanies = companies
            )
        }

        /**
         * Metodos para conversao de string para array, e array para string. conversores
         * para os objetos Genres e ProductionCompanies.
         */
        private fun convertGenreArrayToString (genres: Array<Genres.Genre>?): String {
            val genresSize = genres?.size  ?: 0
            var genresText = ""
            for (i in 0 until genresSize)
                genresText += genres!![i].name + ","

            genresText = genresText.substring(0, genresSize-2) + "."
            return genresText
        }

        private fun convertStringToGenreArray (genres: String): Array<Genres.Genre> {
            val newGenres = genres.substring(0,genres.length-1)//remover o '.'
            val arrString = newGenres.split(',')
            val arrGenres = arrayOf(Genres.Genre())
            for (i in arrString.indices)
                arrGenres[i].name = arrString[i]

            return arrGenres
        }

        private fun convertCompanyArrayToString (companies: Array<ProductionCompanies.ProductionCompany>?) : String {
            val companiesSize = companies?.size  ?: 0
            var companiesText = ""
            for (i in 0 until companiesSize)
                companiesText += companies!![i].name + ", "

            companiesText = companiesText.substring(0, companiesSize-2) + "."
            return companiesText
        }

        private fun convertStringToCompanyArray (companies: String): Array<ProductionCompanies.ProductionCompany> {
            val newCompanies = companies.substring(0,companies.length-1)//remover o '.'
            val arrString = newCompanies.split(',')
            val arrCompanies = arrayOf(ProductionCompanies.ProductionCompany())
            for (i in arrString.indices)
                arrCompanies[i].name = arrString[i]

            return arrCompanies
        }

    }

}