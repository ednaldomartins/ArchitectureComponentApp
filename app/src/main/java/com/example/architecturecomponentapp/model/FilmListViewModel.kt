package com.example.architecturecomponentapp.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel

abstract class FilmListViewModel(app: Application): AndroidViewModel(app) {

    //  pagina atual
    protected var _actualPage: Int = 1
    val actualPage: Int get() = _actualPage
    //  total de paginas
    protected var _totalPages: Int = 1
    val totalPages: Int get() = _totalPages

    abstract fun setPresentation (page: Int = _actualPage)

    protected fun validatePage(page: Int) =  when {
        (page < 1) -> 1
        (page > _totalPages) -> totalPages
        else -> page
    }

}