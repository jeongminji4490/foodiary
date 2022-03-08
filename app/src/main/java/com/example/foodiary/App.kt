package com.example.foodiary

import android.app.Application

// SharedPreferences 클래스는 앱에 있는 다른 액티비티보다 먼저 생성되어야 다른 곳에 데이터를 넘겨줄 수 있음
// 따라서 Application에 해당하는 클래스를 생성한 뒤, 전역 변수로 SharedPreferences를 가지고 있어야함
// Application()을 상속받는 클래스를 생성하여, onCreate()보다 먼저 prefs를 초기화해줌
//class App: Application() {
//
//    companion object{
//        lateinit var prefs: DateSharedPreferences
//    }
//
//    override fun onCreate() {
//        prefs= DateSharedPreferences(applicationContext)
//        super.onCreate()
//    }
//}

//Application 객체를 싱글톤으로 사용
class DateApp: Application(){
    private lateinit var dataStore: DateDataStore

    companion object{
        private lateinit var dateApp: DateApp
        fun getInstance() : DateApp = dateApp
    }

    override fun onCreate() {
        super.onCreate()
        dateApp=this
        dataStore=DateDataStore(this)
    }

    fun getDataStore() : DateDataStore = dataStore
}