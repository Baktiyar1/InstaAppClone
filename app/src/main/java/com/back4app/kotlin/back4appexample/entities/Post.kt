package com.back4app.kotlin.back4appexample.entities

data class Post(
    var id: String? = null,
    var title: String? = null,
    var description: String? = null,
    var image: Image? = null,
    var location: String? = null,
)