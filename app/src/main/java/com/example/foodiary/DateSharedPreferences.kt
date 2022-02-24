package com.example.foodiary

import android.content.Context
import android.content.SharedPreferences

// 선택된 날짜를 저장할 SharedPreferences 클래스
class DateSharedPreferences(context: Context) {
    private val fileName="datePrefs"
    private val fileKey="myDatePrefs"
    private val prefs: SharedPreferences=context.getSharedPreferences(fileName,0)

    //key를 이용한 저장
    fun set(key: String, value: String?){
        prefs.edit().putString(key, value).apply()
    }

    //key를 이용한 값 load
    fun get(key: String): String?{
        return prefs.getString(key,"")
    }
}