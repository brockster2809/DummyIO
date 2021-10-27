package com.example.dummyapp

import com.example.dummyapp.db.User
import com.example.dummyapp.db.UserDao
import com.example.libdummyapi.ApiClient
import com.example.libdummyapi.models.UserDetailsResponse

class HomeRepository(private val userDao : UserDao) {

    private var currentPageNumber : Int = 0
    private var isNetworkCallOngoing = false

    fun getUserList() = userDao.getUsers()

    suspend fun fetchUserList() {
        if (isNetworkCallOngoing) return
        isNetworkCallOngoing = true
        val response = ApiClient.api.getUserList(DEFAULT_PAGE_SIZE,currentPageNumber)
        response.body()?.let { userListResponse ->
            currentPageNumber++
            userDao.insertAll(userListResponse.data.map { User(it.id,it.firstName, it.lastName, it.picture, it.title) })
        }
        isNetworkCallOngoing = false
    }

    suspend fun fetchUserDetails(userId : String) : UserDetailsResponse? {
        return ApiClient.api.getUserDetailsById(userId).body()
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 10
    }
}