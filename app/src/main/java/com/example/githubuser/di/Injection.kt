package com.example.githubuser.di

import android.content.Context
import com.example.githubuser.data.local.UserFavoriteDatabase
import com.example.githubuser.data.remote.retrofit.ApiConfig
import com.example.githubuser.data.repository.DetailUserRepository
import com.example.githubuser.data.repository.FavoriteRepository
import com.example.githubuser.data.repository.FollowersRepository
import com.example.githubuser.data.repository.FollowingRepository
import com.example.githubuser.data.repository.HomeRepository
import com.example.githubuser.utils.AppExecutors

object Injection {
    fun homeRepository(context: Context): HomeRepository {
        val apiService = ApiConfig.getApiService()
        return HomeRepository.getInstance(apiService)
    }

    fun followersRepository(context: Context): FollowersRepository {
        val apiService = ApiConfig.getApiService()
        return FollowersRepository.getInstance(apiService)
    }

    fun followingRepository(context: Context): FollowingRepository {
        val apiService = ApiConfig.getApiService()
        return FollowingRepository.getInstance(apiService)
    }

    fun detailUserRepository(context: Context): DetailUserRepository {
        val apiService = ApiConfig.getApiService()
        val database = UserFavoriteDatabase.getInstance(context)
        val userFavoriteDao = database.userFavoriteDao()
        val appExecutors = AppExecutors()
        return DetailUserRepository.getInstance(apiService, userFavoriteDao, appExecutors)
    }

    fun favoriteRepository(context: Context): FavoriteRepository {
        val database = UserFavoriteDatabase.getInstance(context)
        val userFavoriteDao = database.userFavoriteDao()
        return FavoriteRepository.getInstance(userFavoriteDao)
    }
}