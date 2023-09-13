package com.theseuntaylor.picsomeapp.di

import android.content.Context
import androidx.room.Room
import com.theseuntaylor.picsomeapp.lib_home.local.PhotosDao
import com.theseuntaylor.picsomeapp.lib_home.local.PhotosDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): PhotosDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            PhotosDatabase::class.java,
            "photos.db"
        ).build()
    }

    @Provides
    fun provideTaskDao(database: PhotosDatabase): PhotosDao = database.photosDao()
}