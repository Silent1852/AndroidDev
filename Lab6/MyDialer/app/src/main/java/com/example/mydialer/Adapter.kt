package com.example.mydialer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class Adapter(private val context: Context,
              private val list: ArrayList<Contact>,
              private val json: String,
              private val mainActivity: MainActivity) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val textName: TextView = view.findViewById(R.id.textName)
        val textPhone: TextView = view.findViewById(R.id.textPhone)
        val textType: TextView = view.findViewById(R.id.textType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rview_item,parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textName.text = list[position].name
        holder.textPhone.text = list[position].phone
        holder.textType.text = list[position].type

        val btn: Button = mainActivity.findViewById(R.id.btn_search)
        val txt: EditText = mainActivity.findViewById(R.id.et_search)
        btn.setOnClickListener {
            val copy = arrayListOf<Contact>()
            for(i in 0 until list.size){
                copy.add(Contact(list[i].name, list[i].phone, list[i].type))
            }

            Timber.d("before " + list.size.toString())
            var myText = txt.text.toString()

            list.filter { myText !in it.name }
                .forEach { list.remove(it) }
            this.notifyDataSetChanged()

            Timber.d("after " + list.size.toString())

        }
    }
}