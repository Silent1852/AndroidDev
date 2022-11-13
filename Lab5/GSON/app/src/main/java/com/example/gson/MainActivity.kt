package com.example.gson

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import okhttp3.*
import timber.log.Timber
import java.io.IOException
import java.io.InputStream
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.plant(Timber.DebugTree())

        Thread{
            getRequest(this)
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

fun getRequest(mainActivity: MainActivity) {
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

        val listPNG = arrayListOf<Drawable>()
        val listLinks = arrayListOf<String>()
        fillLists(listPNG, listLinks, wrapper)

        Timber.d("size: " + listPNG.size.toString())

        val recycler_view: RecyclerView = mainActivity.findViewById(R.id.rView)
        val gridLayoutManager = GridLayoutManager(mainActivity,2)
        val gridListAdapter = GridListAdapter(listPNG, listLinks)
        recycler_view.setHasFixedSize(true)

        mainActivity.runOnUiThread(java.lang.Runnable {
            recycler_view.layoutManager = gridLayoutManager
            recycler_view.adapter = gridListAdapter
        })
    }
}

fun fillLists(listPNG: ArrayList<Drawable>, listLinks: ArrayList<String>, wrapper: Wrapper) {
    var limit: Int = 7

    for (i in 0..limit) {
        try {
            var urlString =
                "https://farm${wrapper.photos!!.photo[i].farm}.staticflickr.com/" +
                        "${wrapper.photos!!.photo[i].server}/" +
                        "${wrapper.photos!!.photo[i].id}_" +
                        "${wrapper.photos!!.photo[i].secret}_z.jpg"

            var url = URL(urlString).content as InputStream
            var d = Drawable.createFromStream(url, "src name")

            listPNG.add(d)
            listLinks.add(urlString)
        } catch (ex: java.lang.Exception) {
            Timber.d("ERROR: " + "https://farm${wrapper.photos!!.photo[i].farm}.staticflickr.com/" +
                    "${wrapper.photos!!.photo[i].server}/" +
                    "${wrapper.photos!!.photo[i].id}_" +
                    "${wrapper.photos!!.photo[i].secret}_z.jpg")
            limit++
            continue
        }
    }
}

