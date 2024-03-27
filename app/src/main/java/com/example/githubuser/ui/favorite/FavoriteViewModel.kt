package com.example.githubuser.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.local.UserFavorite
import com.example.githubuser.data.repository.UserFavoriteRepository

class FavoriteViewModel(application: Application) : ViewModel() {
    private val mUserFavoriteRepository: UserFavoriteRepository =
        UserFavoriteRepository(application)

    fun getAllUsersFavorite(): LiveData<List<UserFavorite>> =
        mUserFavoriteRepository.getAllUsersFavorite()
}