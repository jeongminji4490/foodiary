package com.example.foodiary

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SearchResultAdapter(val context: Context) : RecyclerView.Adapter<SearchResultAdapter.Holder>() {
    var list=ArrayList<FoodItemInList>()
    interface ItemClick{
        fun onClick(view: View, position: Int, list: ArrayList<FoodItemInList>)
    }
    var itemClick: ItemClick?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view= LayoutInflater.from(context).inflate(R.layout.food_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) { //item binding
        holder.name.text=list[position].name
        holder.calorie.text=list[position].calorie

        if (itemClick!=null){
            holder.view.setOnClickListener{ v->
                itemClick?.onClick(v, position, list)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getName(pos: Int): String{
        return list[pos].name
    }

    fun getCalorie(pos: Int): String{
        return list[pos].calorie
    }

    fun add(item: FoodItemInList){
        list.add(item)
    }

    inner class Holder(val view: View) : RecyclerView.ViewHolder(view){
        val name: TextView=view.findViewById(R.id.food_name)
        val calorie: TextView=view.findViewById(R.id.food_calorie)
    }

    fun setData(newdata:ArrayList<FoodItemInList>){
        list=newdata
        //notifyDataSetChanged() //이거 꼭 해야되냐
    }

}