package com.example.foodiary

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Diary(
    @PrimaryKey(autoGenerate = true)
    val serialNum: Int,
    val date: String, //2000/00/00
    val category: String,
    val time: String, //아침?점심?저녁?
    val food_name: String,
    val food_calorie: String
)
