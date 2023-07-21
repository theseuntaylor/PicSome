package com.application.storelabs.lib_home.data.fakes

import com.application.storelabs.lib_home.domain.model.Photo
import com.application.storelabs.lib_home.local.PhotoEntity
import com.application.storelabs.lib_home.remote.model.PhotoDto

object DummyData {
    val cachedFavouritePhotos = listOf(
        PhotoEntity(
            id = "102",
            author = "Ben Moore",
            width = 4320,
            height = 3240,
            url = "https://unsplash.com/photos/pJILiyPdrXI",
            download_url = "https://picsum.photos/id/102/4320/3240",
            isFavourite = true,
        ),
        PhotoEntity(
            id = "103",
            author = "Ben Moore",
            width = 4320,
            height = 3240,
            url = "https://unsplash.com/photos/pJILiyPdrXI",
            download_url = "https://picsum.photos/id/102/4320/3240",
            isFavourite = true,
        ),
    )
    val favouritePhotos = listOf(
        Photo(
            id = "102",
            author = "Ben Moore",
            width = 4320,
            height = 3240,
            url = "https://unsplash.com/photos/pJILiyPdrXI",
            download_url = "https://picsum.photos/id/102/4320/3240",
            isFavourite = true,
        ),
        Photo(
            id = "103",
            author = "Ben Moore",
            width = 4320,
            height = 3240,
            url = "https://unsplash.com/photos/pJILiyPdrXI",
            download_url = "https://picsum.photos/id/102/4320/3240",
            isFavourite = true,
        ),
    )

    val cachedPhotos = listOf(
        PhotoEntity(
            id = "102",
            author = "Ben Moore",
            width = 5320,
            height = 6240,
            url = "https://unsplash.com/photos/pJILiyPdrXI",
            download_url = "https://picsum.photos/id/102/4320/3240",
            isFavourite = false,
        ),
        PhotoEntity(
            id = "103",
            author = "Ben Moore",
            width = 4320,
            height = 3240,
            url = "https://unsplash.com/photos/pJILiyPdrXI",
            download_url = "https://picsum.photos/id/102/4320/3240",
            isFavourite = true,
        ),
    )
    val allPhotos = listOf(
        Photo(
            id = "102",
            author = "Ben Moore",
            width = 5320,
            height = 6240,
            url = "https://unsplash.com/photos/pJILiyPdrXI",
            download_url = "https://picsum.photos/id/102/4320/3240",
            isFavourite = false,
        ),
        Photo(
            id = "103",
            author = "Ben Moore",
            width = 4320,
            height = 3240,
            url = "https://unsplash.com/photos/pJILiyPdrXI",
            download_url = "https://picsum.photos/id/102/4320/3240",
            isFavourite = true,
        ),
    )
    val allPhotosFromRemote = listOf(
        Photo(
            id = "102",
            author = "Ben Moore",
            width = 5320,
            height = 6240,
            url = "https://unsplash.com/photos/pJILiyPdrXI",
            download_url = "https://picsum.photos/id/102/4320/3240",
            isFavourite = false,
        ),
        Photo(
            id = "103",
            author = "Ben Moore",
            width = 4320,
            height = 3240,
            url = "https://unsplash.com/photos/pJILiyPdrXI",
            download_url = "https://picsum.photos/id/102/4320/3240",
            isFavourite = false,
        ),
    )

    val remotePhotos = listOf(
        PhotoDto(
            id = "102",
            author = "Ben Moore",
            width = 5320,
            height = 6240,
            url = "https://unsplash.com/photos/pJILiyPdrXI",
            download_url = "https://picsum.photos/id/102/4320/3240",
        ),
        PhotoDto(
            id = "103",
            author = "Ben Moore",
            width = 4320,
            height = 3240,
            url = "https://unsplash.com/photos/pJILiyPdrXI",
            download_url = "https://picsum.photos/id/102/4320/3240",
        ),
    )
}