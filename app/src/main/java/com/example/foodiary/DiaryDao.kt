package com.example.foodiary

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DiaryDao {

    @Query("Select * From morning")
    suspend fun getMoringAll(): List<morningDiary>

    @Query("Select * From lunch")
    suspend fun getLunchAll(): List<lunchDiary>

    @Query("Select * From dinner")
    suspend fun getDinnerAll(): List<dinnerDiary>

    @Insert
    suspend fun morningInsert(d: morningDiary)

    @Insert
    suspend fun lunchInsert(d: lunchDiary)

    @Insert
    suspend fun dinnerInsert(d: dinnerDiary)

    //삭제는 나중에..
}