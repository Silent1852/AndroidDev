package com.example.gson

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.plant(Timber.DebugTree())

        val dataURL = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=ff49fcd4d4a08aa6aafb6ea3de826464&tags=cat&format=json&nojsoncallback=1"
        val wrapped : Wrapper =
            Gson().fromJson(dataURL,Wrapper::class.java)

        val firstPage: PhotoPage =
            Gson().fromJson(wrapped.photo, PhotoPage::class.java)

        val photos =
            Gson().fromJson(firstPage.photo, Array<Photo>::class.java).toList()

        println(photos)
    }
}

data class Photo(
    val id: Long,
    val owner: String,
    val secret: String,
    val server: Int,
    val farm:  Int,
    val title: String,
    val isPublic: Int,
    val isFriend: Int,
    val isFamily: Int
)

data class PhotoPage(
    val page: Int,
    val pages: Int,
    val perPage: Int,
    val photo: JsonArray
)

data class Wrapper(
    val photo: JsonObject,
    val stat: String = "ok"
)