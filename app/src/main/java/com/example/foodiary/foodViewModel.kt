package com.example.foodiary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class foodViewModel: ViewModel() {
    val liveData= MutableLiveData<ArrayList<FoodItemInList>>()
    private val data= arrayListOf<FoodItemInList>()

    fun addItem(food: FoodItemInList){
        data.add(food)
        liveData.value=data
    }

//    fun deleteItem(position: Int){ //인덱스로 아이템 삭제
//        data.removeAt(position)
//        liveData.value=data
//    }
}