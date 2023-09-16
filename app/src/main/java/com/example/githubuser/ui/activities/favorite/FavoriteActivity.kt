package com.example.githubuser.ui.activities.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.data.local.entity.FavoriteUser
import com.example.githubuser.databinding.ActivityFavoriteBinding
import com.example.githubuser.ui.adapter.FavoriteUserAdapter

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.favorite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val favoriteViewModel : FavoriteViewModel by viewModels {
            FavoriteViewModelFactory(application)
        }

        //observe favoriteUsers
        favoriteViewModel.favoriteUsers.observe(this) { favoriteUsers ->
            if (favoriteUsers.isNotEmpty()) {
                setFavoriteUserData(favoriteUsers)
            } else {
                setFavoriteUserData(emptyList())
            }
        }


    }

    private fun setFavoriteUserData(data : List<FavoriteUser> ) {
        val userAdapter = FavoriteUserAdapter(data)
        with(binding) {
            rvFavorite.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvFavorite.adapter = userAdapter
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}