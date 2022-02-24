package com.example.foodiary

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "morning")
data class morningDiary(
    @PrimaryKey(autoGenerate = true)
    val serialNum: Int,
    val date: String, //2000/00/00
    val category: String,
    val food_name: String,
    val food_calorie: String
)

@Entity(tableName = "lunch")
data class lunchDiary(
    @PrimaryKey(autoGenerate = true)
    val serialNum: Int,
    val date: String, //2000/00/00
    val category: String,
    val food_name: String,
    val food_calorie: String
)

@Entity(tableName = "dinner")
data class dinnerDiary(
    @PrimaryKey(autoGenerate = true)
    val serialNum: Int,
    val date: String, //2000/00/00
    val category: String,
    val food_name: String,
    val food_calorie: String
)
