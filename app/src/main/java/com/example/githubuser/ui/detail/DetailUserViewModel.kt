package com.example.githubuser.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.Result
import com.example.githubuser.data.remote.response.UserDetailResponse
import com.example.githubuser.data.repository.DetailUserRepository

class DetailUserViewModel(
    private val detailUserRepository: DetailUserRepository
) : ViewModel() {

    private val _detailUser = MutableLiveData<Result<UserDetailResponse>>()
    val detailUser: LiveData<Result<UserDetailResponse>>
        get() = _detailUser

    fun getDetailUser(username: String) {
        _detailUser.value = Result.Loading

        detailUserRepository.findUserDetail(username) { result ->
            _detailUser.value = result
        }
    }
}