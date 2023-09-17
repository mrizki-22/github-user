package com.example.githubuser.ui.activities.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.data.UserRepository
import com.example.githubuser.data.local.entity.FavoriteUser
import com.example.githubuser.data.remote.response.UserDetailResponse

class DetailViewModel(private val username: String, application: Application) : ViewModel() {
    private val repository = UserRepository(application)

    private val _userDetail = MutableLiveData<UserDetailResponse>()
    val userDetail: LiveData<UserDetailResponse> = _userDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    companion object {
        private const val TAG = "DetailViewModel"
    }

    init {
        getUserDetail(username)
    }

    private fun getUserDetail(username: String) {
        _isLoading.value = true

        repository.getUserDetail(username) { result ->
            _isLoading.value = false

            result.onSuccess {
                _userDetail.value = it
            }.onFailure {

                Log.e(TAG, "Error: ${it.message}")
            }
        }
    }

    fun addToFavorite(username: String, avatarUrl: String) {
        val user = FavoriteUser(
            username = username,
            avatarUrl = avatarUrl
        )
        repository.insertFavoriteUser(user)
    }

    fun isFavorite(username: String): LiveData<Boolean> {
        return repository.isFavorite(username)
    }

    fun removeFromFavorite(username: String) {
        repository.deleteFavoriteUser(FavoriteUser(username, ""))

    }

}

class DetailViewModelFactory(
    private val username: String,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(username, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


