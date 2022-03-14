package com.example.foodiary

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class DiaryRepository(application: Application) {
    private val db=DiaryDatabase.getInstance(application)!! //메인쓰레드에서 접근 불가
    private val dao: DiaryDao=db.DiaryDao()

    fun getMorningAll(): LiveData<List<morningDiary>> {
        return dao.getMoringAll()
    }

    fun getLunchAll(): LiveData<List<lunchDiary>>{
        return dao.getLunchAll()
    }

    fun getDinnerAll(): LiveData<List<dinnerDiary>>{
        return dao.getDinnerAll()
    }

    fun getDateAll(): LiveData<List<date>>{
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

}