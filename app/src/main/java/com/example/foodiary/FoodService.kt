package com.example.foodiary

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodService {
    @GET("api/af2bd97db6b846529d0e/I2790/json/1/1")
    fun getFoodName(
        @Query("keyId") keyId: String, //인증키
        @Query("serviceId") serviceId: String,
        @Query("dataType") json: String,
        @Query("startIdx") startIdx: String,
        @Query("endIdx") endIdx: String
    ):Call<FoodList> //FoodDto 반환
}