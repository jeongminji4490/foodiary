package com.example.foodiary

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentStateAdapter(fragmentActivity: TodayDiet): FragmentStateAdapter(fragmentActivity) {

    //뷰페이저에 연결할 프래그먼트 생성
    val fragmentList= listOf<Fragment>(MorningDietPage(), LunchDietPage(), DinnerDietPage())

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    //뷰페이저의 각 페이지에서 노출할 프래그먼트 설정
    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}