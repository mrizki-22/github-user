package com.example.githubuser.data.remote.response

import com.google.gson.annotations.SerializedName

data class GithubSearchResponse(
    @field:SerializedName("items")
	val items: List<UserItems>,
)

data class UserItems(

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@field:SerializedName("login")
	val login: String,
)
