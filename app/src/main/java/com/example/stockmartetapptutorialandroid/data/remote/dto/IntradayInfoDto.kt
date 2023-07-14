package com.example.stockmartetapptutorialandroid.data.remote.dto

import java.time.LocalDateTime

data class IntradayInfoDto(
    val timestamp : String,
    val close : Double,
)