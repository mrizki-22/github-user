package com.example.githubuser.ui.activities.detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.githubuser.data.response.UserDetailResponse
import com.example.githubuser.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    companion object {
        val EXTRA_USERNAME: String = "extra_username"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //get username from intent
        val username= intent.getStringExtra(EXTRA_USERNAME)

        val detailViewModelFactory = DetailViewModelFactory(username.toString())

        val detailViewModel =
            ViewModelProvider(this, detailViewModelFactory)[DetailViewModel::class.java]


        //observe userdetail
        detailViewModel.userDetail.observe(this) { userDetail ->
            if (userDetail != null) {
                setDetailData(userDetail)
            }
        }

        //observe isLoading
        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        //pager adapter
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username.toString()
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