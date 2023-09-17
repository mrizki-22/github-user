package com.example.githubuser.ui.activities.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.data.local.datastore.SettingPreferences
import com.example.githubuser.data.local.datastore.datastore
import com.example.githubuser.data.remote.response.UserItems
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.ui.activities.favorite.FavoriteActivity
import com.example.githubuser.ui.activities.setting.SettingActivity
import com.example.githubuser.ui.activities.setting.SettingViewModel
import com.example.githubuser.ui.activities.setting.SettingViewModelFactory
import com.example.githubuser.ui.adapter.UserAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        //theme setting
        val pref = SettingPreferences.getInstance(application.datastore)
        val settingViewModel: SettingViewModel by viewModels {
            SettingViewModelFactory(pref)
        }
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }


        val mainViewModel : MainViewModel by viewModels() {
            MainViewModelFactory(application)
        }

        //observe userItems
        mainViewModel.userItems.observe(this) { users ->
            if (users.isNotEmpty()) {
                setUserData(users)
                binding.notFound.visibility = View.GONE
            } else {
                setUserData(emptyList())
                binding.notFound.visibility = View.VISIBLE
                Log.e(TAG, "onCreate: users is null")
            }
        }

        //observe isLoading
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        //search bar
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    mainViewModel.getSearchResult(textView.text.toString())

                    searchBar.text = searchView.text
                    searchView.hide()
                    false
                }
            searchBar.inflateMenu(R.menu.menu_main)
            searchBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.setting -> {
                        val intent = Intent(this@MainActivity, SettingActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.favorite -> {
                        val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
        }

    }


    private fun setUserData(users: List<UserItems>) {
        val adapter = UserAdapter(users)
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvUsers.adapter = adapter

    }

    private fun showLoading(b: Boolean) {
        if (b) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        if (binding.searchView.isShowing) {
            binding.searchView.hide()
        } else {
            super.onBackPressed()
        }

    }
}