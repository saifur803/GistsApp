package com.saifurrahman.gist.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.saifurrahman.gist.db.model.FavouriteGist

@Dao
abstract class FavoriteGistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertFavouriteGist(favouriteGist: FavouriteGist)

    @Query("DELETE FROM FavouriteGist WHERE gistId = :gistId")
    abstract suspend fun deleteFavouriteGist(gistId: String)

    @Query("SELECT * FROM FavouriteGist WHERE gistId = :gistId")
    abstract suspend fun getFavouriteGist(gistId: String): FavouriteGist?

    @Query("SELECT * FROM FavouriteGist")
    abstract suspend fun getFavouriteGists(): List<FavouriteGist>


}