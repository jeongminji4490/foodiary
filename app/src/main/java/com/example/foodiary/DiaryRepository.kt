package com.example.foodiary

import android.app.Application
import androidx.lifecycle.MutableLiveData

class DiaryRepository(application: Application) {
    private val db=DiaryDatabase.getInstance(application)!! //메인쓰레드에서 접근 불가
    private val dao: DiaryDao=db.DiaryDao()

    //private val diaries: List<Diary> = dao.getAll()

    suspend fun getMorningAll(): MutableLiveData<List<morningDiary>>{
        return dao.getMoringAll()
    }

    suspend fun getLunchAll(): List<lunchDiary>{
        return dao.getLunchAll()
    }

    suspend fun getDinnerAll(): List<dinnerDiary>{
        return dao.getDinnerAll()
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