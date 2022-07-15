package com.saifurrahman.gist.api

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {
    @GET("gists/public?since")
    suspend fun getGistList()

    @GET("users/{username}/gists?since")
    suspend fun getGistByUser(@Path(value = "username") username: String)
}