package com.example.dummyapp

import com.example.libdummyapi.ApiClient
import com.example.libdummyapi.models.Data
import com.example.libdummyapi.models.UserDetailsResponse

class HomeRepository {

    private var currentPageNumber : Int = 0

    suspend fun fetchUserList() : List<Data>? {
        val response = ApiClient.api.getUserList(10,currentPageNumber)
        return response.body()?.data
    }

    suspend fun fetchUserDetails(userId : String) : UserDetailsResponse? {
        return ApiClient.api.getUserDetailsById(userId).body()
    }
}