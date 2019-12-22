package com.example.architecturecomponentapp.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import com.example.architecturecomponentapp.data.dao.FilmDao
import com.example.architecturecomponentapp.data.database.remote.FilmsApi
import com.example.architecturecomponentapp.data.entity.Film

class FilmViewModel (val databaseDao: FilmDao, app: Application) : AndroidViewModel (app) {

    private var viewModelJob = Job()
    private val uiCoroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var response = MutableLiveData<Film>()
    val resp: LiveData<Film> get() = response

    fun requestFilmApiService () {
        FilmsApi.retrofitService.callFilmsApi().enqueue( object : Callback<Film> {
            override fun onFailure(call: Call<Film>, t: Throwable) {
                response.value = Film(-1,"sem retorno", "-1")
            }

            override fun onResponse(call: Call<Film>, r: Response<Film>) {
                response.value = r.body()
            }
        })
    }

    // inserir filme chamando funcao de suspensao
    fun insertFilm(name: String, date: String) {
        uiCoroutineScope.launch { insert( Film(title = name, releaseData = date) ) }
    }

    //funcao de suspensao para inserir film no BD
    private suspend fun insert (film: Film) {
        withContext(Dispatchers.IO) { databaseDao.insertFilm(film) }
    }

    override fun onCleared() {
        super.onCleared()
        // cancelar raiz das coroutines
        viewModelJob.cancel()
    }
}