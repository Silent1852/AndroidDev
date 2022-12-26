package com.example.gson2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gson2.data.ImageData
import com.example.gson2.data.Photo
import com.example.gson2.data.PhotoPage
import com.example.gson2.data.Wrapper
import com.google.gson.Gson
import okhttp3.*
import timber.log.Timber
import java.io.IOException

class GridListAdapter(private val context: Context,
              private val list: ArrayList<ImageData>,
              private val cellClickListener : CellClickListener) : RecyclerView.Adapter<GridListAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val image : ImageView?  = view.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rview_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.image?.let { Glide.with(context).load(data.imageUrl).into(it) }

        holder.itemView.setOnClickListener{
            cellClickListener.onCellClickListener(data)
        }
    }
}

fun getURLDataOk(url : String, callback: (ArrayList<ImageData>) -> Unit) {

    val client = OkHttpClient();

    val request : Request = Request.Builder()
        .url(url)
        .build()

    client.newCall(request).enqueue(object: Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                val body = response.body?.string()
                val wrapper: Wrapper? = Gson().fromJson(body, Wrapper::class.java)
                val photoPage: PhotoPage? = Gson().fromJson(wrapper?.photos, PhotoPage::class.java)
                val photo: List<Photo> = Gson().fromJson(photoPage?.photo, Array<Photo>::class.java).toList()

                photo.forEachIndexed { index, element ->
                    run {
                        if ((index + 1) % 5 == 0)
                            Timber.d(element.toString())
                    }
                }

                val photoUrl: ArrayList<ImageData> = photo.map {  ImageData("https://farm${it.farm}.staticflickr.com/${it.server}/${it.id}_${it.secret}_z.jpg")} as ArrayList<ImageData>

                callback(photoUrl)
            }

        }
    })
}