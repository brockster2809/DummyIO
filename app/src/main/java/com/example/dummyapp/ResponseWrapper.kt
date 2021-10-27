package com.example.dummyapp

sealed class ResponseWrapper<T> {
    class Loading<T> : ResponseWrapper<T>()
    data class Success<T>(val data : T) : ResponseWrapper<T>()
    data class Error<T>(val stringId : Int) : ResponseWrapper<T>()
}
