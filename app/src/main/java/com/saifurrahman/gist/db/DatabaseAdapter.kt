package com.saifurrahman.gist.db

import com.saifurrahman.gist.db.model.FavouriteGist
import com.saifurrahman.gist.model.Gist
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DatabaseAdapter {
    private val database = AppDatabase.getInstance()

    fun changeFavouriteState(gist: Gist) {
        GlobalScope.launch {
            if (gist.isFavourite) {
                database.favoriteGistDao().insertFavouriteGist(
                    FavouriteGist(gist.id, true)
                )
            } else {
                database.favoriteGistDao().deleteFavouriteGist(gist.id)
            }
        }
    }

}