package com.example.foodiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.example.foodiary.databinding.ActivityMainBinding
import com.example.foodiary.databinding.MainpageItemBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import nl.joery.animatedbottombar.AnimatedBottomBar

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val TodayDietFragment by lazy { TodayDiet() } //식단
    private val ListFragment by lazy { ListPage() } //모든 식단 모아보기
    private val LicenseFragment by lazy { LicensePage() } //저작권 페이지
    private lateinit var binding: ActivityMainBinding //메인화면에 대한 데이터바인딩 객체

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)

        val naviView: NavigationView=findViewById(R.id.navi_view) //내비게이션뷰

        runBottomBar()
        naviView.setNavigationItemSelectedListener(this)
        //메뉴버튼 클릭 시 드로어 오픈
        binding.menuBtn.setOnClickListener { binding.drawerLayout.openDrawer(GravityCompat.START) }
    }

    //AnimatedBottomBar 클릭 시 프래그먼트 교체
    private fun runBottomBar(){
        binding.bottomBar.selectTabAt(0)
        changeFragment(TodayDietFragment)
        binding.bottomBar.onTabSelected={
            when(it.id){
                R.id.navi_todayBtn->{
                    changeFragment(TodayDietFragment)
                }
                R.id.navi_allBtn->{
                    changeFragment(ListFragment)
                }
            }
        }
        binding.bottomBar.onTabReselected={ }
    }


    private fun changeFragment(fragment: Fragment){ //프래그먼트 교체
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout,fragment) //프래그먼트에 해당하는 화면으로 프레임 레이아웃 교체
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() { //뒤로가기 버튼 클릭 시 종료
        val count=supportFragmentManager.backStackEntryCount
        if (count==0){
            super.onBackPressed()
        }else{
            finish()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean { //저작권 버튼 클릭 시 저작권 페이지로 이동
        when(item.itemId){
            R.id.license_btn->{
                changeFragment(LicenseFragment)
                binding.drawerLayout.close()
            }

        }
        return false
    }

    companion object{
        private const val TAG="MainActivity"
    }

}