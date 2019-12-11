package com.ednaldomartins.architecturecomponentapp.data.entity

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Filme(
    @NonNull @PrimaryKey(autoGenerate = true) var id: Long,
    var nome: String = "",
    var ano: Int = 0
) : Serializable