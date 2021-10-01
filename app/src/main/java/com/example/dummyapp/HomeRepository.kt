package com.example.dummyapp

import android.util.Log
import com.example.libdummyapi.ApiClient
import com.example.libdummyapi.models.Data
import com.example.libdummyapi.models.UserDetailsResponse

class HomeRepository {

    private var currentPageNumber : Int = 0
    private val cache : ArrayList<Data> = ArrayList()

    suspend fun fetchUserList() : List<Data>? {
        Log.d("ANSHUL DEBUG", "currentPageNumber ${currentPageNumber}")
        val response = ApiClient.api.getUserList(10,currentPageNumber)
        response.body()?.let {
            Log.d("ANSHUL DEBUG", "currentPageNumber ${currentPageNumber},limit ${it.limit}, page ${it.page}, total ${it.total}")
            currentPageNumber++
            cache.addAll(it.data)
        }
        return cache
    }

    suspend fun fetchUserDetails(userId : String) : UserDetailsResponse? {
        return ApiClient.api.getUserDetailsById(userId).body()
    }
}