package com.saifurrahman.gist.api

import com.saifurrahman.gist.api.ApiConstants.CONNECTION_TIMEOUT
import com.saifurrahman.gist.api.ApiConstants.READ_TIMEOUT
import com.saifurrahman.gist.api.ApiConstants.WRITE_TIMEOUT
import com.saifurrahman.gist.api.deserializer.GistTypeAdapter
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient() {
    companion object {

    }

    val apiInterface: ApiInterface

    init {
        val client = OkHttpClient.Builder().run {
            connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            build()
        }

        val moshi = Moshi.Builder().run {
            add(GistTypeAdapter.Factory)
            build()
        }

        apiInterface = Retrofit.Builder().run {
            baseUrl(ApiConstants.baseHostUrl)
            addConverterFactory(MoshiConverterFactory.create(moshi))
            client(client)
            build()
        }.create(ApiInterface::class.java)
    }
}