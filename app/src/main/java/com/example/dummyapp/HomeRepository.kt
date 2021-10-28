package com.example.dummyapp

import com.example.dummyapp.db.User
import com.example.dummyapp.db.UserDao
import com.example.libdummyapi.ApiClient
import com.example.libdummyapi.models.UserDetailsResponse

class HomeRepository(private val userDao : UserDao) {

    private var lastPageFetched : Int = -1
    private var isNetworkCallOngoing = false

    fun getUserList() = userDao.getUsers()

    suspend fun fetchUserList(totalItemCount: Int?) {
        var userItemCount : Int? = totalItemCount
        if (userItemCount == null) {
            userItemCount = userDao.getTotalUsersCountInDB()
        }
        if (isNetworkCallOngoing) return

        val pageNumberToBeFetched = userItemCount / DEFAULT_PAGE_SIZE
        if (pageNumberToBeFetched == lastPageFetched) return
        lastPageFetched = pageNumberToBeFetched
        isNetworkCallOngoing = true

        val response = ApiClient.api.getUserList(DEFAULT_PAGE_SIZE,lastPageFetched)
        response.body()?.let { userListResponse ->
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