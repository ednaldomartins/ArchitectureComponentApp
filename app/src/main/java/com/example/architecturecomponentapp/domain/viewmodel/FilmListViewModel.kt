package com.example.architecturecomponentapp.domain.viewmodel

import android.app.Application
import android.os.Parcelable
import androidx.lifecycle.AndroidViewModel

abstract class FilmListViewModel(app: Application): AndroidViewModel(app) {

    //  pagina atual
    protected var _actualPage: Int = 1
    val actualPage: Int get() = _actualPage
    //  total de paginas
    protected var _totalPages: Int = 1
    val totalPages: Int get() = _totalPages
    //  RecyclerViewState
    private var _recyclerViewState: Parcelable? = null
    val recyclerViewState: Parcelable? get() = _recyclerViewState
    fun setRecyclerViewState(recyclerViewState: Parcelable?) {
        this._recyclerViewState = recyclerViewState
    }

    abstract fun setPresentation (page: Int = _actualPage)

    protected fun validatePage(page: Int) =  when {
        (page < 1) -> 1
        (page > _totalPages) -> totalPages
        else -> page
    }

}