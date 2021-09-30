package com.example.libdummyapi.api

import com.example.libdummyapi.ApiClient
import kotlinx.coroutines.runBlocking
import org.junit.Test

class DummyServiceTests {
    val api = ApiClient.api

    @Test
    fun `get user list working`()  = runBlocking {
        val response = api.getUserList(10,1)
        assert(response.body() != null)
    }

    @Test
    fun `get user details working`()  = runBlocking {
        val id = "60d0fe4f5311236168a109ca"
        val response = api.getUserDetailsById(id)
        assert(response.body() != null)
    }
}