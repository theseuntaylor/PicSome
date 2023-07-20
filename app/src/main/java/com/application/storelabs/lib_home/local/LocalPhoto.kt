package com.application.storelabs.lib_home.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "photos"
)
data class LocalPhoto(
    @PrimaryKey val id: String,
    val author: String,
    val width: Int,
    val height: Int,
    val url: String,
    val download_url: String,
    val isFavourite: String
)