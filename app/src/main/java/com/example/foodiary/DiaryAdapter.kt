package com.example.foodiary

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DiaryAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var list=ArrayList<DiaryItemInList>()
    val MEAL: Int=0
    val SNACK: Int=1

    interface ItemClick{
        fun onClick(view: View, position: Int, list: ArrayList<DiaryItemInList>)
    }
    var itemClick: ItemClick?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            MEAL->{
                val view= LayoutInflater.from(context).inflate(R.layout.mainpage_item, parent, false)
                MealHolder(view)
            }
            else -> {
                val view= LayoutInflater.from(context).inflate(R.layout.mainpage_snack_item, parent, false)
                SnackHolder(view)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].viewType == 0) MEAL else SNACK
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //holder.category.text=list[position].category
        //뷰타입 지정할 필요?! 있다..
        //MealHolder cannot be cast to SnackHolder의 원인은
        //getItemViewType를 오버라이딩 하지 않아서 발생했던것
        //근데 왜 snack view만 나오는지??
        when(getItemViewType(position)){
            MEAL->{
                Log.e("DiaryAdapter",getItemViewType(position).toString())
                (holder as MealHolder).name.text=list[position].name
                holder.calorie.text=list[position].calorie

                if (itemClick!=null){
                    (holder).view.setOnClickListener{ v->
                        itemClick?.onClick(v, position, list)
                    }
                }
            }
            else->{
                Log.e("DiaryAdapter",getItemViewType(position).toString())
                (holder as SnackHolder).name.text=list[position].name
                (holder).calorie.text=list[position].calorie

                if (itemClick!=null){
                    (holder).view.setOnClickListener{ v->
                        itemClick?.onClick(v, position, list)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun add(d: DiaryItemInList){
        list.add(d)
    }

    fun addAll(d: ArrayList<DiaryItemInList>){
        list.addAll(d)
    }

    inner class MealHolder(val view: View): RecyclerView.ViewHolder(view){
        val name: TextView =view.findViewById(R.id.main_food_name)
        val calorie: TextView =view.findViewById(R.id.main_food_calorie)
    }
    inner class SnackHolder(val view: View): RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.main_snack_name)
        val calorie: TextView = view.findViewById(R.id.main_snack_calorie)
    }
}