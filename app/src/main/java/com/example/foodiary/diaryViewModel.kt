package com.example.foodiary

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class diaryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository= DiaryRepository(application)
    //private val diaries=repository.getAll()

    suspend fun getMorningAll(): List<morningDiary>{
        return repository.getMorningAll()
    }

    suspend fun getLunchAll(): List<lunchDiary>{
        return repository.getLunchAll()
    }

    suspend fun getDinnerAll(): List<dinnerDiary>{
        return repository.getDinnerAll()
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
}