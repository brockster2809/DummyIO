package com.example.libdummyapi.models

data class UserListResponse(
    val `data`: List<Data>,
    val limit: Int,
    val page: Int,
    val total: Int
)