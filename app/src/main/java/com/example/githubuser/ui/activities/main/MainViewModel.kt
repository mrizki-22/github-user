package com.example.githubuser.ui.activities.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.data.UserRepository
import com.example.githubuser.data.remote.response.UserItems

class MainViewModel(application: Application) : ViewModel() {
    private val repository = UserRepository(application)

    private val _userItems = MutableLiveData<List<UserItems>>()
    val userItems: LiveData<List<UserItems>> = _userItems

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    companion object {
        private const val TAG = "MainViewModel"
    }


    fun getSearchResult(username: String) {
        _isLoading.value = true
        repository.getSearchResult(username) { result ->
            _isLoading.value = false

            result.onSuccess {
                _userItems.value = it.items
            }.onFailure {
                Log.e(TAG, "Error: ${it.message}")
            }
        }
    }
}

//factory
class MainViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(application) as T
    }

}