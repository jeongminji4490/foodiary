package com.example.foodiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        val gson=GsonBuilder().setLenient().create()

        val okHttpClient=OkHttpClient.Builder()
            .connectTimeout(120,TimeUnit.SECONDS)
            .readTimeout(120,TimeUnit.SECONDS)
            .writeTimeout(120,TimeUnit.SECONDS)
            .build()

        val retrofit=Retrofit.Builder()
            .baseUrl("https://openapi.foodsafetykorea.go.kr/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        val foodService=retrofit.create(FoodService::class.java) //retrofit 객체 생성

        foodService.getFoodName("af2bd97db6b846529d0e","I2790","json","1","1")
            .enqueue(object: Callback<FoodList>{
                override fun onResponse(call: Call<FoodList>, response: Response<FoodList>) {
                    //TODO("Not yet implemented")
                    if (response.isSuccessful.not()){
                        //Log.e(TAG,"조회 실패")
                        return
                    }else{
                        Log.d(TAG,response.body().toString())
                    }
                }

                override fun onFailure(call: Call<FoodList>, t: Throwable) {
                    //TODO("Not yet implemented")
                    Log.e(TAG,"연결 실패 ㅠ")
                    Log.e(TAG,t.toString())
                }

            })
    }

    companion object{
        private const val TAG="MainActivity"
    }
}