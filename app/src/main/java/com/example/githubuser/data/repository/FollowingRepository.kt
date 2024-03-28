package com.example.githubuser.data.repository

import android.util.Log
import com.example.githubuser.data.Result
import com.example.githubuser.data.remote.response.FollowUserResponseItem
import com.example.githubuser.data.remote.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingRepository private constructor(
    private val apiService: ApiService
) {
    fun findFollowing(username: String, callback: (Result<List<FollowUserResponseItem>>) -> Unit) {
        val client = apiService.getFollowing(username)
        client.enqueue(object : Callback<List<FollowUserResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowUserResponseItem>>,
                response: Response<List<FollowUserResponseItem>>
            ) {
                if (response.isSuccessful) {
                    val following = response.body()
                    if (following.isNullOrEmpty()) {
                        callback(Result.Error("Tidak ada following"))
                    } else {
                        callback(Result.Success(following))
                    }
                } else {
                    callback(Result.Error("Gagal memuat following | ${response.message()}"))
                    Log.e(TAG, "onfailure: ${response.message()}")
                }
            }


            override fun onFailure(call: Call<List<FollowUserResponseItem>>, t: Throwable) {
                callback(Result.Error("Gagal memuat following | ${t.message}"))
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    companion object {
        private const val TAG = "FollowingRepository"

        @Volatile
        private var instance: FollowingRepository? = null
        fun getInstance(apiService: ApiService): FollowingRepository =
            instance ?: synchronized(this) {
                instance ?: FollowingRepository(apiService)
            }.also { instance = it }
    }
}