package com.example.stockmartetapptutorialandroid.data.repository

import com.example.stockmartetapptutorialandroid.data.csv.CSVParser
import com.example.stockmartetapptutorialandroid.data.csv.CompanyListingCsvParser
import com.example.stockmartetapptutorialandroid.data.csv.IntradayInfoCsvParser
import com.example.stockmartetapptutorialandroid.data.local.StockDb
import com.example.stockmartetapptutorialandroid.data.mapper.toComapnyInfo
import com.example.stockmartetapptutorialandroid.data.mapper.toCompanyListing
import com.example.stockmartetapptutorialandroid.data.mapper.toCompanyListingEntity
import com.example.stockmartetapptutorialandroid.data.remote.StockApi
import com.example.stockmartetapptutorialandroid.domain.model.CompanyInfo
import com.example.stockmartetapptutorialandroid.domain.model.CompanyListing
import com.example.stockmartetapptutorialandroid.domain.model.IntradayInfo
import com.example.stockmartetapptutorialandroid.domain.repository.StockRepository
import com.example.stockmartetapptutorialandroid.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class StockRepositoryImpl @Inject constructor(
    private val stockDb: StockDb,
    private val stockApi: StockApi,
    private val companyListingCsvParser: CSVParser<CompanyListing>,
    private val intradayInfoCsvParser: CSVParser<IntradayInfo>
    ) : StockRepository {

    private val dao = stockDb.dao


    override suspend fun getCompanyListing(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading(true))
            val localListing = dao.searchCompanyListing(query)
            emit(Resource.Success(localListing.map { it.toCompanyListing() }))
            val isDbEmpty = localListing.isEmpty() && query.isBlank()
            val shouldLoadFromCache = !isDbEmpty && !fetchFromRemote
            if(shouldLoadFromCache){
                emit(Resource.Loading(false))
                return@flow
            }
            val remoteListing = try {
                val response = stockApi.getAllCompaniesList()
                companyListingCsvParser.parse(response.byteStream())
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(e.message.toString()))
                emit(Resource.Loading(false))
                null
            } catch (e: HttpException){
                e.printStackTrace()
                emit(Resource.Error(e.message.toString()))
                emit(Resource.Loading(false))
                null
            }
            remoteListing?.apply {
                dao.clearAllCompanyListing()
                stockDb.dao.upsertCompanyListing(
                    remoteListing.map { it.toCompanyListingEntity() }
                )
                val localListing = dao.searchCompanyListing(query)
                emit(Resource.Success(localListing.map { it.toCompanyListing() }))
                emit(Resource.Loading(false))
            }

        }
    }

    override suspend fun getCompanyInfo(symbol: String): Flow<Resource<CompanyInfo>> {
        return flow{
            emit(Resource.Loading(true))
                try{
                    val companyInfoResponse = stockApi.getCompanyInfo(symbol = symbol)
                    emit(Resource.Success(companyInfoResponse.toComapnyInfo()))
                    emit(Resource.Loading(false))
                }catch (e: IOException){
                    e.printStackTrace()
                    emit(Resource.Error(message = e.message.toString()))
                    emit(Resource.Loading(false))
                }catch (e: HttpException){
                    e.printStackTrace()
                    emit(Resource.Error(message = e.message.toString()))
                    emit(Resource.Loading(false))
                }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getIntradayInfo(symbol: String): Flow<Resource<List<IntradayInfo>>> {
        return flow {
            emit(Resource.Loading(true))
                try{
                    val response = stockApi.getIntradayInfoForCompany(symbol = symbol)
                    val intradayInfo = intradayInfoCsvParser.parse(response.byteStream())
                    emit(Resource.Success(intradayInfo))
                    emit(Resource.Loading(false))
                }catch (e: IOException){
                    e.printStackTrace()
                    emit(Resource.Error(message = e.message.toString()))
                    emit(Resource.Loading(false))
                }catch (e: HttpException){
                    e.printStackTrace()
                    emit(Resource.Error(message = e.message.toString()))
                    emit(Resource.Loading(false))
                }
        }.flowOn(Dispatchers.IO)
    }
}