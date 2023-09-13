package com.theseuntaylor.picsomeapp.di

import com.theseuntaylor.picsomeapp.lib_home.remote.PhotosNetworkDataSource
import com.theseuntaylor.picsomeapp.utils.BASE_URL
import com.theseuntaylor.picsomeapp.utils.REQUEST_TIMEOUT
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    @Named(AppConstants.MAIN_DISPATCHER)
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Singleton
    @Provides
    @Named(AppConstants.IO_DISPATCHER)
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun providesCoroutineScope(
        @Named(AppConstants.MAIN_DISPATCHER) dispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)

    @Provides
    @Singleton
    fun providePhotosNetworkDataSource(retrofit: Retrofit): PhotosNetworkDataSource =
        retrofit.create(PhotosNetworkDataSource::class.java)

    @Provides
    @Singleton
    fun provideInterceptor() = Interceptor {
        val originalRequest = it.request()
        val request = originalRequest
            .newBuilder()
        it.proceed(request.build())
    }

    @Provides
    @Singleton
    fun provideHttpClient(interceptor: Interceptor): OkHttpClient {
        return okHttpClient(interceptor)
    }

    private fun okHttpClient(interceptor: Interceptor): OkHttpClient {
        return builder(interceptor).build()
    }

    private fun builder(interceptor: Interceptor): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor())
            .addInterceptor(interceptor)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideRetrofit(httpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BASE_URL).client(httpClient).build()
    }
}