package com.example.githubuser.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubuser.data.local.entity.FavoriteUser
import com.example.githubuser.data.local.room.FavoriteUserDao
import com.example.githubuser.data.local.room.FavoriteUserDatabase
import com.example.githubuser.ui.activities.detail.DetailActivity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class FavoriteUserRepository(application: Application) {
    private val mFavoriteUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserDatabase.getDatabase(application)
        mFavoriteUserDao = db.favoriteUserDao()
    }

    fun getAll(): LiveData<List<FavoriteUser>> = mFavoriteUserDao.getAll()

    fun insert(favoriteUser: FavoriteUser) {
        executorService.execute {
            mFavoriteUserDao.insert(favoriteUser)
        }
    }

    fun delete(favoriteUser: FavoriteUser) {
        executorService.execute{
            mFavoriteUserDao.delete(favoriteUser)
        }
    }

    fun isFavorite(username: String): LiveData<Boolean> {
        return mFavoriteUserDao.isFavorite(username)
    }

}


