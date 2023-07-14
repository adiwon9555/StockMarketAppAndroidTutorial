package com.example.stockmartetapptutorialandroid.presentation.company_info

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stockmartetapptutorialandroid.domain.model.CompanyInfo
import com.example.stockmartetapptutorialandroid.ui.theme.StockMartetAppTutorialAndroidTheme
import com.ramcosta.composedestinations.annotation.Destination
import java.text.DateFormatSymbols

@Composable
@Destination()
fun CompanyInfoScreen(
    symbol: String,
    viewModel: CompanyInfoViewModel = hiltViewModel()
){
    val state = viewModel.state.collectAsState().value
    val comapanyInfo = state.companyInfo
    val intradayInfo = state.intradayInfo
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        comapanyInfo?.let {
            Text(
                text = comapanyInfo.name,
                modifier = Modifier
                    .fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = comapanyInfo.symbol,
                modifier = Modifier
                    .fillMaxWidth(),
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic
            )
            Spacer(modifier = Modifier.height(8.dp))
            Divider(modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Industry: ${comapanyInfo.industry}",
                modifier = Modifier
                    .fillMaxWidth(),
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Country: ${comapanyInfo.country}",
                modifier = Modifier
                    .fillMaxWidth(),
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp,
            )
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = comapanyInfo.description,
                modifier = Modifier
                    .fillMaxWidth(),
                overflow = TextOverflow.Ellipsis,
                fontSize = 12.sp,
            )
            if(intradayInfo.isNotEmpty()){
                Spacer(modifier = Modifier.height(8.dp))
                Text(text ="Market Summary")
                Spacer(modifier = Modifier.height(32.dp))
                StockChart(
                    infos = intradayInfo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        if(state.isLoading){
            CircularProgressIndicator()
        }else if(state.error != null){
            Text(text = state.error, color = MaterialTheme.colors.error)
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun previewCompanyInfoScreen(){
    StockMartetAppTutorialAndroidTheme(darkTheme = true) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
//            CompanyInfoScreen(comapanyInfo = CompanyInfo(
//                symbol = "HDFC",
//                name = "HDFC BANK",
//                description = "ip: the intraday data (including 20+ years of historical data) is updated at the end of each trading day for all users by default. If you would like to access realtime or 15-minute delayed intraday data, please subscribe to a premium membership plan for your personal use. For commercial use, please contact sales.",
//                industry = "Banking and Finance",
//                country = "India",
//            ))
            CompanyInfoScreen("GOOG")
        }

    }
}