package com.example.foodiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    //by lazy : 각 변수가 처음 사용되는 시점에서 지연 초기화
    private val TodayDietFragment by lazy { TodayDiet() }
    private val EmptyFragment by lazy { EmptyPage() }
    private val naviView: BottomNavigationView by lazy {
       findViewById(R.id.main_underbar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val foodService=FoodClient.foodService
        initNavigationBar()

//        foodService.getFoodName("af2bd97db6b846529d0e","I2790","json","1","1")
//            .enqueue(object: Callback<FoodList>{
//                override fun onResponse(call: Call<FoodList>, response: Response<FoodList>) {
//                    //TODO("Not yet implemented")
//                    if (response.isSuccessful.not()){
//                        //Log.e(TAG,"조회 실패")
//                        return
//                    }else{
//                        //Log.d(TAG,response.body().toString())
//                        response.body()?.let {
//                            val foodName=it.list.food[0].foodName
//                        }
//                    }
//                }
//
//                override fun onFailure(call: Call<FoodList>, t: Throwable) {
//                    //TODO("Not yet implemented")
//                    Log.e(TAG,"연결 실패 ㅠ")
//                    Log.e(TAG,t.toString())
//                }
//
//            })
    }

    private fun initNavigationBar(){
        naviView.itemIconTintList=null
        naviView.run {
            setOnItemSelectedListener {
                when(it.itemId){
                    R.id.navi_todayBtn->{
                        changeFragment(TodayDietFragment)
                    }
                    R.id.navi_allBtn->{
                        changeFragment(EmptyFragment)
                    }
                }
                true
            }
            selectedItemId=R.id.navi_todayBtn //프래그먼트 초기화면 셋팅
        }
    }

    private fun changeFragment(fragment: Fragment){
        Log.e(TAG,"교체")
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout,fragment)
            .commit()
    }

    companion object{
        private const val TAG="MainActivity"
    }
}