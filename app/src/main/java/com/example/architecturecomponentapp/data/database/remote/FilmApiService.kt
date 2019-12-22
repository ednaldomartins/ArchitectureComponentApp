package com.example.architecturecomponentapp.data.database.remote

import com.example.architecturecomponentapp.data.entity.Film
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private val  moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val  retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Api.URL)
    .build()

interface FilmApiService {
    // clube da luta (500) nome teste
    @GET("movie/550${Api.UNIQUE_KEY}")
    fun callFilmsApi(): Call<Film>
}

object FilmsApi {
    val retrofitService: FilmApiService by lazy {
        retrofit.create(FilmApiService::class.java)
    }
}