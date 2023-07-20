package com.application.storelabs.lib_home.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocalPhoto::class], version = 1, exportSchema = false)
abstract class PhotosDatabase: RoomDatabase() {

    abstract fun photosDao(): PhotosDao

}