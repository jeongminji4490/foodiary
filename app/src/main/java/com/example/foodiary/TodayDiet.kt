package com.example.foodiary

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.foodiary.databinding.TodaydietPageBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TodayDiet : Fragment() {

    private lateinit var selectedDate:String
    private lateinit var currentDate: Date
    private lateinit var binding: TodaydietPageBinding
    private val scope= CoroutineScope(Dispatchers.IO)
    private val dateApp=DateApp.getInstance()
    private val calendar by lazy {
        Calendar.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DataBindingUtil.inflate(inflater,R.layout.todaydiet_page,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentStateAdapter = FragmentStateAdapter(this)
        binding.dietViewPager.adapter=fragmentStateAdapter //어댑터와 뷰페이저 연결

        currentDate() //먼저 오늘 날짜로 초기화
        selectedDate=currentDate.dateToString("yyyy-MM-dd")
        binding.todayDateText.text=selectedDate

        InitDataStore()

        //이전 날짜로
        binding.dateLeftBtn.setOnClickListener(View.OnClickListener {
            calendar.add(Calendar.DATE,-1)
            currentDate=calendar.time
            selectedDate=currentDate.dateToString("yyyy-MM-dd")
            binding.todayDateText.text=selectedDate
            InitDataStore()
            binding.dietViewPager.adapter=fragmentStateAdapter //어댑터와 뷰페이저 연결
        })
        //다음 날짜로
        binding.dateRightBtn.setOnClickListener(View.OnClickListener {
            calendar.add(Calendar.DATE,1)
            currentDate=calendar.time
            selectedDate=currentDate.dateToString("yyyy-MM-dd")
            binding.todayDateText.text=selectedDate
            InitDataStore()
            binding.dietViewPager.adapter=fragmentStateAdapter //어댑터와 뷰페이저 연결
        })
    }

    fun Date.dateToString(format: String, local: Locale=Locale.getDefault()): String{
        val formatter=SimpleDateFormat(format, local)
        return formatter.format(this)
    }

    fun currentDate(): Unit { //오늘날짜로 calendar 셋팅
        currentDate=calendar.time
        calendar.time=currentDate
    }

    fun InitDataStore(){
        scope.launch {
            val date=selectedDate
            dateApp.getDataStore().setDate(date)
            Log.e(TAG, date)
        }
    }

    companion object{
        private val TAG="TodayDiet"
    }

}