package com.example.githubuser.ui.activities.favorite

import android.app.Application
import androidx.lifecycle.LiveData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.data.FavoriteUserRepository
import com.example.githubuser.data.local.entity.FavoriteUser

class FavoriteViewModel(application: Application) : ViewModel() {
    private val repository = FavoriteUserRepository(application)

    val favoriteUsers : LiveData<List<FavoriteUser>> = repository.getAll()
}

class FavoriteViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}