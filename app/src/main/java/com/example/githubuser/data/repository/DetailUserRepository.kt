package com.example.githubuser.data.repository

import android.util.Log
import com.example.githubuser.data.Result
import com.example.githubuser.data.remote.response.UserDetailResponse
import com.example.githubuser.data.remote.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserRepository(
    private val apiService: ApiService
) {

    fun findUserDetail(username: String, callback: (Result<UserDetailResponse>) -> Unit) {
        val client = apiService.getDetailUser(username)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                if (response.isSuccessful) {
                    val userDetail = response.body()
                    if (userDetail == null) {
                        callback(Result.Error("Tidak dapat menemukan User"))
                    } else {
                        callback(Result.Success(userDetail))
                    }
                } else {
                    callback(Result.Error("Gagal mendapatkan detail user : ${response.message()}"))
                    Log.e(TAG, "onfailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                callback(Result.Error("Detail user tidak ditemukan: ${t.message}"))
                Log.e(TAG, "onfailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "DeailUserRepository"

        @Volatile
        private var instance: DetailUserRepository? = null
        fun getInstance(apiService: ApiService): DetailUserRepository =
            instance ?: synchronized(this) {
                instance ?: DetailUserRepository(apiService)
            }.also { instance = it }
    }

}