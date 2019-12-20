package com.example.architecturecomponentapp.data.database.remote

import com.example.architecturecomponentapp.data.entity.Film
import com.example.architecturecomponentapp.R
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private const val URL_API = "http://www.omdbapi.com/"
private val  retrofit = Retrofit.Builder()
    .baseUrl(URL_API)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface FilmApiService {
    // matrix nome teste
    @GET("?t=matrix&" + R.string.api_key_movies)
    fun callFilmsApi() : Call<Film>
}

object FilmsApi {
    val filmApiService: FilmApiService by lazy {
        retrofit.create(FilmApiService::class.java)
    }
}