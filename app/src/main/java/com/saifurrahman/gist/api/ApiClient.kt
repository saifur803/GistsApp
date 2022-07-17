package com.saifurrahman.gist.api

import android.util.Log
import com.saifurrahman.gist.api.ApiConstants.CONNECTION_TIMEOUT
import com.saifurrahman.gist.api.ApiConstants.READ_TIMEOUT
import com.saifurrahman.gist.api.ApiConstants.WRITE_TIMEOUT
import com.saifurrahman.gist.api.deserializer.GistTypeAdapter
import com.saifurrahman.gist.model.Gist
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    companion object {
        private var instance: ApiClient? = null
        fun getInstance(): ApiClient {
            if (instance == null) {
                instance = ApiClient()
            }
            return instance!!
        }
    }

    private val TAG = "ApiClient"
    val apiInterface: ApiInterface

    init {
        val client = OkHttpClient.Builder().run {
            connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            addInterceptor(AuthenticatorInterceptor())
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

    suspend fun callGetGistList(): ApiResponse<Gist> {
        Log.i(TAG, "Gist List Api triggered")

        val apiResponse = ApiResponse<Gist>(emptyList())

        try {
            val gistList = apiInterface.getGistList()
            Log.i(TAG, "Gist List Api finished")

            apiResponse.dataList = gistList
            return apiResponse
        } catch (e: Exception) {
            Log.e(TAG, "Gist List Api failed")
            e.printStackTrace()

            apiResponse.error = e.message?: e.localizedMessage ?: "Error calling Gist List Api"
            return apiResponse
        }
    }

    suspend fun callGetGistByUser(username: String): ApiResponse<Gist> {
        Log.i(TAG, "Gist List Api by user $username triggered")

        val apiResponse = ApiResponse<Gist>(emptyList())

        try {
            val gistList = apiInterface.getGistByUser(username)
            Log.i(TAG, "Gist List Api by user $username finished")

            apiResponse.dataList = gistList
            return apiResponse
        } catch (e: Exception) {
            Log.e(TAG, "Gist List Api by user $username failed")
            e.printStackTrace()

            apiResponse.error = e.message?: e.localizedMessage ?: "Error calling Gist List Api"
            return apiResponse
        }
    }
}

class AuthenticatorInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .addHeader("authorization", ApiConstants.accessToken)
            .build()

        return chain.proceed(newRequest)
    }

}

class ApiResponse<T>(list: List<T>) {
    var dataList = list
    var error: String? = null
}