package com.example.githubuser.ui.activities.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.data.remote.response.UserDetailResponse
import com.example.githubuser.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding


    companion object {
        const val EXTRA_USERNAME: String = "extra_username"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val username : String = intent.getStringExtra(EXTRA_USERNAME).toString()

        val detailViewModel: DetailViewModel by viewModels {
            DetailViewModelFactory(username, application)
        }

        detailViewModel.userDetail.observe(this) { userDetail ->
            if (userDetail != null) {
                setDetailData(userDetail)
            }
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailViewModel.isFavorite(username).observe(this) { isFavorite ->
            if (isFavorite) {
                binding.fab.setImageResource(R.drawable.ic_favorite)
                binding.fab.setOnClickListener {
                    val user = detailViewModel.userDetail.value
                    if (user != null) {
                        detailViewModel.removeFromFavorite(user.login)
                    }
                }
            } else {
                binding.fab.setImageResource(R.drawable.ic_favorite_border)
                binding.fab.setOnClickListener {
                    val user = detailViewModel.userDetail.value
                    if (user != null) {
                        detailViewModel.addToFavorite(user.login, user.avatarUrl)
                    }
                }
            }
        }

        //pager adapter
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        val viewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Followers"
                1 -> tab.text = "Following"
            }
        }.attach()



    }



    private fun showLoading(it: Boolean?) {
        if (it == true) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDetailData(userDetail: UserDetailResponse) {
        binding.tvName.text = userDetail.name
        binding.tvUsername.text = userDetail.login
        binding.tvFollowers.text = "${userDetail.followers} Followers"
        binding.tvFollowing.text = "${userDetail.following} Following"

        Glide.with(this)
            .load(userDetail.avatarUrl)
            .into(binding.ivAvatar)
    }


}