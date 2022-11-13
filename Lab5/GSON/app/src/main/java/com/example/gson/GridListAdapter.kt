package com.example.gson

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class GridListAdapter(private val itemList: List<Drawable>, private val listLinks: List<String>) : RecyclerView.Adapter<GridListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.rview_item, null)
        return ViewHolder(layoutView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTopic.setImageDrawable(itemList[position])
    }

    override fun getItemCount() = itemList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var tvTopic: ImageView

        init {
            itemView.setOnClickListener(this)
            tvTopic = itemView.findViewById<View>(R.id.imageView) as ImageView
        }

        override fun onClick(view: View) {
            Toast.makeText(view.context,
                "copied", Toast.LENGTH_SHORT)
                .show()

            Timber.i(listLinks[adapterPosition])
        }
    }
}