package com.example.githubuser.ui.favorite

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.data.repository.FavoriteRepository
import com.example.githubuser.di.Injection

class FavoriteViewModelFactory private constructor(private val favoriteRepository: FavoriteRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CASR")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(favoriteRepository) as T
        }
        throw IllegalArgumentException("Viewmodel class tidak dikethaui: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: FavoriteViewModelFactory? = null

        fun getInstance(context: Context): FavoriteViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: FavoriteViewModelFactory(Injection.favoriteRepository(context))
            }.also { instance = it }
    }
}