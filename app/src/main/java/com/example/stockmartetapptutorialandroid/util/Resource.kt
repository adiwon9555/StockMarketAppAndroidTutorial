package com.example.stockmartetapptutorialandroid.util

sealed class Resource<T>(val data : T?,val message : String? = null){
    class Success<T>(data: T?) : Resource<T>(data, null)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data,message)
    data class Loading<T>(val isLoading: Boolean) : Resource<T>(null)
}
