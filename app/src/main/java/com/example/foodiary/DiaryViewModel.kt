package com.example.foodiary

import android.app.Application
import androidx.lifecycle.*
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Call

class DiaryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository= DiaryRepository(application)
    private val scope by lazy { viewModelScope }
    private var mList = repository.getMorningAll()
    private var lList = repository.getLunchAll()
    private var dList = repository.getDinnerAll()
    private var dateList = repository.getDateAll()

    //UI관련 데이터들은 뷰모델에서 관리
    //LiveData -> MutableLiveData로 수정(livedata는 추상클래스라 객체생성 불가)
    //상단의 리스트에 데이터들을 저장하고, 상단의 리스트들을 리턴하기!
    //insert, delete 코루틴 스코프내에서
    fun getFoodList() {
        repository.getFoodList()
    }

    fun getMorningAll(): LiveData<List<morningDiary>> = mList

    fun getLunchAll(): LiveData<List<lunchDiary>> = lList

    fun getDinnerAll(): LiveData<List<dinnerDiary>> = dList

    fun getDateAll(): LiveData<List<date>> = dateList

    fun morningInsert(diary: morningDiary) = scope.launch{
        repository.morningInsert(diary)
    }

    fun lunchInsert(diary: lunchDiary) = scope.launch{
        repository.lunchInsert(diary)
    }

    fun dinnerInsert(diary: dinnerDiary) = scope.launch {
        repository.dinnerInsert(diary)
    }

    fun dateInsert(date: date) = scope.launch{
        repository.dateInsert(date)
    }

    fun morningDelete(serialNum: Int) = scope.launch{
        repository.morningDelete(serialNum)
    }

    fun lunchDelete(serialNum: Int) = scope.launch{
        repository.lunchDelete(serialNum)
    }

    fun dinnerDelete(serialNum: Int) = scope.launch{
        repository.dinnerDelete(serialNum)
    }

    fun dateDelete(date: String) = scope.launch{
        repository.dateDelete(date)
    }

    fun deleteAll(date: String) = scope.launch{
        repository.AllDateDelete(date)
    }
}