package com.example.githubuser.data.repository

import androidx.lifecycle.LiveData
import com.example.githubuser.data.local.UserFavoriteDao
import com.example.githubuser.data.local.entity.UserFavoriteEntity

class FavoriteRepository(
    private val userFavoriteDao: UserFavoriteDao
) {
    fun getFavoriteUser(): LiveData<List<UserFavoriteEntity>> {
        return userFavoriteDao.getAllFavoriteUsers()
    }

    companion object {

        @Volatile
        private var instance: FavoriteRepository? = null

        fun getInstance(
            userFavoriteDao: UserFavoriteDao
        ): FavoriteRepository =
            instance ?: synchronized(this) {
                instance ?: FavoriteRepository(userFavoriteDao)
            }.also { instance = it }
    }
}