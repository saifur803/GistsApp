package com.saifurrahman.gist.api

import com.saifurrahman.gist.model.Gist
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ApiInterface {
    @GET("gists/public?since")
    suspend fun getGistList(): List<Gist>

    @GET("users/{username}/gists?since")
    suspend fun getGistByUser(@Path(value = "username") username: String): List<Gist>
}