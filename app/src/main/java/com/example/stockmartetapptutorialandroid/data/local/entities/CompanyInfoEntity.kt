package com.example.stockmartetapptutorialandroid.data.local.entities

import androidx.room.Entity
import com.squareup.moshi.Json

@Entity
data class CompanyInfoEntity(
    val symbol : String,
    val name : String,
    val description : String,
    val industry : String,
    val country : String,
)