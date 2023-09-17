package com.example.githubuser.ui.activities.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.data.UserRepository
import com.example.githubuser.data.remote.response.UserItems

class FollowViewModel(private val username: String, application: Application) : ViewModel() {
    private val repository = UserRepository(application)

    private val _followers = MutableLiveData<List<UserItems>?>()
    val followers: LiveData<List<UserItems>?> = _followers

    private val _following = MutableLiveData<List<UserItems>?>()
    val following: LiveData<List<UserItems>?> = _following

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "FollowViewModel"
    }

    init {
        getFollowers(username)
        getFollowing(username)
    }

    fun getFollowers(username: String) {
        _isLoading.value = true

        repository.getFollowers(username) { result ->
            _isLoading.value = false

            result.onSuccess {
                _followers.value = it
            }.onFailure {
                Log.e(TAG, "Error: ${it.message}")
            }
        }
    }

    fun getFollowing(username: String) {
        _isLoading.value = true

        repository.getFollowing(username) { result ->
            _isLoading.value = false

            result.onSuccess {
                _following.value = it
            }.onFailure {
                Log.e(TAG, "Error: ${it.message}")
            }
        }
    }
}

class FollowViewModelFactory(private val username: String, private val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FollowViewModel::class.java)) {
            return FollowViewModel(username, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}