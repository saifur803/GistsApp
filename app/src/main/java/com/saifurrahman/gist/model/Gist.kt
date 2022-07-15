package com.saifurrahman.gist.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Gist(
    val id: String,
    val url: String,
    val filename: String,
    var username: String
) {
    var isFavourite: Boolean = false
    internal set
}