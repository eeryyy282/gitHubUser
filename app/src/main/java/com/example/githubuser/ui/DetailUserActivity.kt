package com.example.githubuser.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.data.response.UserDetailResponse
import com.example.githubuser.databinding.ActivityDetailUserBinding
import com.google.android.material.snackbar.Snackbar


class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private var isDataLoaded = false

    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        supportActionBar?.hide()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val detailUserViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailUserViewModel::class.java)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        if (username != null) {
            detailUserViewModel.setUsername(username)
        }

        detailUserViewModel.userDetail.observe(this) { userDetailData ->
            setUserData(userDetailData)
            isDataLoaded = true
            showLoading(false)
        }

        detailUserViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailUserViewModel.snackbarText.observe(this) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(
                    window.decorView.rootView,
                    snackBarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setUserData(responseBody: UserDetailResponse?) {
        with(binding) {
            tvId.text = responseBody?.id.toString()
            tvName.text = responseBody?.name.toString()
            tvUsernameDetail.text = responseBody?.login.toString()
            Glide.with(binding.root)
                .load(responseBody?.avatarUrl)
                .into(binding.ivUser)
            tvFollowers.text = "${responseBody?.followers.toString()} Followers"
            tvFollowing.text = "${responseBody?.following.toString()} Following"
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isDataLoaded) {
            binding.progressBar.visibility = View.GONE
        } else {
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
}
