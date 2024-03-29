package com.example.githubuser.ui.favorite

import androidx.lifecycle.ViewModel
import com.example.githubuser.data.repository.FavoriteRepository

class FavoriteViewModel(
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    fun getFavoriteUser() = favoriteRepository.getFavoriteUser()

}