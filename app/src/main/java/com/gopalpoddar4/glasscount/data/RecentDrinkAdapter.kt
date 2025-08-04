package com.gopalpoddar4.glasscount.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gopalpoddar4.glasscount.R

class RecentDrinkAdapter(private val list: List<RecentDrinkModel>): RecyclerView.Adapter<RecentDrinkAdapter.Viewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.last5rcv_layout,parent,false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.drinkTime.text = list[position].time
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class Viewholder(view: View): RecyclerView.ViewHolder(view){
        val drinkTime = view.findViewById<TextView>(R.id.drinkTime)
    }
}