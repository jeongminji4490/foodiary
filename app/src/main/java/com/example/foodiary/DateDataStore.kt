package com.example.foodiary

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.util.concurrent.Flow

class DateDataStore(val context: Context) {
    private val Context.dataStore by preferencesDataStore(name="dateStore")

    private val key= stringPreferencesKey("dateKey")

    //값 읽어오기
    //map을 이용해 DataStore에 저장되어 있는 값을 키를 통해 가져옴, 이때 반환되는 값은 Flow
    val date: kotlinx.coroutines.flow.Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException){
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }.map { preferences ->
            preferences[key] ?: ""
        }

    //키를 이용해 값 저장하기
    //DataStore의 값을 쓸 때는 edit을 사용하고, 비동기로 작업해야함
    suspend fun setDate(date : String){
        context.dataStore.edit { preferences ->
            preferences[key]=date
        }
    }

}