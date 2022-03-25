package com.example.foodiary

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foodiary.databinding.FoodItemBinding

class SearchResultAdapter(val context: Context) : RecyclerView.Adapter<SearchResultAdapter.Holder>() {
    var list=ArrayList<FoodItemInList>()
    interface ItemClick{ //recyclerview내의 아이템 클릭 이벤트
        fun onClick(view: View, position: Int, list: ArrayList<FoodItemInList>)
    }
    var itemClick: ItemClick?=null
    private lateinit var binding: FoodItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater=LayoutInflater.from(context)
        binding= DataBindingUtil.inflate(inflater,R.layout.food_item,parent,false)
        return Holder(binding.root)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onBind(list[position])

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
        fun onBind(data: FoodItemInList){
            binding.food=data
        }
    }

    fun setData(newdata:ArrayList<FoodItemInList>){
        list=newdata
    }

}