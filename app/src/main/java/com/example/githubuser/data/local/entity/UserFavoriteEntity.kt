package com.example.githubuser.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class UserFavoriteEntity(
    @PrimaryKey(autoGenerate = false)

    @ColumnInfo(name = "username")
    var username: String = "",

    @ColumnInfo(name = "avatar_url")
    var avatarUrl: String? = null,

    @ColumnInfo(name = "id")
    var id: Int? = null
) : Parcelable