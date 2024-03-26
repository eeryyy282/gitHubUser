package com.example.githubuser.ui.follow

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.response.FollowUserResponseItem
import com.example.githubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _followerResponse = MutableLiveData<List<FollowUserResponseItem>>()
    val followerResponse: LiveData<List<FollowUserResponseItem>> = _followerResponse

    fun getFollowers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<FollowUserResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowUserResponseItem>>,
                response: Response<List<FollowUserResponseItem>>
            ) {
                if (response.isSuccessful) {
                    val followers = response.body()
                    _followerResponse.value = followers
                } else {
                    Log.e(TAG, "onfailure: ${response.message()}")
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<List<FollowUserResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onfailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "DetailUserActivity"
    }
}
