package com.example.githubuser.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.data.repository.HomeRepository
import com.example.githubuser.di.Injection

class HomeViewModelFactory private constructor(
    private val homeRepository: HomeRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(homeRepository) as T
        }
        throw IllegalArgumentException("Viewmodel class tidak diketahui: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: HomeViewModelFactory? = null
        fun getInstance(): HomeViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: HomeViewModelFactory(
                    Injection.homeRepository()
                )
            }.also { instance = it }
    }

}