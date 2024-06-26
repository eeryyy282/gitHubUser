package com.example.githubuser.ui.detail.follow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.data.repository.FollowingRepository
import com.example.githubuser.di.Injection

class FollowingViewModelFactory private constructor(
    private val followingRepository: FollowingRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FollowingViewModel::class.java)) {
            return FollowingViewModel(followingRepository) as T
        }
        throw IllegalArgumentException("Viewmodel class tidak ditemukan")
    }

    companion object {
        @Volatile
        private var instance: FollowingViewModelFactory? = null
        fun getInstance(): FollowingViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: FollowingViewModelFactory(
                    Injection.followingRepository()
                )
            }.also { instance = it }
    }
}