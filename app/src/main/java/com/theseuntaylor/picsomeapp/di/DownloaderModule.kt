package com.theseuntaylor.picsomeapp.di

import android.content.Context
import com.theseuntaylor.picsomeapp.core.downloader.AndroidDownloader
import com.theseuntaylor.picsomeapp.core.downloader.Downloader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DownloaderModule {

    @Provides
    @Singleton
    fun provideDownloader(@ApplicationContext context: Context): Downloader {
        return AndroidDownloader(context)
    }
}