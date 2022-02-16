package com.example.foodiary

import com.google.gson.annotations.SerializedName

data class FoodDto(
    @SerializedName("row") val food:List<FoodItem>
)
