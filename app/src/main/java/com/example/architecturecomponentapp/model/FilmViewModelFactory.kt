package com.example.architecturecomponentapp.model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.example.architecturecomponentapp.data.dao.FilmDao

class FilmViewModelFactory (
    private val databaseDao: FilmDao,
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("cast sem checagem")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilmApiViewModel::class.java))
            return FilmApiViewModel(databaseDao, application) as T
        else if (modelClass.isAssignableFrom(FilmDataViewModel::class.java))
            return FilmDataViewModel(databaseDao, application) as T
        else if (modelClass.isAssignableFrom(FilmDetailsViewModel::class.java))
            return FilmDetailsViewModel(databaseDao, application) as T
        throw IllegalArgumentException("Classe ViewModel desconhecida")
    }

}