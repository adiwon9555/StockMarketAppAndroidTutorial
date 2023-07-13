package com.example.stockmartetapptutorialandroid.data.remote

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApi {

    @GET("query?function=LISTING_STATUS")
    suspend fun getAllCompaniesList(
        @Query("apikey") apikey : String = API_KEY
    ) : ResponseBody

    companion object{
        const val API_KEY = "8QI7Z8PZV3W08K2Q"
        const val BASE_URL = "https://alphavantage.co"
    }
}