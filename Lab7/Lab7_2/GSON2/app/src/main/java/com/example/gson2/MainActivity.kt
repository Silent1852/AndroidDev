package com.example.gson2

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gson2.data.ImageData
import timber.log.Timber
import timber.log.Timber.Forest.plant

interface CellClickListener {
    fun onCellClickListener(data: ImageData)
}

class MainActivity : AppCompatActivity(), CellClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_menu)
        setSupportActionBar(toolbar)

        val recyclerView: RecyclerView? = findViewById(R.id.rView)

        recyclerView?.layoutManager = GridLayoutManager(this, 2);

        plant(Timber.DebugTree())

        val okAdapter = fun(data: ArrayList<ImageData>) {
            runOnUiThread {
                recyclerView?.adapter = GridListAdapter(this, data, this)
            }
        }

        getURLDataOk("https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=ff49fcd4d4a08aa6aafb6ea3de826464&tags=cat&format=json&nojsoncallback=1", okAdapter)
    }

    override fun onCellClickListener(data: ImageData) {
        val myClipboard: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val myClip: ClipData = ClipData.newPlainText("text", data.imageUrl)
        myClipboard.setPrimaryClip(myClip)

        Timber.i(data.imageUrl)

        this.sendMessage(data.imageUrl)
    }

    public fun sendMessage(picURL: String){
        val intent = Intent(this, PicViewer::class.java)

        intent.putExtra("picLink", picURL)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()

        if (id == R.id.action_one) {
            Toast.makeText(this, "Item One Clicked", Toast.LENGTH_LONG).show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}