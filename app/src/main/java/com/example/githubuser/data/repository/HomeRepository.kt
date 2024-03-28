package com.example.githubuser.data.repository

import android.util.Log
import com.example.githubuser.data.Result
import com.example.githubuser.data.remote.response.UserResponse
import com.example.githubuser.data.remote.response.Users
import com.example.githubuser.data.remote.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeRepository private constructor(
    private val apiService: ApiService
) {
    fun findUsers(username: String, callback: (Result<List<Users>>) -> Unit) {
        val client = apiService.getUsers(username)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val users = response.body()?.items
                    if (users.isNullOrEmpty()) {
                        callback(Result.Error("Maaf username tidak ditemukan :("))
                    } else {
                        callback(Result.Success(users))
                    }
                } else {
                    callback(Result.Error("Gagal memuat user | ${response.message()}"))
                    Log.e(TAG, "onfailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                callback(Result.Error("Gagal memuat user | ${t.message}"))
                Log.e(TAG, "onfailure: ${t.message}")
            }
        })
    }


    companion object {
        private const val TAG = "HomeRepository"

        @Volatile
        private var instance: HomeRepository? = null
        fun getInstance(apiService: ApiService): HomeRepository =
            instance ?: synchronized(this) {
                instance ?: HomeRepository(apiService)
            }.also { instance = it }
    }
}