package com.ednaldomartins.architecturecomponentapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import com.ednaldomartins.architecturecomponentapp.data.entity.Filme

@Dao
interface FilmeDao {
    @Insert(onConflict = IGNORE)
    fun insertFilme (filme: Filme)

    @Update
    fun updateFilme (filme: Filme)

    @Delete
    fun deleteFilme (vararg filme: Filme)

    //listar todos os filmes por ordem alfabetica
    @Query("SELECT * FROM Filme ORDER BY nome")
    fun listarFilmes(): LiveData<List<Filme>>
}