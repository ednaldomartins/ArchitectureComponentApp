package com.example.architecturecomponentapp.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ednaldomartins.architecturecomponentapp.data.dao.FilmDao

class FilmViewModel (
    val databaseDao: FilmDao,
    application: Application
): AndroidViewModel (application) {

}