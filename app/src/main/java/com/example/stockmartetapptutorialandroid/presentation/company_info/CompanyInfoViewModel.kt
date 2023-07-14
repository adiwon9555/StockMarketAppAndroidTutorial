package com.example.stockmartetapptutorialandroid.presentation.company_info

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockmartetapptutorialandroid.domain.repository.StockRepository
import com.example.stockmartetapptutorialandroid.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.system.measureTimeMillis

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    private val stockRepositoryImpl: StockRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(CompanyInfoState())
    val state = _state.asStateFlow()


    init {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }
            val symbol = savedStateHandle.get<String>("symbol") ?: return@launch

            Log.d("@aditya", "1st")
            async {
                Log.d("@aditya", "before getCompanyInfo")
                getCompanyInfo(symbol)
                Log.d("@aditya", "after getCompanyInfo")
            }
            Log.d("@aditya", "2nd")
            async {
                Log.d("@aditya", "before getIntradayInfo")
                getIntradayInfo(symbol)
                Log.d("@aditya", "after getIntradayInfo")
            }
            Log.d("@aditya", "3rd")

//            Log.d("@aditya","1st")
//            getCompanyInfo(symbol)
//            Log.d("@aditya","2nd")
//            getIntradayInfo(symbol)
//            Log.d("@aditya","3rd")

        }

    }

    suspend fun getCompanyInfo(symbol: String) {
        stockRepositoryImpl.getCompanyInfo(symbol)
            .collect { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                companyInfoError = resource.message,
                                //For generic handling
                                error = resource.message,
                                isLoading = false,
                                companyInfo = null
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isCompanyInfoLoading = resource.isLoading,
                                companyInfoError = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        Log.d("@aditya_resource", "getCompany success" + resource.data.toString())
                        _state.update {
                            it.copy(
                                companyInfo = resource.data,
                                //For generic handling
                                isLoading = false,
                                error = null,

                                )
                        }
                    }
                }
            }
    }

    suspend fun getIntradayInfo(symbol: String) {
        stockRepositoryImpl.getIntradayInfo(symbol)
            .collect { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                intradayError = resource.message,
                                //For generic handling
                                error = resource.message,
                                isLoading = false,
                                intradayInfo = emptyList()
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isIntradayLoading = resource.isLoading,
                                intradayError = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        resource.data?.let {
                            Log.d("@aditya_resource", "getIntradayInfo success" + it.toString())
                            _state.update {
                                it.copy(
                                    intradayInfo = resource.data,
                                    //For generic handling
                                    isLoading = false,
                                    error = null,
                                )
                            }
                        }
                    }
                }
            }
    }

}