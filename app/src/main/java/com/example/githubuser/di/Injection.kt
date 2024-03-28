package com.example.githubuser.di

import android.content.Context
import com.example.githubuser.data.remote.retrofit.ApiConfig
import com.example.githubuser.data.repository.FollowersRepository
import com.example.githubuser.data.repository.HomeRepository

object Injection {
    fun homeRepository(context: Context): HomeRepository {
        val apiService = ApiConfig.getApiService()
        return HomeRepository.getInstance(apiService)
    }

    fun followersRepository(context: Context): FollowersRepository {
        val apiService = ApiConfig.getApiService()
        return FollowersRepository.getInstance(apiService)
    }
}