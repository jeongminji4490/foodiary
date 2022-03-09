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
import com.example.foodiary.databinding.ActivityMainBinding
import com.example.foodiary.databinding.MainpageItemBinding
import com.google.android.material.navigation.NavigationView
import nl.joery.animatedbottombar.AnimatedBottomBar

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    //by lazy : 각 변수가 처음 사용되는 시점에서 지연 초기화
    private val TodayDietFragment by lazy { TodayDiet() }
    private val EmptyFragment by lazy { ListPage() }
    private val LicenseFragment by lazy { LicensePage() }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)

        val naviView: NavigationView=findViewById(R.id.navi_view)

        runBottomBar()
        naviView.setNavigationItemSelectedListener(this)
        binding.menuBtn.setOnClickListener(View.OnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        })
    }

    private fun runBottomBar(){
        binding.bottomBar.selectTabAt(0)
        changeFragment(TodayDietFragment)
        binding.bottomBar.onTabSelected={
            when(it.id){
                R.id.navi_todayBtn->{
                    changeFragment(TodayDietFragment)
                }
                R.id.navi_allBtn->{
                    changeFragment(EmptyFragment)
                }
            }
        }
        binding.bottomBar.onTabReselected={
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
        val count=supportFragmentManager.backStackEntryCount
        if (count==0){
            super.onBackPressed()
        }else{
            finish()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
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