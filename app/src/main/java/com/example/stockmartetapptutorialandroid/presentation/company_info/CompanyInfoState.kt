package com.example.stockmartetapptutorialandroid.presentation.company_info

import com.example.stockmartetapptutorialandroid.domain.model.CompanyInfo
import com.example.stockmartetapptutorialandroid.domain.model.IntradayInfo

data class CompanyInfoState(
    val companyInfo: CompanyInfo? = null,
    val intradayInfo: List<IntradayInfo> = emptyList(),
    val isIntradayLoading: Boolean = false, //Individual load and error handling, not doing now
    val isCompanyInfoLoading: Boolean = false, //Individual load and error handling, not doing now
    val intradayError: String? = null, //Individual load and error handling, not doing now
    val companyInfoError: String? = null, //Individual load and error handling, not doing now
    val error : String? = null,
    val isLoading: Boolean = false
)
