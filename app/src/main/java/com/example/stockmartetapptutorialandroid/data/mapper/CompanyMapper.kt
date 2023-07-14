package com.example.stockmartetapptutorialandroid.data.mapper

import com.example.stockmartetapptutorialandroid.data.local.entities.CompanyListingEntity
import com.example.stockmartetapptutorialandroid.data.remote.dto.CompanyInfoDto
import com.example.stockmartetapptutorialandroid.domain.model.CompanyInfo
import com.example.stockmartetapptutorialandroid.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyListing() : CompanyListing{
    return CompanyListing(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyListing.toCompanyListingEntity() : CompanyListingEntity {
    return CompanyListingEntity(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyInfoDto.toComapnyInfo() : CompanyInfo {
    return CompanyInfo(
        symbol = symbol ?: "",
        description = description ?: "",
        name = name ?: "",
        country = country ?: "",
        industry = industry ?: ""
    )
}