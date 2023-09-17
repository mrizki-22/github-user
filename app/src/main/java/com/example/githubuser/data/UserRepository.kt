package com.example.githubuser.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubuser.data.local.entity.FavoriteUser
import com.example.githubuser.data.local.room.FavoriteUserDao
import com.example.githubuser.data.local.room.FavoriteUserDatabase
import com.example.githubuser.data.remote.response.GithubSearchResponse
import com.example.githubuser.data.remote.response.UserDetailResponse
import com.example.githubuser.data.remote.response.UserItems
import com.example.githubuser.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class UserRepository(application: Application) {
    private val mFavoriteUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserDatabase.getDatabase(application)
        mFavoriteUserDao = db.favoriteUserDao()
    }

    fun getSearchResult(username: String, callback : (Result<GithubSearchResponse>) -> Unit) {
        val client = ApiConfig.getApiService().searchUsers(username)
        client.enqueue(object : Callback<GithubSearchResponse> {
            override fun onResponse(
                call: Call<GithubSearchResponse>,
                response: Response<GithubSearchResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback(Result.success(it))
                    } ?: callback(Result.failure(Throwable("No response body")))
                } else {
                    callback(Result.failure(Throwable(response.message())))
                }
            }

            override fun onFailure(call: Call<GithubSearchResponse>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    fun getUserDetail(username: String, callback: (Result<UserDetailResponse>) -> Unit) {
        val client = ApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback(Result.success(it))
                    } ?: callback(Result.failure(Throwable("No response body")))
                } else {
                    callback(Result.failure(Throwable(response.message())))
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    fun getFollowers(username: String, callback: (Result<List<UserItems>>) -> Unit) {
        val client = ApiConfig.getApiService().getUserFollowers(username)
        client.enqueue(object : Callback<List<UserItems>> {
            override fun onResponse(
                call: Call<List<UserItems>>,
                response: Response<List<UserItems>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback(Result.success(it))
                    } ?: callback(Result.failure(Throwable("No response body")))
                } else {
                    callback(Result.failure(Throwable(response.message())))
                }
            }

            override fun onFailure(call: Call<List<UserItems>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    fun getFollowing(username: String, callback: (Result<List<UserItems>>) -> Unit) {
        val client = ApiConfig.getApiService().getUserFollowing(username)
        client.enqueue(object : Callback<List<UserItems>> {
            override fun onResponse(
                call: Call<List<UserItems>>,
                response: Response<List<UserItems>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback(Result.success(it))
                    } ?: callback(Result.failure(Throwable("No response body")))
                } else {
                    callback(Result.failure(Throwable(response.message())))
                }
            }

            override fun onFailure(call: Call<List<UserItems>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> = mFavoriteUserDao.getAll()

    fun insertFavoriteUser(favoriteUser: FavoriteUser) {
        executorService.execute {
            mFavoriteUserDao.insert(favoriteUser)
        }
    }

    fun deleteFavoriteUser(favoriteUser: FavoriteUser) {
        executorService.execute{
            mFavoriteUserDao.delete(favoriteUser)
        }
    }

    fun isFavorite(username: String): LiveData<Boolean> {
        return mFavoriteUserDao.isFavorite(username)
    }

}


