package com.alexlepadatu.trendingrepos.data.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object GitService {
    private const val BASE_URL = "https://github-trending-api.now.sh/"

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(provideGsonConverterFactory())
            .addCallAdapterFactory(provideCallAdapterFactory())
            .client(provideHttpClient())
            .build()
    }

    private fun provideGsonConverterFactory() = GsonConverterFactory.create(provideGson())

    private fun provideCallAdapterFactory() = RxJava2CallAdapterFactory.create()

    private fun provideGson(): Gson = GsonBuilder()
        .create()

    private fun provideHttpClient() = provideHttpClientBuilder().build()

    private fun provideHttpClientBuilder(): OkHttpClient.Builder {
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.connectTimeout(30, TimeUnit.SECONDS)
        httpClientBuilder.readTimeout(30, TimeUnit.SECONDS)
        httpClientBuilder.writeTimeout(30, TimeUnit.SECONDS)

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClientBuilder.addInterceptor(loggingInterceptor)

        return httpClientBuilder
    }
}