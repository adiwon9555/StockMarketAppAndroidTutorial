package com.example.stockmartetapptutorialandroid.domain.repository

import com.example.stockmartetapptutorialandroid.domain.model.CompanyInfo
import com.example.stockmartetapptutorialandroid.domain.model.CompanyListing
import com.example.stockmartetapptutorialandroid.domain.model.IntradayInfo
import com.example.stockmartetapptutorialandroid.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getCompanyListing(
        fetchFromRemote: Boolean,
        query: String
    ) : Flow<Resource<List<CompanyListing>>>

    suspend fun getCompanyInfo(
        symbol: String
    ) : Flow<Resource<CompanyInfo>>

    suspend fun getIntradayInfo(
        symbol: String
    ) : Flow<Resource<List<IntradayInfo>>>
}