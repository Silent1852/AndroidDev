package com.example.newactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide

class PicActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pic_layout)

        setTitle("Картинка")
        val imageView = findViewById<ImageView>(R.id.picView)
        val pic = intent.getStringExtra("picLink")
        Log.d("", pic.toString())
        Glide.with(this).load(pic).into(imageView)
    }
}