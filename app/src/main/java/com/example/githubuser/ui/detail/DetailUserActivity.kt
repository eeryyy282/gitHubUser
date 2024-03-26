package com.example.githubuser.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.data.response.UserDetailResponse
import com.example.githubuser.databinding.ActivityDetailUserBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private var isDataLoaded = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val detailUserViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[DetailUserViewModel::class.java]

        val username = intent.getStringExtra(EXTRA_USERNAME)
        if (username != null) {
            detailUserViewModel.setUsername(username)
        }

        val sectionPageAdapter = SectionPageAdapter(this, username)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionPageAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITTLES[position])
        }.attach()

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

    @SuppressLint("SetTextI18n")
    private fun setUserData(responseBody: UserDetailResponse?) {
        with(binding) {
            tvId.text = responseBody?.id.toString()
            tvName.text = responseBody?.name.toString()
            tvUsernameDetail.text = responseBody?.login.toString()
            Glide.with(binding.root)
                .load(responseBody?.avatarUrl)
                .into(binding.ivUser)
            tvFollowers.text = resources.getString(R.string.followers, responseBody?.followers)
            tvFollowing.text = resources.getString(R.string.following, responseBody?.following)
        }
    }


    private fun showLoading(isLoading: Boolean) {
        if (isDataLoaded) {
            binding.progressBar.visibility = View.GONE
        } else {
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"

        @StringRes
        private val TAB_TITTLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}
