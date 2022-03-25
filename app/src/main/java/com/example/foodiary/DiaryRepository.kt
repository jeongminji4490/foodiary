package com.example.foodiary

import android.app.Application
import androidx.lifecycle.LiveData

class DiaryRepository(application: Application) {
    private val dao= DiaryDatabase.getInstance(application)!!.DiaryDao()

    fun getMorningAll(): LiveData<List<morningDiary>> {
        return dao.getMoringAll()
    }

    fun getLunchAll(): LiveData<List<lunchDiary>>{
        return dao.getLunchAll()
    }

    fun getDinnerAll(): LiveData<List<dinnerDiary>>{
        return dao.getDinnerAll()
    }

    fun getDateAll(): LiveData<List<date>>
    {
        return dao.getDates()
    }

    suspend fun dateInsert(date: date){
        dao.dateInsert(date)
    }

    suspend fun dateDelete(date: String){
        dao.deleteDate(date)
    }

    suspend fun AllDateDelete(date: String){
        dao.deleteMdate(date)
        dao.deleteLdate(date)
        dao.deleteDdate(date)
    }

    suspend fun morningInsert(diary: morningDiary){
        dao.morningInsert(diary)
    }

    suspend fun lunchInsert(diary: lunchDiary){
        dao.lunchInsert(diary)
    }

    suspend fun dinnerInsert(diary: dinnerDiary){
        dao.dinnerInsert(diary)
    }

    suspend fun morningDelete(serialNum: Int){
        dao.deleteMorning(serialNum)
    }

    suspend fun lunchDelete(serialNum: Int){
        dao.deleteLunch(serialNum)
    }

    suspend fun dinnerDelete(serialNum: Int){
        dao.deleteDinner(serialNum)
    }

    companion object{
        const val TAG = "DiaryRepository"
    }

}