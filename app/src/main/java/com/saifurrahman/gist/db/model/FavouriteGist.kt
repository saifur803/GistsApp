package com.saifurrahman.gist.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavouriteGist(
    @PrimaryKey
    var gistId: String,
    var isFavourite: Boolean = false
)