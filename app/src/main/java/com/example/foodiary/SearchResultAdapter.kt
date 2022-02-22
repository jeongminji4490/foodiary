package com.example.foodiary

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SearchResultAdapter(val context: Context) : RecyclerView.Adapter<SearchResultAdapter.Holder>() {
    var list=ArrayList<FoodItemInList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view= LayoutInflater.from(context).inflate(R.layout.food_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) { //item binding
        holder.name.text=list[position].name
        holder.calorie.text=list[position].calorie
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addAll(item: ArrayList<FoodItemInList>){
        list.addAll(item)
    }

    inner class Holder(val view: View) : RecyclerView.ViewHolder(view){
        val name: TextView=view.findViewById(R.id.food_name)
        val calorie: TextView=view.findViewById(R.id.food_calorie)
    }

    fun setData(newdata:ArrayList<FoodItemInList>){
        list=newdata
        notifyDataSetChanged() //이거 꼭 해야되냐
    }
}