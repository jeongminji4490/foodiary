package com.example.foodiary

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DiaryDao {

    //Room이 이미 MutableLiveData를 반환하는 특정 @Query에 대한 백그라운드 스레드를 사용하기 때문에 MutableLiveData를 리턴하는 메소드에는 따로 suspend를 추가하지 않아도 됨
    @Query("Select * From morning")
    fun getMoringAll(): LiveData<List<morningDiary>>

    @Query("Select * From lunch")
    fun getLunchAll(): LiveData<List<lunchDiary>>

    @Query("Select * From dinner")
    fun getDinnerAll(): LiveData<List<dinnerDiary>>

    @Query("SELECT COUNT(serialNum) FROM morning")
    fun getMorningCount(): Int

    @Insert
    suspend fun morningInsert(d: morningDiary)

    @Insert
    suspend fun lunchInsert(d: lunchDiary)

    @Insert
    suspend fun dinnerInsert(d: dinnerDiary)

    @Query("DELETE FROM morning WHERE serialNum = :serialNum")
    suspend fun deleteMorning(serialNum: Int)

    @Query("DELETE FROM lunch WHERE serialNum = :serialNum")
    suspend fun deleteLunch(serialNum: Int)

    @Query("DELETE FROM dinner WHERE serialNum = :serialNum")
    suspend fun deleteDinner(serialNum: Int)

}