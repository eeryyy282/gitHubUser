package com.example.githubuser.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.Result
import com.example.githubuser.data.remote.response.Users
import com.example.githubuser.data.repository.HomeRepository

class HomeViewModel(
    private val homeRepository: HomeRepository,
) : ViewModel() {
    private val _users = MutableLiveData<Result<List<Users>>>()
    val users: LiveData<Result<List<Users>>>
        get() = _users

    fun findUsers(username: String) {
        _users.value = Result.Loading

        homeRepository.findUsers(username) { result ->
            _users.value = result
        }
    }
}