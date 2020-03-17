package com.example.architecturecomponentapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE

import com.example.architecturecomponentapp.domain.entity.FilmData

@Dao
interface FilmDao {
    /********************************************************************************************
     * Metodo para executar comandos basicos para o Database                                    *
     *******************************************************************************************/
    @Insert(onConflict = IGNORE)
    fun insertFilm (filmData: FilmData)

    @Update(onConflict = REPLACE)
    fun updateFilm (filmData: FilmData)

    @Delete
    fun deleteFilm (vararg filmData: FilmData)

    // limpar tabela do database
    @Query("DELETE FROM film_table")
    fun clear()

    /********************************************************************************************
    * Metodos de retorno do Database                                                            *
    ********************************************************************************************/
    // retorna o filme com o mesmo nome caso ele esteja contido no database
    @Query("SELECT * FROM film_table WHERE title = :name")
    fun get(name: String): FilmData

    // retorna o filme com o mesmo id caso ele esteja contido no database
    @Query("SELECT * FROM film_table WHERE id = :id")
    fun get(id: Long): FilmData

    // retorna true caso o filme exista no database
    @Query("SELECT * FROM film_table WHERE id == :id")
    fun getFavorite(id: Long): Boolean

    // retorna o ultimo filme adicionado ao database
    @Query("SELECT * FROM film_table ORDER BY id DESC LIMIT 1")
    fun getLastFilm(): FilmData?

    // listar todos os filmes por ordem alfabetica
    @Query("SELECT * FROM film_table ORDER BY title")
    fun filmList(): LiveData<List<FilmData>>    //usando liveData para att em tempo real

    // listar todos os filmes por ordem de identificacao
    @Query("SELECT * FROM film_table ORDER BY id")
    fun filmListSortedById(): LiveData<List<FilmData>>    //usando liveData para att em tempo real

    @Query("SELECT count(*) FROM film_table")
    fun getFilmsCount(): Int
}