package com.example.mydialer_lab6

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
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

        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        val txt = sharedPref.getString("SEARCH_FILTER", "UNDEFINED VALUE")

        findViewById<EditText>(R.id.et_search).setText(txt)

        Timber.plant(Timber.DebugTree())
        getRequest(this)
    }
}

fun getRequest(mainActivity: MainActivity) {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("https://drive.google.com/u/0/uc?id=1-KO-9GA3NzSgIc1dkAsNm8Dqw0fuPxcR&export=download")
        .build()

    client.newCall(request).enqueue(object : Callback {

        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val jsonText = response?.body?.string()
                val gsBuilder = GsonBuilder()
                val gs = gsBuilder.create()

                var list = gs.fromJson(jsonText, Array<Contact>::class.java).toList() as ArrayList<Contact>
                var copy1 = list.toList() as ArrayList<Contact>

                val recyclerView: RecyclerView = mainActivity.findViewById(R.id.rView)
                val adapter = ListAdapter(mainActivity, list, jsonText.toString(), mainActivity)
                mainActivity.runOnUiThread(java.lang.Runnable {
                    recyclerView.layoutManager = LinearLayoutManager(mainActivity)
                    recyclerView.adapter = adapter
                })

                val editText: EditText = mainActivity.findViewById(R.id.et_search)
                editText.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable) {

                        val sharedPref = mainActivity.getPreferences(Context.MODE_PRIVATE)
                        with (sharedPref.edit()) {
                            putString("SEARCH_FILTER", s.toString())
                            apply()
                        }
                    }
                    override fun beforeTextChanged(s: CharSequence, start: Int,
                                                   count: Int, after: Int) {}
                    override fun onTextChanged(txt: CharSequence, start: Int,
                                               before: Int, count: Int) {
                        var myText = txt.toString()

                        list.clear()
                        list.addAll(copy1)

                        list.filter { myText !in it.name }
                            .forEach { list.remove(it) }
                        adapter.notifyDataSetChanged()
                    }
                })
                Timber.d(gs.toJson(list))
            }
        }
    })
}