package com.example.githubuser.ui.detail.follow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.Result
import com.example.githubuser.data.remote.response.FollowUserResponseItem
import com.example.githubuser.data.repository.FollowingRepository

class FollowingViewModel(
    private val followingRepository: FollowingRepository
) : ViewModel() {

    private val _following = MutableLiveData<Result<List<FollowUserResponseItem>>>()
    val following: LiveData<Result<List<FollowUserResponseItem>>>
        get() = _following

    fun getFollowing(username: String) {
        _following.value = Result.Loading

        followingRepository.findFollowing(username) { result ->
            _following.value = result
        }
    }
}