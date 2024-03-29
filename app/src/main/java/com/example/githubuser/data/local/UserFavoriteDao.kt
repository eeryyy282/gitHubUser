package com.example.githubuser.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.githubuser.data.local.entity.UserFavoriteEntity

@Dao
interface UserFavoriteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addFavoriteUser(userFavoriteEntity: UserFavoriteEntity)

    @Delete
    fun removeFavoriteUser(userFavoriteEntity: UserFavoriteEntity)

    @Query("SELECT * FROM UserFavoriteEntity WHERE username = :username")
    fun getUsersFavoriteByUsername(username: String): LiveData<UserFavoriteEntity>
}