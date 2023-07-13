package com.example.stockmartetapptutorialandroid.data.repository

import com.example.stockmartetapptutorialandroid.data.csv.CSVParser
import com.example.stockmartetapptutorialandroid.data.csv.CompanyListingCsvParser
import com.example.stockmartetapptutorialandroid.data.local.StockDb
import com.example.stockmartetapptutorialandroid.data.mapper.toCompanyListing
import com.example.stockmartetapptutorialandroid.data.mapper.toCompanyListingEntity
import com.example.stockmartetapptutorialandroid.data.remote.StockApi
import com.example.stockmartetapptutorialandroid.domain.model.CompanyListing
import com.example.stockmartetapptutorialandroid.domain.repository.StockRepository
import com.example.stockmartetapptutorialandroid.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class StockRepositoryImpl @Inject constructor(
    private val stockDb: StockDb,
    private val stockApi: StockApi,
    private val companyListingCsvParser: CSVParser<CompanyListing>
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
}