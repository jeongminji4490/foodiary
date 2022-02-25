package com.example.foodiary

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DiaryAdapter(val context: Context) : RecyclerView.Adapter<DiaryAdapter.Holder>() {
    var list=ArrayList<DiaryItemInList>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view= LayoutInflater.from(context).inflate(R.layout.mainpage_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.category.text=list[position].category
        holder.name.text=list[position].name
        holder.calorie.text=list[position].calorie
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class Holder(val view: View): RecyclerView.ViewHolder(view){
        val category: TextView= view.findViewById(R.id.main_category)
        val name: TextView =view.findViewById(R.id.main_food_name)
        val calorie: TextView =view.findViewById(R.id.main_food_calorie)
    }
}