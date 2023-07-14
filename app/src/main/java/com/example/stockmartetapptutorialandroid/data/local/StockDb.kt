package com.example.stockmartetapptutorialandroid.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.stockmartetapptutorialandroid.data.local.entities.CompanyListingEntity

@Database(
    entities = [CompanyListingEntity::class],
    version = 1
)
abstract class StockDb : RoomDatabase() {

    abstract val dao : StockDao
}