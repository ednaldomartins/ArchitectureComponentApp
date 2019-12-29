package com.example.architecturecomponentapp.data.database.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import com.example.architecturecomponentapp.data.dao.FilmDao
import com.example.architecturecomponentapp.data.entity.FilmData

@Database (entities = [FilmData::class], version = 5, exportSchema = false)
abstract class FilmDatabase: RoomDatabase() {
    abstract val filmDao: FilmDao

    companion object {
        @Volatile
        private var INSTANCE: FilmDatabase? = null

        fun getInstance (context: Context): FilmDatabase {
            synchronized(this) {
                var instance =
                    INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext, FilmDatabase::class.java, "filme_database")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}