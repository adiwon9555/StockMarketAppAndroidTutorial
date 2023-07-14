package com.example.stockmartetapptutorialandroid.data.mapper

import com.example.stockmartetapptutorialandroid.data.remote.dto.IntradayInfoDto
import com.example.stockmartetapptutorialandroid.domain.model.IntradayInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun IntradayInfoDto.toIntradayInfo() : IntradayInfo {
    val pattern = "yyyy-MM-dd HH:mm:ss"
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    val localDateTime = LocalDateTime.parse(timestamp, formatter)
    return IntradayInfo(
        timestamp = localDateTime,
        close = close
    )
}