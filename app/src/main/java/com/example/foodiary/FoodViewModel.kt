package com.example.foodiary

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

object FoodViewModel: ViewModel() { //이거 싱글톤
    val liveData= MutableLiveData<ArrayList<FoodItemInList>>()
    private val data= arrayListOf<FoodItemInList>()

    fun addItem(food: FoodItemInList){
        data.add(food)
        liveData.value=data
    }
}