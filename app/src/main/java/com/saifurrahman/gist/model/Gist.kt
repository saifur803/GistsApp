package com.saifurrahman.gist.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity
@JsonClass(generateAdapter = true)
data class Gist(
    @PrimaryKey
    val id: String,
    val url: String,
    val filename: String,
    var username: String
) {
    var createdAt: String? = null
        internal set
    var updatedAt: String? = null
        internal set
    var description: String? = null
        internal set
    var userImageUrl: String? = null
        internal set
    var isFavourite: Boolean = false
    internal set

    var gistCount: Int = 0

    var gistByUserIsLoading = true
}