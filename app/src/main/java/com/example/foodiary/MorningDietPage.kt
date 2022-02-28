package com.example.foodiary

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import kotlin.properties.Delegates

class MorningDietPage : Fragment() {
    private lateinit var diaryAdapter: DiaryAdapter
    private lateinit var dViewModel: diaryViewModel
    private val scope= CoroutineScope(Dispatchers.IO)
    private lateinit var num: Deferred<Int>
    private lateinit var morningList: LiveData<List<morningDiary>>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.morning_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addBtn: Button=view.findViewById(R.id.m_add_btn)
        val morningText: TextView=view.findViewById(R.id.morning_text)
        val recyclerView: RecyclerView=view.findViewById(R.id.morning_recyclerView)
        diaryAdapter= DiaryAdapter(context as Activity)
        dViewModel=
            ViewModelProvider.AndroidViewModelFactory.getInstance((context as Activity).application).create(diaryViewModel::class.java)
        //morningList=dViewModel.getMorningAll()
        //val num=dViewModel.getMorningCount() //이 코드는 메인쓰레드 에러
        
        Log.e(TAG, "2"+num.toString())
        //morningList=dViewModel.getMorningAll() //얘는 no error..?

//        for (i:Int in 0..num){
//            dViewModel.getMorningAll().observe(this.viewLifecycleOwner, Observer {
//                Log.e(TAG,it[i].food_name)
//            })
//        }


        addBtn.setOnClickListener(View.OnClickListener {
            val dialog=AddDialog(context as Activity)
            dialog.lifecycleOwner=this.viewLifecycleOwner
            dialog.timeText=morningText.text.toString()
            dialog.showDialog()
        })
    }

    fun rowCount()=scope.async {
        dViewModel.getMorningCount()
        Log.e(TAG, num.toString())
    }

    fun selectAll()=scope.launch {
        //dViewModel.morningInsert(diary)
        morningList=dViewModel.getMorningAll()
    }

    companion object{
        private const val TAG="MorningDietPage"
    }
}