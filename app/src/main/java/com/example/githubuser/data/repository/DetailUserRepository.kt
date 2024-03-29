package com.example.githubuser.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.githubuser.data.Result
import com.example.githubuser.data.local.UserFavoriteDao
import com.example.githubuser.data.local.entity.UserFavoriteEntity
import com.example.githubuser.data.remote.response.UserDetailResponse
import com.example.githubuser.data.remote.retrofit.ApiService
import com.example.githubuser.utils.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserRepository(
    private val apiService: ApiService,
    private val userFavoriteDao: UserFavoriteDao,
    private val appExecutors: AppExecutors
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

    fun setFavoriteUser(userFavoriteEntity: UserFavoriteEntity) {
        appExecutors.diskIO.execute {
            userFavoriteDao.addFavoriteUser(userFavoriteEntity)
        }
    }

    fun removeFavoriteUser(userFavoriteEntity: UserFavoriteEntity) {
        appExecutors.diskIO.execute {
            userFavoriteDao.removeFavoriteUser(userFavoriteEntity)
        }
    }


    fun getFavoriteUserByUsername(username: String): LiveData<UserFavoriteEntity> {
        return userFavoriteDao.getUsersFavoriteByUsername(username)
    }


    companion object {
        private const val TAG = "DetailUserRepository"

        @Volatile
        private var instance: DetailUserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userFavoriteDao: UserFavoriteDao,
            appExecutors: AppExecutors
        ): DetailUserRepository =
            instance ?: synchronized(this) {
                instance ?: DetailUserRepository(apiService, userFavoriteDao, appExecutors)
            }.also { instance = it }
    }

}