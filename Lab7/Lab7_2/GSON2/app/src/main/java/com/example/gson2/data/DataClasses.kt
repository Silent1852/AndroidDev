package com.example.gson2.data

import com.google.gson.JsonArray
import com.google.gson.JsonObject

data class Photo(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm:  Int,
    val title: String,
    val ispublic: Int,
    val isfriend: Int,
    val isfamily: Int
)

data class PhotoPage(
    val page: Int,
    val pages: Int,
    val perPage: Int,
    val photo: JsonArray
)

data class Wrapper(
    val photos: JsonObject,
    val stat: String = "ok"
)

data class ImageData(val imageUrl: String)