package com.example.architecturecomponentapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import com.example.architecturecomponentapp.data.entity.FilmData

@Dao
interface FilmDao {
    @Insert(onConflict = IGNORE)
    fun insertFilm (filmData: FilmData)

    @Update
    fun updateFilm (filmData: FilmData)

    @Delete
    fun deleteFilm (vararg filmData: FilmData)

    //retorna o filme com o mesmo nome
    @Query("SELECT * FROM film_table WHERE title = :name")
    fun get(name: String): FilmData

    //limpar tabela
    @Query("DELETE FROM film_table")
    fun clear()

    //listar todos os filmes por ordem alfabetica
    @Query("SELECT * FROM film_table ORDER BY title")
    fun filmList(): LiveData<List<FilmData>>    //usando liveData para att em tempo real

    //listar todos os filmes por ordem alfabetica
    @Query("SELECT * FROM film_table ORDER BY id")
    fun filmListSortedById(): LiveData<List<FilmData>>    //usando liveData para att em tempo real

    @Query("SELECT * FROM film_table ORDER BY id DESC LIMIT 1")
    fun getLastFilm(): FilmData?
}