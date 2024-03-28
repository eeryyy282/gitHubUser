package com.example.githubuser.ui.detail.follow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.Result
import com.example.githubuser.data.remote.response.FollowUserResponseItem
import com.example.githubuser.data.repository.FollowersRepository

class FollowersViewModel(
    private val followersRepository: FollowersRepository
) : ViewModel() {
    private val _followers = MutableLiveData<Result<List<FollowUserResponseItem>>>()
    val followers: LiveData<Result<List<FollowUserResponseItem>>>
        get() = _followers

    fun getFollowers(username: String) {
        _followers.value = Result.Loading

        followersRepository.findFollowers(username) { result ->
            _followers.value = result
        }
    }

}
