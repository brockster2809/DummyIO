package com.example.dummyapp.ui.list

import com.example.libdummyapi.ApiClient
import com.example.libdummyapi.models.Data

class UserListRepository {

    private var currentPageNumber : Int = 0

    suspend fun fetchUserList() : List<Data>? {
        val response = ApiClient.api.getUserList(10,currentPageNumber)
        return response.body()?.data
    }
}