package com.example.architecturecomponentapp.data.database.remote

import com.example.architecturecomponentapp.data.entity.Film
import com.example.architecturecomponentapp.data.entity.MovieList
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private val  moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val  retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(Api.URL)
    .build()

interface FilmApiService {
    // clube da luta (500) nome teste
    @GET("movie/550${Api.UNIQUE_KEY}")
    fun callFilmsApi(): Deferred<Film>  //Defered no lugar de Call para coroutines (para aguardar o resultado sem bloquear outras tarefas)

    // chamar 1 unico filme usando o numero identificador
    @GET("movie/{film}${Api.UNIQUE_KEY}")
    fun callFilmApi(@Path("film") film: String): Deferred<Film>

    // chamar lista de filmes mais populares da API
    @GET("movie/popular${Api.UNIQUE_KEY}")
    fun callPopularFilmListApi(): Deferred<List<Film>>

    @GET("movie/popular${Api.UNIQUE_KEY}${Api.LANGUAGE}")
    fun callPopularMovieListApi(): Deferred<MovieList>
}

object FilmsApi {
    val retrofitService: FilmApiService by lazy {
        retrofit.create(FilmApiService::class.java)
    }
}