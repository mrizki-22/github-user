package com.example.githubuser.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_user")
data class FavoriteUser(
    @PrimaryKey(autoGenerate = false)  val username : String,

    val avatarUrl : String,
)
