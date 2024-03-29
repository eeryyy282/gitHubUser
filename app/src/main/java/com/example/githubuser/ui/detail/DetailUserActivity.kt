package com.example.githubuser.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.data.Result
import com.example.githubuser.data.local.entity.UserFavoriteEntity
import com.example.githubuser.databinding.ActivityDetailUserBinding
import com.example.githubuser.ui.MainActivityGitHubUser
import com.example.githubuser.ui.setting.SettingPreferences
import com.example.githubuser.ui.setting.SettingViewModel
import com.example.githubuser.ui.setting.SettingViewModelFactory
import com.example.githubuser.ui.setting.dataStore
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var detailUserViewModel: DetailUserViewModel
    private var isFavorite = false
    private var avatarUrl: String? = null
    private var id: Int? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factoryUserDetail: DetailUserViewModelFactory =
            DetailUserViewModelFactory.getInstance(application)
        detailUserViewModel =
            ViewModelProvider(this, factoryUserDetail)[DetailUserViewModel::class.java]

        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingViewModel = ViewModelProvider(
            this,
            SettingViewModelFactory(pref)
        )[SettingViewModel::class.java]

        settingViewModel.getThemeSetting().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
            } else {
                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val username = intent.getStringExtra(EXTRA_USERNAME)
        if (username != null) {
            if (detailUserViewModel.detailUser.value == null) {
                detailUserViewModel.getDetailUser(username)
            }
            detailUserViewModel.isFavoriteUser(username).observe(this) { favorite ->
                isFavorite = favorite
                invalidateOptionsMenu()
            }
        }

        detailUserViewModel.detailUser.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val detailUsersData = result.data
                        with(binding) {
                            tvName.text = detailUsersData.name
                            tvUsernameDetail.text = detailUsersData.login
                            tvId.text = detailUsersData.id.toString()
                            id = detailUsersData.id
                            tvOrganitation.text = detailUsersData.company
                            if (tvOrganitation.text.isNullOrEmpty()) {
                                tvOrganitation.text = "Organisasi belum ditambahkan"
                            }
                            tvLocation.text = detailUsersData.location
                            if (tvLocation.text.isNullOrEmpty()) {
                                tvLocation.text = "Lokasi belum ditambahkan"
                            }
                            tvFollowers.text = "${detailUsersData.followers}\nFollowers"
                            tvFollowing.text = "${detailUsersData.following}\nFollowing"
                            Glide.with(root)
                                .load(detailUsersData.avatarUrl)
                                .into(ivUser)
                            avatarUrl = detailUsersData.avatarUrl.toString()
                        }

                    }

                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            this,
                            "Terjadi kesalahan menemukan detail user " + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        val sectionPageAdapter = SectionPageAdapter(this, username)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionPageAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITTLES[position])
        }.attach()

        val toolbar: MaterialToolbar = binding.topAppBar
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_user_nav, menu)
        val favoriteMenuItem = menu?.findItem(R.id.favorite)
        favoriteMenuItem?.setIcon(if (isFavorite) R.drawable.baseline_favorite_24 else R.drawable.baseline_favorite_border_24)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, MainActivityGitHubUser::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
                startActivity(intent)
                true
            }

            R.id.favorite -> {
                val username = intent.getStringExtra(EXTRA_USERNAME)
                if (username != null && binding.tvUsernameDetail.text.isNotEmpty()) {
                    val userFavoriteEntity = UserFavoriteEntity(username)
                    if (isFavorite) {
                        detailUserViewModel.removeFavoriteUser(userFavoriteEntity)
                        Toast.makeText(
                            this,
                            "$username\ndihapus dari favorit",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        detailUserViewModel.setFavoriteUser(username, avatarUrl, id)
                        Toast.makeText(
                            this,
                            "$username\nditambahkan ke favorit",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                true
            }

            R.id.share -> {
                if (binding.tvUsernameDetail.text.isNotEmpty()) {
                    val url = "https://github.com/"
                    val username = binding.tvUsernameDetail.text
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "$url$username\nLihat siapa yang saya temukan! Cek profil $username dengan mengklik tautan di atas!"
                        )
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, "Bagikan $username")
                    startActivity(shareIntent)
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
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
