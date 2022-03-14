package com.example.foodiary

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**다이어리 DB의 모든 테이블**/
@Entity(tableName = "morning") //아침식단 테이블
data class morningDiary(
    @PrimaryKey(autoGenerate = true)
    val serialNum: Int,
    val date: String, //2000/00/00
    val category: String,
    val food_name: String,
    val food_calorie: String
)

@Entity(tableName = "lunch") //점심식단 테이블
data class lunchDiary(
    @PrimaryKey(autoGenerate = true)
    val serialNum: Int,
    val date: String, //2000/00/00
    val category: String,
    val food_name: String,
    val food_calorie: String
)

@Entity(tableName = "dinner") //저녁식단 테이블
data class dinnerDiary(
    @PrimaryKey(autoGenerate = true)
    val serialNum: Int,
    val date: String, //2000/00/00
    val category: String,
    val food_name: String,
    val food_calorie: String
)

@Entity(tableName = "date", indices = arrayOf(Index(value = ["date"], unique = true))) //일정 테이블, 중복저장 X
data class date(
    @PrimaryKey(autoGenerate = true)
    val serialNum: Int,
    val date: String
)

//다이어리에 대한 데이터 클래스
data class DiaryItemInList(val serialNum: Int, val category: String, val name: String, val calorie: String, val viewType: Int)
