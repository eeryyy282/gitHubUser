package com.example.githubuser.data.remote.response

import com.google.gson.annotations.SerializedName

data class FollowUserResponseItem(

    @field:SerializedName("login") val login: String? = null,

    @field:SerializedName("url") val url: String? = null,

    @field:SerializedName("avatar_url") val avatarUrl: String? = null,

    @field:SerializedName("id") val id: Int? = null,
)
