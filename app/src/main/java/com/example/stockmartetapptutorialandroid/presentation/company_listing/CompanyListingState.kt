package com.example.stockmartetapptutorialandroid.presentation.company_listing

import com.example.stockmartetapptutorialandroid.domain.model.CompanyListing

data class CompanyListingState(
    val companyListing: List<CompanyListing> = emptyList(),
    val isRefreshing : Boolean = false,
    val isLoading : Boolean = false,
    val searchQuery : String = "",
    val error : String? = null
)
