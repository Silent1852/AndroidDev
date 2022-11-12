package com.example.gson

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.GsonBuilder
import okhttp3.*
import timber.log.Timber
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.plant(Timber.DebugTree())

        Thread{
            getRequest()
        }.start()
    }
}

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
    val photo: List<Photo> = listOf()
)

data class Wrapper(
    val photos: PhotoPage? = null,
    val stat: String = "ok"
)

fun getRequest(){
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=ff49fcd4d4a08aa6aafb6ea3de826464&tags=cat&format=json&nojsoncallback=1")
        .build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")

        val json = response.body?.string()
        val gsBuilder = GsonBuilder()
        val gs = gsBuilder.create()
        val wrapper: Wrapper = gs.fromJson(json, Wrapper::class.java)

        val sizeOfJson: Int? = wrapper.photos?.photo?.size

        for(i in 0 until sizeOfJson!!)
            if (i % 4 == 0) Timber.d(gs.toJson(wrapper.photos?.photo?.get(i)))
    }
}