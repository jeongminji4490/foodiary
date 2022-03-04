package com.example.foodiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import nl.joery.animatedbottombar.AnimatedBottomBar

class MainActivity : AppCompatActivity() {

    //by lazy : 각 변수가 처음 사용되는 시점에서 지연 초기화
    private val TodayDietFragment by lazy { TodayDiet() }
    private val EmptyFragment by lazy { ListPage() }
    private val bottomBar: AnimatedBottomBar by lazy {
        findViewById(R.id.bottom_bar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val foodService=FoodClient.foodService

        runBottomBar()


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

    private fun runBottomBar(){
        bottomBar.selectTabAt(0)
        changeFragment(TodayDietFragment)
        bottomBar.onTabSelected={
            when(it.id){
                R.id.navi_todayBtn->{
                    changeFragment(TodayDietFragment)
                }
                R.id.navi_allBtn->{
                    changeFragment(EmptyFragment)
                }
            }
        }
        bottomBar.onTabReselected={
            Log.d("bottom_bar", "Reselected tab: " + it.title)
        }
    }


    private fun changeFragment(fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout,fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        var count=supportFragmentManager.backStackEntryCount
        if (count==0){
            super.onBackPressed()
        }else{
            //supportFragmentManager.popBackStack()
            finish()
        }
    }

    companion object{
        private const val TAG="MainActivity"
    }
}