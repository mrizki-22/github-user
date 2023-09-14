package com.example.githubuser.ui.activities.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.data.remote.response.UserItems
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.ui.activities.setting.SettingActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[MainViewModel::class.java]

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
                    // mainviewmodel
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