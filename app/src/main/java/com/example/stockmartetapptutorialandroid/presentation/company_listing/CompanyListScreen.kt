package com.example.stockmartetapptutorialandroid.presentation.company_listing

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.ActivityNavigator
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@RootNavGraph(start = true)
@Destination
fun CompanyListScreen(
    navigator: DestinationsNavigator
){
    val viewModel = hiltViewModel<CompanyListingViewModel>()

    val state = viewModel.state.collectAsState().value

    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = state.isRefreshing
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = {
                viewModel.onEvent(
                    CompanyListingEvents.OnSearchInput(it)
                )
            },
            maxLines = 1,
            placeholder = {
                Text(text ="Search..")
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            singleLine = true
        )
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                viewModel.onEvent(CompanyListingEvents.OnRefresh)
            }
        ){
            LazyColumn (
                modifier = Modifier.fillMaxSize()
            ){
                items(state.companyListing.size) { i ->
                    val company = state.companyListing[i]
                    CompanyItem(
                        company = company,
                        modifier = Modifier
                            .clickable {
                            }
                            .fillMaxWidth()
                            .padding(16.dp)

                    )
                    if(i < state.companyListing.size){
                        Divider(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}