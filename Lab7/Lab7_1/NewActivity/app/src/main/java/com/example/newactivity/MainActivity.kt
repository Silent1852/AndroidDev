package com.example.newactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn: Button = this.findViewById(R.id.btn_show_pic)

        val intent = Intent(this, PicActivity::class.java)
        btn.setOnClickListener{
            intent.putExtra("picLink", "https://farm66.staticflickr.com/65535/52509963605_68e37e67e4_z.jpg")
            startActivity(intent)
        }
    }
}