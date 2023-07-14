package com.example.stockmartetapptutorialandroid.data.csv

import com.example.stockmartetapptutorialandroid.data.mapper.toIntradayInfo
import com.example.stockmartetapptutorialandroid.data.remote.dto.IntradayInfoDto
import com.example.stockmartetapptutorialandroid.domain.model.CompanyListing
import com.example.stockmartetapptutorialandroid.domain.model.IntradayInfo
import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDateTime
import javax.inject.Inject

class IntradayInfoCsvParser @Inject constructor() : CSVParser<IntradayInfo> {

    override suspend fun parse(stream: InputStream): List<IntradayInfo> {
        val csvReader = CSVReader(InputStreamReader(stream))
        return withContext(Dispatchers.IO) {
            csvReader
                .readAll()
                .drop(1)
                .mapNotNull {
                    val close = it.getOrNull(4)
                    val timestamp = it.getOrNull(0)
                    IntradayInfoDto(
                        close = close?.toDouble() ?: return@mapNotNull null,
                        timestamp = timestamp ?: return@mapNotNull null,
                    ).toIntradayInfo()
                }
                .filter {
                    it.timestamp.dayOfMonth == LocalDateTime.now().minusDays(4).dayOfMonth
                }
                .sortedBy {
                    it.timestamp.hour
                }
                .also {
                    csvReader.close()
                }
        }
    }
}