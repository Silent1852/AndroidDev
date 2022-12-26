package com.example.gson3

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class PicViewer : AppCompatActivity() {
    private var msg: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pic_viewer)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val intent = intent
        msg = intent.getStringExtra("picLink")

        intent.data = Uri.parse(msg)
        setResult(RESULT_OK, intent)
        finish()


        val imageView = findViewById<ImageView>(R.id.myImage)
        Glide
            .with(this)
            .load(msg)
            .into(imageView)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()

        if (id == R.id.action_one) {

            val intent = intent
            intent.setData(Uri.parse(msg))
            setResult(RESULT_OK, intent)

            finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}