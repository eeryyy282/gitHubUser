package com.example.githubuser.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.response.UserResponse
import com.example.githubuser.data.response.Users
import com.example.githubuser.data.retrofit.ApiConfig
import com.example.githubuser.utils.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _userResponse = MutableLiveData<List<Users>>()
    val userResponse: LiveData<List<Users>> = _userResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    fun findUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsers(username)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val users = response.body()?.items
                    if (users.isNullOrEmpty()) {
                        _snackbarText.value = Event("Maaf, username tidak ditemukan :(")
                    } else {
                        _userResponse.value = users
                    }
                } else {
                    _snackbarText.value = Event("Gagal memuat user | ${response.message()}")
                    Log.e(TAG, "onfailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                _snackbarText.value = Event("Gagal memuat user | ${t.message}")
                Log.e(TAG, "onfailure: ${t.message}")
            }

        })
    }

    fun snackBar(message: String) {
        _snackbarText.value = Event(message)
    }

    companion object {
        private const val TAG = "MainActivityGitHubUser"
    }
}