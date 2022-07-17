package com.saifurrahman.gist.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.saifurrahman.gist.db.dao.FavoriteGistDao
import com.saifurrahman.gist.db.dao.GistDao
import com.saifurrahman.gist.db.model.FavouriteGist
import com.saifurrahman.gist.model.Gist

@Database(
    entities = [
        FavouriteGist::class,
        Gist::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val TAG = "AppDatabase"
        const val DB_NAME = "gist_app_database"

        private lateinit var instance: AppDatabase

        fun initialize(context: Context) {
            if (::instance.isInitialized) {
                Log.i(TAG, "App Database already initialized")
                return
            }

            instance = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }

        fun getInstance(): AppDatabase {
            if (!::instance.isInitialized) {
                throw Exception("AppDatabase not initialized yet, Call AppDatabase.initialize()")
            }

            return instance
        }
    }

    abstract fun favoriteGistDao(): FavoriteGistDao

    abstract fun gistDao(): GistDao
}