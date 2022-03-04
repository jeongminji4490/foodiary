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

    fun getLunchAll(): LiveData<List<lunchDiary>>{
        return repository.getLunchAll()
    }

    fun getDinnerAll(): LiveData<List<dinnerDiary>>{
        return repository.getDinnerAll()
    }

    fun getDateAll(): LiveData<List<date>>{
        return repository.getDateAll()
    }

    suspend fun dateInsert(date: date){
        repository.dateInsert(date)
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

    suspend fun lunchDelete(serialNum: Int){
        repository.lunchDelete(serialNum)
    }

    suspend fun dinnerDelete(serialNum: Int){
        repository.dinnerDelete(serialNum)
    }
}