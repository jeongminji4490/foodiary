package com.example.foodiary

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object FoodClient { //싱글톤

    //서버 요청시간 정의
    //koin 사용하여 의존성 주입
   val okHttpClient= OkHttpClient.Builder()
        .connectTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .build()

    var gson= GsonBuilder().setLenient().create()

    val retrofit= Retrofit.Builder()
        .baseUrl("https://openapi.foodsafetykorea.go.kr/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()

    //val foodService=retrofit.create(FoodService::class.java) //retrofit 객체 생성
    val foodService : FoodService by lazy { retrofit.create(FoodService::class.java) }
}