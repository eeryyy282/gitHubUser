package com.example.githubuser.ui

import retrofit2.Callback
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.data.response.UserDetailResponse
import com.example.githubuser.data.retrofit.ApiConfig
import com.example.githubuser.databinding.ActivityDetailUserBinding
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Response

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        private const val TAG = "DetailUserActivity"

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val username = intent.getStringExtra(EXTRA_USERNAME)
        username?.let { findDetailUser(it) }

    }


    private fun findDetailUser(usernameDetail: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().getDetailUser(usernameDetail)
        client.enqueue(object : Callback<UserDetailResponse>{
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                showLoading(false)
                if(response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setUserData(responseBody)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    showErrorSnackbar("Gagal untuk mendapatkan Detail User :(.")
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })

    }

    private fun setUserData(responseBody: UserDetailResponse?) {
            with(binding) {
                tvId.text = responseBody?.id.toString()
                tvName.text = responseBody?.name.toString()
                tvUsernameDetail.text = responseBody?.login.toString()
                Glide.with(binding.root)
                    .load(responseBody?.avatarUrl)
                    .into(binding.ivUser)
            }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showErrorSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}