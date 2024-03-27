package com.example.githubuser.ui.detail.follow

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.response.FollowUserResponseItem
import com.example.githubuser.data.retrofit.ApiConfig
import com.example.githubuser.utils.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _followingResponse = MutableLiveData<List<FollowUserResponseItem>>()
    val followingResponse: LiveData<List<FollowUserResponseItem>> = _followingResponse

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    private val _isListEmpty = MutableLiveData<Boolean>()
    val isListEmpty: LiveData<Boolean> = _isListEmpty

    fun getFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<FollowUserResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowUserResponseItem>>,
                response: Response<List<FollowUserResponseItem>>
            ) {
                if (response.isSuccessful) {
                    val following = response.body()
                    _followingResponse.value = following
                    _isListEmpty.value = following.isNullOrEmpty()
                } else {
                    _snackbarText.value = Event("Gagal memuat following | ${response.message()}")
                    Log.e(TAG, "onfailure: ${response.message()}")
                }
                _isLoading.value = false
            }


            override fun onFailure(call: Call<List<FollowUserResponseItem>>, t: Throwable) {
                _isLoading.value = false
                _snackbarText.value = Event("Gagal memuat following | ${t.message}")
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    companion object {
        private const val TAG = "DetailUserActivity"
    }

}