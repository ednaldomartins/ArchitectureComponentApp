package com.ednaldomartins.architecturecomponentapp.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.ednaldomartins.architecturecomponentapp.data.dao.FilmeDao
import com.ednaldomartins.architecturecomponentapp.data.entity.Filme
import com.ednaldomartins.architecturecomponentapp.respository.local.FilmeDataBase

class FilmeViewModel (app: Application): AndroidViewModel(app) {
    var liveDataFilmes: LiveData<List<Filme>>? = null
    var filmeDao: FilmeDao? = null

    private fun getDao(): FilmeDao? {
        if (filmeDao == null)
            filmeDao = FilmeDataBase.getDataBase(getApplication())?.filmeDao()
        return filmeDao
    }

    fun getFilme(): LiveData<List<Filme>>? {
        if (liveDataFilmes == null)
            liveDataFilmes = getDao()?.listarFilmes()
        return liveDataFilmes
    }
}