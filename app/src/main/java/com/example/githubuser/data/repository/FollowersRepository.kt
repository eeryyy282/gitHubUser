package com.example.githubuser.data.repository

import android.util.Log
import com.example.githubuser.data.Result
import com.example.githubuser.data.remote.response.FollowUserResponseItem
import com.example.githubuser.data.remote.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersRepository private constructor(
    private val apiService: ApiService
) {
    fun findFollowers(username: String, callback: (Result<List<FollowUserResponseItem>>) -> Unit) {
        val client = apiService.getFollowers(username)
        client.enqueue(object : Callback<List<FollowUserResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowUserResponseItem>>,
                response: Response<List<FollowUserResponseItem>>
            ) {
                if (response.isSuccessful) {
                    val followers = response.body()
                    if (followers.isNullOrEmpty()) {
                        callback(Result.Error("Tidak ada followers"))
                    } else {
                        callback(Result.Success(followers))
                    }
                } else {
                    callback(Result.Error("Gagal memuat followers | ${response.message()}"))
                    Log.e(TAG, "onfailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<FollowUserResponseItem>>, t: Throwable) {
                callback(Result.Error("Gagal memuat followers | ${t.message}"))
                Log.e(TAG, "onfailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "FollowersRepository"

        @Volatile
        private var instance: FollowersRepository? = null
        fun getInstance(apiService: ApiService): FollowersRepository =
            instance ?: synchronized(this) {
                instance ?: FollowersRepository(apiService)
            }.also { instance = it }
    }
}