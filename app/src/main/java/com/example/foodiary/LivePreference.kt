package com.example.foodiary

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import java.util.*

//abstract class LivePreference<T> constructor(
//    private val preferences: DateSharedPreferences,
//    private val key: String,
//    private val defaultValue: T
//): LiveData<T>(){
//    private val preferenceChangeListener =  SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
//        if (key == this.key) {
//            value = getValueFromPreferences(key, defaultValue)
//        }
//    }
//
//    abstract fun getValueFromPreferences(key: String, defaultValue: T): T
//
//    override fun onActive() {
//        super.onActive()
//        value=getValueFromPreferences(key, defaultValue)
//        preferences.registerOn
//    }
//
//    override fun onInactive() {
//        super.onInactive()
//    }
//}