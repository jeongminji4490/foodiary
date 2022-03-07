package com.example.foodiary

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foodiary.databinding.MainpageItemBinding
import com.example.foodiary.databinding.MainpageSnackItemBinding

class DiaryAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var list=ArrayList<DiaryItemInList>()
    val MEAL: Int=0
    val SNACK: Int=1
    private lateinit var mealBinding: MainpageItemBinding
    private lateinit var snackBinding: MainpageSnackItemBinding

    interface ItemClick{
        fun onClick(view: View, position: Int, list: ArrayList<DiaryItemInList>)
    }
    var itemClick: ItemClick?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater=LayoutInflater.from(context)
        return when(viewType){
            MEAL->{
                mealBinding= DataBindingUtil.inflate(inflater,R.layout.mainpage_item,parent,false)
                val view=mealBinding.root
                MealHolder(view)
            }
            else -> {
                snackBinding= DataBindingUtil.inflate(inflater,R.layout.mainpage_snack_item,parent,false)
                val view=snackBinding.root
                SnackHolder(view)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].viewType == 0) MEAL else SNACK
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //MealHolder cannot be cast to SnackHolder의 원인은
        //getItemViewType를 오버라이딩 하지 않아서 발생했던것
        when(getItemViewType(position)){
            MEAL->{
                (holder as MealHolder).onBind(list[position])
                if (itemClick!=null){
                    (holder).view.setOnClickListener{ v->
                        itemClick?.onClick(v, position, list)
                    }
                }
            }
            else->{
                (holder as SnackHolder).onBind(list[position])
                if (itemClick!=null){
                    (holder).view.setOnClickListener{ v->
                        itemClick?.onClick(v, position, list)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int { return list.size }

    fun addAll(d: ArrayList<DiaryItemInList>){ list.addAll(d) }

    inner class MealHolder(val view: View): RecyclerView.ViewHolder(view){
        fun onBind(data: DiaryItemInList){
            mealBinding.diary=data
        }
    }
    inner class SnackHolder(val view: View): RecyclerView.ViewHolder(view) {
        fun onBind(data: DiaryItemInList){
            snackBinding.diary=data
        }
    }
}

class DateAdapter(val context: Context): BaseAdapter() {
    val list=ArrayList<String>()

    override fun getCount(): Int { return list.size }

    override fun getItem(p0: Int): Any { return list[p0] }

    override fun getItemId(p0: Int): Long { return p0.toLong() }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val inflater=LayoutInflater.from(context)
        val view=inflater.inflate(R.layout.item_in_gridview,p2,false)
        val date: TextView=view.findViewById(R.id.dateText)
        date.text=list[p0]
        return view
    }

    fun add(date: String){ list.add(date) }

    fun addAll(data: List<String>){ list.addAll(data) }
}

class ListAdapter(val context: Context): BaseAdapter(){
    val list=ArrayList<FoodItemInList>()
    override fun getCount(): Int { return list.size }

    override fun getItem(p0: Int): Any { return list[p0] }

    override fun getItemId(p0: Int): Long { return p0.toLong()}

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val inflater=LayoutInflater.from(context)
        val view=inflater.inflate(R.layout.food_item,p2,false)
        val name: TextView=view.findViewById(R.id.food_name)
        val calorie: TextView=view.findViewById(R.id.food_calorie)
        name.text=list[p0].name
        calorie.text=list[p0].calorie
        return view
    }

    fun addAll(data: ArrayList<FoodItemInList>){ list.addAll(data) }

    fun removeAll(){
        list.clear()
    }
}