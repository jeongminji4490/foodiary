package com.example.foodiary

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodService {
    @GET("https://openapi.foodsafetykorea.go.kr/api/af2bd97db6b846529d0e/I2790/json/1/5")
    fun getFoodName(
        @Query("keyId") keyId: String, //인증키
        @Query("serviceId") serviceId: String,
        @Query("dataType") json: String,
        @Query("startIdx") startIdx: String,
        @Query("endIdx") endIdx: String
        //@Query("NUTR_CONT1") kcal: Int
    ):Call<FoodDto> //FoodDto 반환
}