package com.saifurrahman.gist.db.dao

import androidx.room.*
import com.saifurrahman.gist.model.Gist

@Dao
abstract class GistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertGist(gist: Gist)

    @Update
    abstract suspend fun updateGist(gist: Gist)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertGists(gists: List<Gist>)

    @Query("DELETE FROM Gist")
    abstract suspend fun deleteAllGist()

    @Query("SELECT * FROM Gist WHERE id = :id")
    abstract suspend fun getGist(id: String): Gist?

    @Query("SELECT * FROM Gist")
    abstract suspend fun getGists(): List<Gist>

    @Query("SELECT * FROM Gist WHERE isFavourite = 1")
    abstract suspend fun getFavouriteGists(): List<Gist>
}