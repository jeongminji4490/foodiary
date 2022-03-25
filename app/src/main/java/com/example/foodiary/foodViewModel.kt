package com.example.foodiary

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class foodViewModel: ViewModel() {
    val liveData= MutableLiveData<ArrayList<FoodItemInList>>()
    private val data= arrayListOf<FoodItemInList>()

    fun addItem(food: FoodItemInList){
        data.add(food)
        liveData.value=data
    }
}