package com.example.foodiary

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiaryRepository(application: Application) {
    //private val db=DiaryDatabase.getInstance(application)!! //db 생성
    //private val dao: DiaryDao=db.DiaryDao()
    private val dao=DiaryDatabase.getInstance(application)!!.DiaryDao()
    //레트로핏과 같은 네트워크 작업 여기서?

    //레트로핏 사용
    fun bodyFromRetrofit() : FoodList? {
        var foodList: FoodList? =null
        val response = FoodClient.foodService
        response.getFoodName("af2bd97db6b846529d0e","I2790","json")
            .enqueue(object: Callback<FoodList> {
                override fun onResponse(call: Call<FoodList>, response: Response<FoodList>) {
                    if (response.isSuccessful.not()){
                        Log.e("","조회 실패")
                        return
                    }else{ //조회에 성공했다면
                        response.body()?.let {
                            foodList=it
                            Log.e(TAG, foodList.toString())
                        }
                    }
                }
                override fun onFailure(call: Call<FoodList>, t: Throwable) {
                    Log.e(TAG,"연결 실패 ㅠ")
                    Log.e(TAG,t.toString())
                    return
                }

            })
        Log.e(TAG, foodList.toString()+"2")
        return foodList
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