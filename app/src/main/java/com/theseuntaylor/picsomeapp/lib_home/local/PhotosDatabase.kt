package com.theseuntaylor.picsomeapp.lib_home.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PhotoEntity::class], version = 1, exportSchema = false)
abstract class PhotosDatabase: RoomDatabase() {
    abstract fun photosDao(): PhotosDao
}