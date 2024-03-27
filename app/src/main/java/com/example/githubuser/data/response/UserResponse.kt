package com.example.githubuser.data.response

import com.google.gson.annotations.SerializedName

data class UserResponse(

    @field:SerializedName("total_count") val totalCount: Int,

    @field:SerializedName("incomplete_results") val incompleteResults: Boolean,

    @field:SerializedName("items") val items: List<Users>
)

data class Users(

    @field:SerializedName("login") val login: String,

    @field:SerializedName("avatar_url") val avatarUrl: String,

    @field:SerializedName("id") val id: Int,

    )
