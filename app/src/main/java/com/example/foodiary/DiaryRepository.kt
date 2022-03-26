package com.example.foodiary

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiaryRepository(application: Application) {
    private val dao= DiaryDatabase.getInstance(application)!!.DiaryDao()

    fun getFoodList() {
        Log.e(TAG,"getFoodList")
        FoodClient.foodService.getFoodName("af2bd97db6b846529d0e","I2790","json")
            .enqueue(object: Callback<FoodList> {
                override fun onResponse(call: Call<FoodList>, response: Response<FoodList>) {
                    if (response.isSuccessful.not()){
                        Log.e(TAG,"조회 실패")
                        return
                    }else{ //조회에 성공했다면
                        Log.e(TAG,"엥") //조회는 잘됨
                        response.body()?.let {
                            for (i: Int in 0..999){ //응답받은 데이터들 중 1000개의 데이터에 대해
                                val name=it.list.food[i].foodName //음식명
                                val kcal=it.list.food[i].kcal //1회 제공량당 kcal
                                foodViewModel.addItem(FoodItemInList(name, kcal)) //뷰모델에 추가
                            }
                        }
                    }
                }
                override fun onFailure(call: Call<FoodList>, t: Throwable) {
                    Log.e(TAG,"연결 실패")
                    Log.e(TAG,t.toString())
                }
            })
    }

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