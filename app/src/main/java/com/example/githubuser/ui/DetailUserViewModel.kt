package com.example.githubuser.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.response.UserDetailResponse
import com.example.githubuser.data.retrofit.ApiConfig
import com.example.githubuser.utils.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel : ViewModel() {

    private val _username = MutableLiveData<String>()

    private val _userDetail = MutableLiveData<UserDetailResponse>()
    val userDetail: LiveData<UserDetailResponse> = _userDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText


    fun setUsername(username: String) {
        _username.value = username
        findUserDetail()
    }

    private fun findUserDetail() {
        _isLoading.value = true
        val username = _username.value
        if (!username.isNullOrEmpty()) {
            val client = ApiConfig.getApiService().getDetailUser(username)
            client.enqueue(object : Callback<UserDetailResponse> {
                override fun onResponse(
                    call: Call<UserDetailResponse>,
                    response: Response<UserDetailResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _userDetail.value = response.body()
                    } else {
                        _snackbarText.value =
                            Event("Gagal mendapatkan detail user : ${response.message()}")
                        Log.e(TAG, "onfailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                    _isLoading.value = false
                    _snackbarText.value = Event("Gagal mendapatkan detail User: ${t.message}")
                    Log.e(TAG, "onfailure: ${t.message}")
                }
            })
        }
    }

    companion object {
        private const val TAG = "DetailUserActivity"
    }
}