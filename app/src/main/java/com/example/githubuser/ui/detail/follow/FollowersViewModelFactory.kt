package com.example.githubuser.ui.detail.follow

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.data.repository.FollowersRepository
import com.example.githubuser.di.Injection

class FollowersViewModelFactory private constructor(
    private val followersRepository: FollowersRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FollowersViewModel::class.java)) {
            return FollowersViewModel(followersRepository) as T
        }
        throw IllegalArgumentException("Viewmodel class tidak diketahui")
    }

    companion object {
        @Volatile
        private var instance: FollowersViewModelFactory? = null
        fun getInstance(context: Context): FollowersViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: FollowersViewModelFactory(
                    Injection.followersRepository(context)
                )
            }.also { instance = it }
    }

}