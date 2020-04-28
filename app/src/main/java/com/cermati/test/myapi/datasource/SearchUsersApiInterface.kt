package com.cermati.test.myapi.datasource

import com.cermati.test.myapi.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchUsersApiInterface{
    @GET("/search/users")
    fun users(
        @Query("q") keyword: String?,
        @Query("page") page: Int?
    ): Call<UserResponse>
}