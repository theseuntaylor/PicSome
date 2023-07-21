package com.application.storelabs.lib_home.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.application.storelabs.lib_home.domain.model.Photo

@Entity(
    tableName = "photos"
)
data class PhotoEntity(
    @PrimaryKey val id: String,
    val author: String,
    val width: Int,
    val height: Int,
    val url: String,
    val download_url: String,
    var isFavourite: Boolean = false
)

fun PhotoEntity.toDomainModel() = Photo(
    id = id,
    author = author,
    width = width,
    height = height,
    url = url,
    download_url = download_url,
    isFavourite = isFavourite
)