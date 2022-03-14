package com.example.foodiary

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodService {
    @GET("api/af2bd97db6b846529d0e/I2790/json/1/1000/RESEARCH_YEAR=\"2019\"&GROUP_NAME=\"\"&MAKER_NAME=\"\"&SAMPLING_REGION_NAME=\"전국(대표)\"&SAMPLING_MONTH_CD=\"AVG\"&SAMPLING_MONTH_NAME=\"평균\"")
    fun getFoodName( //파라미터
        @Query("keyId") keyId: String,
        @Query("serviceId") serviceId: String,
        @Query("dataType") json: String,
    ):Call<FoodList> //FoodDto 반환
}