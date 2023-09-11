package com.example.githubuser.data.retrofit

import com.example.githubuser.data.response.GithubSearchResponse
import com.example.githubuser.data.response.UserDetailResponse
import com.example.githubuser.data.response.UserItems
import retrofit2.Call
import retrofit2.http.*

interface  ApiService {

    @GET("search/users")
    fun searchUsers(
        @Query("q") query: String
    ): Call<GithubSearchResponse>

    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<List<UserItems>>

    @GET("users/{username}/following")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<List<UserItems>>
}