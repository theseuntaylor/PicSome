package com.application.storelabs.lib_home.di

import com.application.storelabs.lib_home.data.PhotosRepository
import com.application.storelabs.lib_home.data.PhotosRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface HomeModule {

    @get:Binds
    val PhotosRepositoryImpl.photosRepository: PhotosRepository

}