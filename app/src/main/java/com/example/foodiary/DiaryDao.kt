package com.example.foodiary

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.domain.date
import com.example.domain.dinnerDiary
import com.example.domain.lunchDiary
import com.example.domain.morningDiary

@Dao
interface DiaryDao {

    /**다이어리 DB에 대한 인터페이스**/
    //Room이 이미 MutableLiveData를 반환하는 특정 @Query에 대한 백그라운드 스레드를 사용하기 때문에 MutableLiveData를 리턴하는 메소드에는 따로 suspend를 추가하지 않아도 됨
    @Query("Select * From morning")
    fun getMoringAll(): LiveData<List<morningDiary>>

    @Query("Select * From lunch")
    fun getLunchAll(): LiveData<List<lunchDiary>>

    @Query("Select * From dinner")
    fun getDinnerAll(): LiveData<List<dinnerDiary>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun dateInsert(d: date)

    @Query("SELECT * FROM date")
    fun getDates(): LiveData<List<date>>

    @Query("DELETE FROM date WHERE date = :date")
    suspend fun deleteDate(date: String)

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

    @Query("DELETE FROM morning WHERE date = :date")
    suspend fun deleteMdate(date: String)

    @Query("DELETE FROM lunch WHERE date = :date")
    suspend fun deleteLdate(date: String)

    @Query("DELETE FROM dinner WHERE date = :date")
    suspend fun deleteDdate(date: String)
}