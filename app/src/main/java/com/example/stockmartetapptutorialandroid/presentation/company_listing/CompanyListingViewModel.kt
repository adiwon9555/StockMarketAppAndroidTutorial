package com.example.stockmartetapptutorialandroid.presentation.company_listing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockmartetapptutorialandroid.domain.repository.StockRepository
import com.example.stockmartetapptutorialandroid.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CompanyListingViewModel @Inject constructor(
    private val stockRepositoryImpl: StockRepository
) : ViewModel() {
    private val _state = MutableStateFlow(CompanyListingState())
    val state = _state.asStateFlow()

    private var searchJob : Job? = null

    init {
        getCompanyListing()
    }

    fun onEvent(events: CompanyListingEvents) {
        when (events) {
            CompanyListingEvents.OnRefresh -> {
                getCompanyListing(fetchFromRemote = true)
            }
            is CompanyListingEvents.OnSearchInput -> {
                _state.update {
                    it.copy(
                        searchQuery = events.query
                    )
                }
                searchJob?.cancel()
                searchJob = viewModelScope.launch{
                    delay(500L)
                    getCompanyListing()
                }
            }
        }
    }

    private fun getCompanyListing(fetchFromRemote : Boolean = false, query: String = state.value.searchQuery.lowercase()){
        viewModelScope.launch{
            stockRepositoryImpl.getCompanyListing(fetchFromRemote,query)
                .collect{ resource ->
                    when(resource){
                        is Resource.Error -> {
                            _state.update {
                                it.copy(
                                    error = resource.message,
                                    isLoading = false
                                )
                            }
                        }
                        is Resource.Loading -> {
                            _state.update {
                                it.copy(
                                    isLoading = resource.isLoading
                                )
                            }
                        }
                        is Resource.Success -> {
                            resource.data?.let{ data ->
                                _state.update {
                                    it.copy(
                                        error = null,
                                        isLoading = false,
                                        companyListing = data
                                    )
                                }
                            }

                        }
                    }
                }
        }

    }
}