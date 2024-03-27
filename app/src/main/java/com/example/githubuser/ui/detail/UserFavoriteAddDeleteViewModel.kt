package com.example.githubuser.ui.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.local.UserFavorite
import com.example.githubuser.data.repository.UserFavoriteRepository

class UserFavoriteAddDeleteViewModel(application: Application) : ViewModel() {
    private val mUserFavoriteRepository: UserFavoriteRepository =
        UserFavoriteRepository(application)

    fun insert(userFavorite: UserFavorite) {
        mUserFavoriteRepository.insert(userFavorite)
    }

    fun delete(userFavorite: UserFavorite) {
        mUserFavoriteRepository.delete(userFavorite)
    }

}