package com.theseuntaylor.picsomeapp.lib_home.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.theseuntaylor.picsomeapp.lib_home.domain.model.Photo

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
    var isFavourite: Boolean = false,
    /** Picsum list page this photo came from; the next page to fetch is MAX(page) + 1. */
    @ColumnInfo(defaultValue = "1") val page: Int = 1,
    /** Order within its page, so the cached grid keeps the order it was first shown in. */
    @ColumnInfo(defaultValue = "0") val position: Int = 0,
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