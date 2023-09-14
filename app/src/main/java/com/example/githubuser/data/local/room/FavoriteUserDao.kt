package com.example.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.githubuser.data.local.entity.FavoriteUser


@Dao
interface FavoriteUserDao {
    @Query("SELECT * FROM favorite_user")
    fun getAll(): LiveData<List<FavoriteUser>>

    @Insert
    fun insert(favoriteUser: FavoriteUser)

    @Delete
    fun delete(favoriteUser: FavoriteUser)

    @Query("SELECT EXISTS (SELECT 1 FROM favorite_user WHERE username = :username)")
    fun isFavorite(username: String): LiveData<Boolean>
}