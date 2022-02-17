package com.example.foodiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Spinner
import android.widget.TextView
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val foodService=FoodClient.foodService

//        foodService.getFoodName("af2bd97db6b846529d0e","I2790","json","1","1")
//            .enqueue(object: Callback<FoodList>{
//                override fun onResponse(call: Call<FoodList>, response: Response<FoodList>) {
//                    //TODO("Not yet implemented")
//                    if (response.isSuccessful.not()){
//                        //Log.e(TAG,"조회 실패")
//                        return
//                    }else{
//                        //Log.d(TAG,response.body().toString())
//                        response.body()?.let {
//                            val foodName=it.list.food[0].foodName
//                        }
//                    }
//                }
//
//                override fun onFailure(call: Call<FoodList>, t: Throwable) {
//                    //TODO("Not yet implemented")
//                    Log.e(TAG,"연결 실패 ㅠ")
//                    Log.e(TAG,t.toString())
//                }
//
//            })
    }

    companion object{
        private const val TAG="MainActivity"
    }
}