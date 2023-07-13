package com.example.stockmartetapptutorialandroid.presentation.company_listing


sealed interface CompanyListingEvents{
    data class OnSearchInput(val query: String) : CompanyListingEvents
    object OnRefresh : CompanyListingEvents
}
