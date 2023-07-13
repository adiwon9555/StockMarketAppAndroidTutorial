package com.example.stockmartetapptutorialandroid.di

import com.example.stockmartetapptutorialandroid.data.csv.CSVParser
import com.example.stockmartetapptutorialandroid.data.csv.CompanyListingCsvParser
import com.example.stockmartetapptutorialandroid.data.repository.StockRepositoryImpl
import com.example.stockmartetapptutorialandroid.domain.model.CompanyListing
import com.example.stockmartetapptutorialandroid.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCompanyCsvParser(
        companyListingCsvParser: CompanyListingCsvParser
    ) : CSVParser<CompanyListing>

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepositoryImpl: StockRepositoryImpl
    ) : StockRepository
}