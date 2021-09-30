package com.example.libdummyapi.api

import com.example.libdummyapi.models.UserDetailsResponse
import com.example.libdummyapi.models.UserListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DummyService {

    @GET("/data/v1/user")
    suspend fun getUserList(@Query("limit") limit : Int, @Query("page") page: Int) : Response<UserListResponse>

    @GET("/data/v1/user/{id}")
    suspend fun getUserDetailsById(@Path("id") id : String) : Response<UserDetailsResponse>
}