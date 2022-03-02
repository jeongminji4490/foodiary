package com.example.foodiary

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import java.text.SimpleDateFormat
import java.util.*

class TodayDiet : Fragment() {

    private lateinit var selectedDate:String
    private lateinit var currentDate: Date
    private val calendar by lazy {
        Calendar.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.todaydiet_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager: ViewPager2 =view.findViewById(R.id.diet_viewPager)
        val fragmentStateAdapter = FragmentStateAdapter(this)
        viewPager.adapter=fragmentStateAdapter //어댑터와 뷰페이저 연결
//        val leftBtn:ImageButton=view.findViewById(R.id.date_leftBtn)
//        val rightBtn:ImageButton=view.findViewById(R.id.date_rightBtn)
        val selectedDateText: TextView=view.findViewById(R.id.todayDateText)

        currentDate() //먼저 오늘 날짜로 초기화
        selectedDate=currentDate.dateToString("yyyy-MM-dd")
        selectedDateText.text=selectedDate
        App.prefs.set("myDatePrefs",selectedDate)

        //이전 날짜로
//        leftBtn.setOnClickListener(View.OnClickListener {
//            calendar.add(Calendar.DATE,-1)
//            currentDate=calendar.time
//            selectedDate=currentDate.dateToString("yyyy-MM-dd")
//            selectedDateText.text=selectedDate
//            App.prefs.set("myDatePrefs",selectedDate)
//            Log.d(TAG,selectedDate)
//        })
//        //다음 날짜로
//        rightBtn.setOnClickListener(View.OnClickListener {
//            calendar.add(Calendar.DATE,1)
//            currentDate=calendar.time
//            selectedDate=currentDate.dateToString("yyyy-MM-dd")
//            selectedDateText.text=selectedDate
//            App.prefs.set("myDatePrefs",selectedDate)
//            Log.d(TAG,selectedDate)
//        })
    }

    fun Date.dateToString(format: String, local: Locale=Locale.getDefault()): String{
        val formatter=SimpleDateFormat(format, local)
        return formatter.format(this)
    }

    fun currentDate(): Unit { //오늘날짜로 calendar 셋팅
        currentDate=calendar.time
        calendar.time=currentDate
    }

    companion object{
        private val TAG="TodayDiet"
    }

}