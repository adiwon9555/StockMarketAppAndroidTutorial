package com.example.stockmartetapptutorialandroid.domain.model

import com.squareup.moshi.Json

data class CompanyInfo(
    val symbol : String,
    val name : String,
    val description : String,
    val industry : String,
    val country : String,
)