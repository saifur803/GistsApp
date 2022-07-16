package com.saifurrahman.gist.api

import com.saifurrahman.gist.model.Gist
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ApiAdapter() {
    private val apiClient = ApiClient.getInstance()

    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob)

    fun getGistList(onGistListDataCallback: OnGistListDataCallback) {
        fun afterCall(response: ApiResponse<Gist>) {
            coroutineScope.launch(Dispatchers.Main) {
                response.error?.let {
                    onGistListDataCallback.onError(it)
                    return@launch
                }

                onGistListDataCallback.onData(response.dataList)
            }
        }

        coroutineScope.launch(Dispatchers.IO) {
            val response = apiClient.callGetGistList()
            afterCall(response)
        }
    }

    fun getGistListByUser(username: String, onGistListDataCallback: OnGistListDataCallback) {
        fun afterCall(response: ApiResponse<Gist>) {
            coroutineScope.launch(Dispatchers.Main) {
                response.error?.let {
                    onGistListDataCallback.onError(it)
                    return@launch
                }

                onGistListDataCallback.onData(response.dataList)
            }
        }

        coroutineScope.launch(Dispatchers.IO) {
            val response = apiClient.callGetGistByUser(username)
            afterCall(response)
        }
    }
}

interface OnGistListDataCallback {
    fun onData(gists: List<Gist>)
    fun onError(error: String)
}