package com.saifurrahman.gist.api

import com.saifurrahman.gist.db.AppDatabase
import com.saifurrahman.gist.model.Gist
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ApiAdapter {
    private val apiClient = ApiClient.getInstance()
    private val database = AppDatabase.getInstance()

    private val mutex = Mutex()
    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob)

    fun getGistList(onGistListDataCallback: OnGistListDataCallback) {
        fun afterCall(list: List<Gist>, error: String?) {
            coroutineScope.launch(Dispatchers.Main) {
                error?.let {
                    onGistListDataCallback.onError(it, database.gistDao().getGists())
                    return@launch
                }

                onGistListDataCallback.onData(list)
            }
        }

        coroutineScope.launch(Dispatchers.IO) {
            val response = apiClient.callGetGistList()
            val gistList = response.dataList

            val gistDao = database.gistDao()
            if (gistList.isNotEmpty()) {
                gistList.forEach con@ {
                    it.gistByUserIsLoading = false
                    val favouriteGist = database.favoriteGistDao().getFavouriteGist(it.id) ?: return@con
                    it.isFavourite = favouriteGist.isFavourite
                }
                gistDao.insertGists(gistList)
            }

            afterCall(gistDao.getGists(), response.error)
        }
    }

    fun getGistListByUser(gist: Gist, onGistByUserDataCallback: OnGistByUserDataCallback) {
        fun afterCall(gist: Gist?, error: String?) {
            coroutineScope.launch(Dispatchers.Main) {
                if (gist != null && error == null) {
                    onGistByUserDataCallback.onData(gist)
                    return@launch
                }

                onGistByUserDataCallback.onError(error?: "", gist)
            }
        }

        coroutineScope.launch(Dispatchers.IO) {
            mutex.withLock {
                val response = apiClient.callGetGistByUser(gist.username)
                val gistList = response.dataList

                if (response.error == null) {
                    val gistDao = database.gistDao()
                    gist.gistCount = gistList.size
                    gistDao.updateGist(gist)
                }

                afterCall(gist, response.error)
            }
        }
    }
}

interface OnGistListDataCallback {
    fun onData(gists: List<Gist>)
    fun onError(error: String, cachedGist: List<Gist>)
}

interface OnGistByUserDataCallback {
    fun onData(gist: Gist)
    fun onError(error: String, cached: Gist?)
}