package com.example.mydialer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import okhttp3.*
import timber.log.Timber
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.plant(Timber.DebugTree())

        Thread {
            getRequest(this)
        }.start()
    }
}

data class Contact(
    var name: String,
    var phone: String,
    var type: String
)

fun getRequest(mainActivity: MainActivity) {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("https://drive.google.com/u/0/uc?id=1-KO-9GA3NzSgIc1dkAsNm8Dqw0fuPxcR&export=download")
        .build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")

        val json = response.body?.string()
        val gsBuilder = GsonBuilder()
        val gs = gsBuilder.create()
        val contact: List<Contact> = gs.fromJson(json, Array<Contact>::class.java).toList()

        val recyclerView: RecyclerView = mainActivity.findViewById(R.id.rView)
        mainActivity.runOnUiThread(java.lang.Runnable {
            recyclerView.layoutManager = LinearLayoutManager(mainActivity)
            recyclerView.adapter = Adapter(mainActivity, contact as ArrayList<Contact>, json.toString(), mainActivity)
            recyclerView.addItemDecoration(DividerItemDecoration(mainActivity,RecyclerView.VERTICAL))
        })

        Timber.d(gs.toJson(contact))
    }
}