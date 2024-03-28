package com.example.githubuser.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.githubuser.data.local.entity.UserFavoriteEntity

@Database(entities = [UserFavoriteEntity::class], version = 1)
abstract class UserFavoriteDatabase : RoomDatabase() {
    abstract fun userFavoriteDao(): UserFavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: UserFavoriteDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): UserFavoriteDatabase {
            if (INSTANCE == null) {
                synchronized(UserFavoriteDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        UserFavoriteDatabase::class.java, "user_favorite_database"
                    )
                        .build()
                }
            }
            return INSTANCE as UserFavoriteDatabase
        }
    }
}