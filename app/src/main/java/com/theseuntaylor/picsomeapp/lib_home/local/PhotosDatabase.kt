package com.theseuntaylor.picsomeapp.lib_home.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [PhotoEntity::class], version = 2, exportSchema = true)
abstract class PhotosDatabase: RoomDatabase() {
    abstract fun photosDao(): PhotosDao

    companion object {
        /** Adds paging columns. Version 1 only ever cached Picsum's first page, so page = 1 is accurate. */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE photos ADD COLUMN page INTEGER NOT NULL DEFAULT 1")
                db.execSQL("ALTER TABLE photos ADD COLUMN position INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}
