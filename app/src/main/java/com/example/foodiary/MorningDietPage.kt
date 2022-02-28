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
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import kotlin.properties.Delegates

class MorningDietPage : Fragment() {
    private lateinit var diaryAdapter: DiaryAdapter
    private lateinit var dViewModel: diaryViewModel
    private val scope= CoroutineScope(Dispatchers.IO)
    //private lateinit var num: Deferred<Int>
    private var num:Int=0
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
        val lifecycleOwner: LifecycleOwner=this.viewLifecycleOwner
        diaryAdapter= DiaryAdapter(context as Activity)
        dViewModel=
            ViewModelProvider.AndroidViewModelFactory.getInstance((context as Activity).application).create(diaryViewModel::class.java)
        //val num=dViewModel.getMorningCount() //이 코드는 메인쓰레드 에러, 따라서 코루틴스코프에서 실행
        //morningList=dViewModel.getMorningAll() //얘는 no error..? 왜??
        /**이슈: 백그라운드 스레드에서 Observe 사용 불가!!**/
        //그럼 num을 어떻게 갖고오지..?;
        scope.launch {
            num=dViewModel.getMorningCount()
            Log.e("in rowCount", num.toString())
        }

        

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

//    fun rowCount()=scope.launch {
//        num=dViewModel.getMorningCount()
//        Log.e("in rowCount", num.toString())
//    }

    fun selectAll()=scope.async {
        dViewModel.getMorningAll()
    }

    companion object{
        private const val TAG="MorningDietPage"
    }
}