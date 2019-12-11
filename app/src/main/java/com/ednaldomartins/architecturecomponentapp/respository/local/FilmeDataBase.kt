package com.ednaldomartins.architecturecomponentapp.respository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ednaldomartins.architecturecomponentapp.data.dao.FilmeDao
import com.ednaldomartins.architecturecomponentapp.data.entity.Filme

//padrao Singleton para acesso unico
@Database(entities = [Filme::class], version = 1)
abstract class FilmeDataBase: RoomDatabase() {
    abstract fun filmeDao(): FilmeDao

    companion object {
        private val DB_NOME = "dbFilme"
        private var instance: FilmeDataBase? = null

        fun getDataBase (context: Context): FilmeDataBase? {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext, FilmeDataBase::class.java, DB_NOME)
                    .allowMainThreadQueries()
                    .build()
            }
            return instance
        }

        fun destroyInstance () {
            instance = null
        }
    }
}