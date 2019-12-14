package com.example.architecturecomponentapp.model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ednaldomartins.architecturecomponentapp.data.dao.FilmDao

class FilmViewModelFactory (
    private val databaseDao: FilmDao,
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("cast sem checagem")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilmViewModel::class.java))
            return FilmViewModel(databaseDao, application) as T
        throw IllegalArgumentException("Classe ViewModel desconhecida")
    }

}