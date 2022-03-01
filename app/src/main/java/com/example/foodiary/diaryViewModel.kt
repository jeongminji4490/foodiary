package com.example.foodiary

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class diaryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository= DiaryRepository(application)
    //private val diaries=repository.getAll()

    fun getMorningAll(): LiveData<List<morningDiary>> {
        return repository.getMorningAll()
    }

    fun getLunchAll(): List<lunchDiary>{
        return repository.getLunchAll()
    }

    fun getDinnerAll(): List<dinnerDiary>{
        return repository.getDinnerAll()
    }

    fun getMorningCount(): Int{
        return repository.getMorningCount()
    }

    suspend fun morningInsert(diary: morningDiary){
        repository.morningInsert(diary)
    }

    suspend fun lunchInsert(diary: lunchDiary){
        repository.lunchInsert(diary)
    }

    suspend fun dinnerInsert(diary: dinnerDiary){
        repository.dinnerInsert(diary)
    }

    suspend fun morningDelete(serialNum: Int){
        repository.morningDelete(serialNum)
    }
}