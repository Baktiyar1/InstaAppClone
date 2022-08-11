package com.back4app.kotlin.back4appexample.activities.api

import com.back4app.kotlin.back4appexample.utils.APPLICATION_ID
import com.back4app.kotlin.back4appexample.utils.BASE_URL
import com.back4app.kotlin.back4appexample.utils.CONTENT_TYPE
import com.back4app.kotlin.back4appexample.utils.REST_API_KEY
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitBuilder {

    private val requestInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .cacheControl(CacheControl.Builder().maxAge(0, TimeUnit.SECONDS).build())
            .addHeader("X-Parse-Application-Id", APPLICATION_ID)
            .addHeader("X-Parse-REST-API-Key", REST_API_KEY)
            .addHeader("Content-Type", CONTENT_TYPE).build()
        return@Interceptor chain.proceed(request)
    }

    private val httpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val client = OkHttpClient.Builder().addInterceptor(requestInterceptor)
        .addInterceptor(httpLoggingInterceptor).connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).build()

    private val retrofit: Retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .client(client).addConverterFactory(GsonConverterFactory.create()).build()

    val api: Api = retrofit.create(Api::class.java)

}