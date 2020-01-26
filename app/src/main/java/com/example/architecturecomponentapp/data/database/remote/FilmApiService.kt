package com.example.architecturecomponentapp.data.database.remote

import com.example.architecturecomponentapp.model.FilmsJson
import com.example.architecturecomponentapp.model.Genres
import com.example.architecturecomponentapp.util.Api
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private val  moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val  retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(Api.URL)
    .build()

interface FilmApiService {
    // chamar lista de generos
    @GET("genre/movie/list${Api.UNIQUE_KEY}${Api.LANGUAGE}")
    fun callGenreMovieApi(): Deferred<Genres>

    // chamar 1 unico filme usando o ID
    @GET("movie/{filmId}${Api.UNIQUE_KEY}${Api.LANGUAGE}")
    fun callFilmApi(@Path("filmId") filmId: Long): Deferred<FilmsJson.FilmJson>

    // chamar lista de filmes mais populares da API
    @GET("movie/popular${Api.UNIQUE_KEY}${Api.LANGUAGE}")
    fun callPopularMovieListApi(@Query("page") page: Int): Deferred<FilmsJson>

    // buscar lista de filmes na API pelo token search
    @GET("search/movie${Api.UNIQUE_KEY}${Api.LANGUAGE}")
    fun callSearchMovieList(
        @Query("page") page: Int,
        @Query("query") search: String
    ): Deferred<FilmsJson>

    // chamar lista de filmes por categoria
    @GET("list/{genre}${Api.UNIQUE_KEY}${Api.LANGUAGE}")
    fun callMovieListByGenreApi (@Path("genre") genre: String): Deferred<FilmsJson>

}

object FilmsApi {
    val retrofitService: FilmApiService by lazy {
        retrofit.create(FilmApiService::class.java)
    }
}