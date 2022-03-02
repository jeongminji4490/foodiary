package com.example.foodiary

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object FoodClient { //Singleton
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

    val foodService=retrofit.create(FoodService::class.java) //retrofit 객체 생성
}