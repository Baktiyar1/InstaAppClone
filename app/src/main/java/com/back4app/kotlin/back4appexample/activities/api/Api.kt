package com.back4app.kotlin.back4appexample.activities.api

import com.back4app.kotlin.back4appexample.entities.PostResponse
import com.back4app.kotlin.back4appexample.entities.AddPostRequest
import com.back4app.kotlin.back4appexample.entities.AddPostResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {

    @GET("Classes/Post")
    fun getAllPosts(): Call<PostResponse>

    @POST("Classes/Post")
    fun createPosts(@Body post: AddPostRequest): Call<AddPostResponse>

}