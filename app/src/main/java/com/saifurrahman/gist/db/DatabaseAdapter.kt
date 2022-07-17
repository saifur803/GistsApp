package com.saifurrahman.gist.db

import com.saifurrahman.gist.db.model.FavouriteGist
import com.saifurrahman.gist.model.Gist
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class DatabaseAdapter {
    private val database = AppDatabase.getInstance()
    var onDatabaseCallback: OnDatabaseCallback? = null

    fun changeFavouriteState(gist: Gist) {
        GlobalScope.launch {
            database.gistDao().insertGist(gist)

            if (gist.isFavourite) {
                database.favoriteGistDao().insertFavouriteGist(
                    FavouriteGist(gist.id, true)
                )
            } else {
                database.favoriteGistDao().deleteFavouriteGist(gist.id)
            }
       }
    }

    fun getGists() {
        GlobalScope.launch {
            val gists = database.gistDao().getGists()
            MainScope().launch {
                onDatabaseCallback?.onGists(gists)
            }
        }
    }

    fun getGist(id: String) {
        GlobalScope.launch {
            val gist = database.gistDao().getGist(id)
            MainScope().launch {
                gist?.let {
                    onDatabaseCallback?.onGist(it)
                }
            }
        }
    }

}

interface OnDatabaseCallback {
    fun onGists(gists: List<Gist>) {}
    fun onGist(gist: Gist) {}
}