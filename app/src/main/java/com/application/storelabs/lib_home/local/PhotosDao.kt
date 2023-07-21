package com.application.storelabs.lib_home.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface PhotosDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPhotos(photoEntity: List<PhotoEntity>)

    @Query("SELECT * FROM photos")
    suspend fun getPhotos(): List<PhotoEntity>

    @Query("UPDATE photos SET isFavourite=:isFavourite WHERE id = :id")
    suspend fun toggleFavourites(id: String, isFavourite: Boolean)

    @Query("SELECT * FROM photos WHERE isFavourite=:isFavourite")
    suspend fun getFavouritePhotos(isFavourite: Boolean = true): List<PhotoEntity>

    @Query("DELETE from photos WHERE isFavourite=:isFavourite")
    suspend fun deleteUnfavourite(isFavourite: Boolean = false)

    @Transaction
    suspend fun refreshPhotos(photoEntity: List<PhotoEntity>){
        deleteUnfavourite()
        addPhotos(photoEntity = photoEntity)
    }
}