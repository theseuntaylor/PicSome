package com.application.storelabs.lib_home.remote

import com.application.storelabs.lib_home.remote.model.PhotoDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotosNetworkDataSource {
    @GET("v2/list")
    suspend fun getPhotos(
        @Query("limit") limit: Int = 100
    ): List<PhotoDTO>
}