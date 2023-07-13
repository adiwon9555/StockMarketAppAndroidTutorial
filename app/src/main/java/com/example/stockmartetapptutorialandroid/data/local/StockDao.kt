package com.example.stockmartetapptutorialandroid.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface StockDao {

    @Upsert
    suspend fun upsertCompanyListing(companyListingEntities : List<CompanyListingEntity>)

    @Query("""
        SELECT *
        FROM companylistingentity
        WHERE LOWER(name) like '%' || LOWER(:query) || '%'
        OR symbol = UPPER(:query)
    """)
    suspend fun searchCompanyListing(query: String) : List<CompanyListingEntity>

    @Query("DELETE FROM companylistingentity")
    suspend fun clearAllCompanyListing()

}
