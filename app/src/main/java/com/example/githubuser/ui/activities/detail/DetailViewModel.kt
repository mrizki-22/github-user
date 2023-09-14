package com.example.githubuser.ui.activities.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.data.FavoriteUserRepository
import com.example.githubuser.data.local.entity.FavoriteUser
import com.example.githubuser.data.remote.response.UserDetailResponse
import com.example.githubuser.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//@TODO : Migrasi viewmodel ke repository

class DetailViewModel(private val username: String, application: Application) : ViewModel() {
    private val repository = FavoriteUserRepository(application)

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
        val client = ApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _userDetail.value = responseBody as UserDetailResponse
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })

    }

    fun addToFavorite(username: String, avatarUrl: String) {
        val user = FavoriteUser(
            username = username,
            avatarUrl = avatarUrl
        )
        repository.insert(user)
    }

    fun isFavorite(username: String): LiveData<Boolean> {
        return repository.isFavorite(username)
    }

    fun removeFromFavorite(username: String) {
        repository.delete(FavoriteUser(username, ""))

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


