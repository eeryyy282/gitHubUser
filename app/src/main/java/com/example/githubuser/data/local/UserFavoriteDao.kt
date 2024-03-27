package com.example.githubuser.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface UserFavoriteDao {
    @Query("SELECT * from userfavorite ORDER BY username ASC")
    fun getAllUsersFavorite(): LiveData<List<UserFavorite>>
}