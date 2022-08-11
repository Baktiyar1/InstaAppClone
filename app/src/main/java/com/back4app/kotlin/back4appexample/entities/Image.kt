package com.back4app.kotlin.back4appexample.entities

import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("__type")
    var type: String = "File",
    var name: String? = null,
    var url: String? = null,
)