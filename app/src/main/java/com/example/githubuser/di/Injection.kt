package com.example.githubuser.di

import android.content.Context
import com.example.githubuser.data.remote.retrofit.ApiConfig
import com.example.githubuser.data.repository.HomeRepository

object Injection {
    fun provideRepository(context: Context): HomeRepository {
        val apiService = ApiConfig.getApiService()
        return HomeRepository.getInstance(apiService)
    }
}