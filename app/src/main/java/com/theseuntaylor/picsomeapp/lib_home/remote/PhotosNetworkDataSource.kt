package com.theseuntaylor.picsomeapp.lib_home.remote

import com.theseuntaylor.picsomeapp.lib_home.remote.model.PhotoDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotosNetworkDataSource {
    @GET("v2/list")
    /** One page of Picsum's catalogue; an empty list means there are no more pages. */
    suspend fun getPhotos(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = PAGE_SIZE,
    ): List<PhotoDto>

    companion object {
        const val PAGE_SIZE = 100
    }
}