package com.example.architecturecomponentapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import com.example.architecturecomponentapp.data.entity.Film

@Dao
interface FilmDao {
    @Insert(onConflict = IGNORE)
    fun insertFilm (film: Film)

    @Update
    fun updateFilm (film: Film)

    @Delete
    fun deleteFilm (vararg film: Film)

    //retorna o filme com o mesmo nome
    @Query("SELECT * FROM film_table WHERE title = :name")
    fun get(name: String): Film

    //limpar tabela
    @Query("DELETE FROM film_table")
    fun clear()

    //listar todos os filmes por ordem alfabetica
    @Query("SELECT * FROM film_table ORDER BY title")
    fun filmList(): LiveData<List<Film>>    //usando liveData para att em tempo real

    //listar todos os filmes por ordem alfabetica
    @Query("SELECT * FROM film_table ORDER BY id")
    fun filmListSortedById(): LiveData<List<Film>>    //usando liveData para att em tempo real

    @Query("SELECT * FROM film_table ORDER BY id DESC LIMIT 1")
    fun getLastFilm(): Film?
}