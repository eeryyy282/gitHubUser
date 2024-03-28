package com.example.githubuser.ui.detail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.data.repository.DetailUserRepository
import com.example.githubuser.di.Injection

class DetailUserViewModelFactory private constructor(
    private val detailUserRepository: DetailUserRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailUserViewModel::class.java)) {
            return DetailUserViewModel(detailUserRepository) as T
        }
        throw IllegalArgumentException("Viewmodel class tidak ditemukan")
    }

    companion object {
        @Volatile
        private var instance: DetailUserViewModelFactory? = null

        fun getInstance(context: Context): DetailUserViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: DetailUserViewModelFactory(
                    Injection.detailUserRepository(context)
                )
            }.also { instance = it }
    }
}