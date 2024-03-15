package com.example.githubuser.data.retrofit

import com.example.githubuser.data.response.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getUsers(
        @Query("q") q: String
    ): Call<UserResponse>

    @GET("user/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<UserResponse>
}