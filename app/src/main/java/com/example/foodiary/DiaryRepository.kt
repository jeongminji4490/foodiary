package com.example.foodiary

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class DiaryRepository(application: Application) {
    private val db=DiaryDatabase.getInstance(application)!! //메인쓰레드에서 접근 불가
    private val dao: DiaryDao=db.DiaryDao()

    //private val diaries: List<Diary> = dao.getAll()

    fun getMorningAll(): LiveData<List<morningDiary>> {
        return dao.getMoringAll()
    }

    fun getLunchAll(): List<lunchDiary>{
        return dao.getLunchAll()
    }

    fun getDinnerAll(): List<dinnerDiary>{
        return dao.getDinnerAll()
    }

    fun getMorningCount(): Int{
        return dao.getMorningCount()
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
}