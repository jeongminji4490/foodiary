package com.example.foodiary

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.foodiary.databinding.DeleteDietDialogBinding
import com.example.foodiary.databinding.TodaydietPageBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class TodayDiet : Fragment(), View.OnClickListener {

    private val calendar by lazy { Calendar.getInstance() } //캘린더 객체
    private lateinit var currentDate: Date //오늘 날짜
    private lateinit var selectedDate : String //이동한 날짜
    private lateinit var binding: TodaydietPageBinding //오늘식단 화면에 대한 바인딩 객체
    private val scope by lazy { CoroutineScope(Dispatchers.IO) } //coroutine scope
    private val dViewModel: diaryViewModel by inject()
    private lateinit var diaryAdapter1: DiaryAdapter //아침식단 어댑터
    private lateinit var diaryAdapter2: DiaryAdapter //점심식단 어댑터
    private lateinit var diaryAdapter3: DiaryAdapter //저녁식단 어댑터
    private lateinit var dialogBinding: DeleteDietDialogBinding //삭제 다이얼로그에 대한 바인딩 객체
    private var diaryList: ArrayList<DiaryItemInList> = ArrayList() //다이어리 데이터를 담기 위한 ArrayList

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

        currentDate() //오늘 날짜로 초기화
        selectedDate=currentDate.dateToString("yyyy-MM-dd") //해당 형식으로 date 객체를 문자열로 변환
        binding.todayDateText.text=selectedDate

        diaryAdapter1= DiaryAdapter(context as Activity)
        diaryAdapter2= DiaryAdapter(context as Activity)
        diaryAdapter3= DiaryAdapter(context as Activity)

        //삭제 다이얼로그
        val deleteDialog= Dialog(context as Activity)
        deleteDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        deleteDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialogBinding= DeleteDietDialogBinding.inflate(LayoutInflater.from(context))
        deleteDialog.setContentView(dialogBinding.root)
        deleteDialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        deleteDialog.setCanceledOnTouchOutside(true)
        deleteDialog.setCancelable(true)
        tab()

        //각 버튼에 대한 이벤트 리스너 등록
        binding.dateLeftBtn.setOnClickListener(this)
        binding.dateRightBtn.setOnClickListener(this)
        binding.mAddBtn.setOnClickListener(this)
        binding.lAddBtn.setOnClickListener(this)
        binding.dAddBtn.setOnClickListener(this)

        //아침식단 리스트의 아이템 클릭 시 삭제 다이얼로그 호출
        diaryAdapter1.itemClick=object : DiaryAdapter.ItemClick{
            override fun onClick(view: View, position: Int, list: ArrayList<DiaryItemInList>) {
                deleteDialog.show()
                dialogBinding.deleteDialogOkBtn.setOnClickListener {
                    delete(list[position].serialNum,"m")
                    deleteDialog.dismiss()
                    diaryAdapter1.list.clear()
                }
                dialogBinding.deleteDialogCancelBtn.setOnClickListener {
                    deleteDialog.dismiss()
                }
            }
        }

        //점심식단 리스트의 아이템 클릭 시 삭제 다이얼로그 호출
        diaryAdapter2.itemClick=object : DiaryAdapter.ItemClick{
            override fun onClick(v: View, position: Int, list: ArrayList<DiaryItemInList>) {
                deleteDialog.show()
                dialogBinding.deleteDialogOkBtn.setOnClickListener {
                    delete(list[position].serialNum,"l")
                    deleteDialog.dismiss()
                    diaryAdapter3.list.clear()
                }
                dialogBinding.deleteDialogCancelBtn.setOnClickListener {
                    deleteDialog.dismiss()
                }
            }
        }

        //저녁식단 리스트의 아이템 클릭 시 삭제 다이얼로그 호출
        diaryAdapter3.itemClick=object : DiaryAdapter.ItemClick{
            override fun onClick(view: View, position: Int, list: ArrayList<DiaryItemInList>) {
                deleteDialog.show()
                dialogBinding.deleteDialogOkBtn.setOnClickListener {
                    delete(list[position].serialNum, "d")
                    deleteDialog.dismiss()
                    diaryAdapter3.list.clear()
                }
                dialogBinding.deleteDialogCancelBtn.setOnClickListener {
                    deleteDialog.dismiss()
                }
            }
        }

    }

    //날짜에 따라 데이터 리스트업
    fun tab(){
        //아침
        diaryAdapter1.removeAll()
        dViewModel.getMorningAll().observe(this.viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                diaryList.clear()
                for (i: Int in it.indices){
                    if(it[i].date==selectedDate){
                        val serialNum=it[i].serialNum
                        val category=it[i].category
                        val name=it[i].food_name
                        val calorie=it[i].food_calorie

                        if (category == "식사"){
                            diaryList.add(DiaryItemInList(serialNum,category,name,calorie,0))
                        }else{
                            diaryList.add(DiaryItemInList(serialNum,category,name,calorie,1))
                        }
                    }
                }
                diaryAdapter1.addAll(diaryList)
                binding.mRecyclerView.adapter=diaryAdapter1
                binding.mRecyclerView.layoutManager=LinearLayoutManager(context)
            }
        })
        dViewModel.getLunchAll().observe(this.viewLifecycleOwner, androidx.lifecycle.Observer{
            //점심
            diaryAdapter2.removeAll()
            diaryList.clear()
            it?.let {
                for (i: Int in it.indices){
                    if(it[i].date==selectedDate){
                        val serialNum=it[i].serialNum
                        val category=it[i].category
                        val name=it[i].food_name
                        val calorie=it[i].food_calorie
                        if (category == "식사"){
                            diaryList.add(DiaryItemInList(serialNum,category,name,calorie,0))
                        }else{
                            diaryList.add(DiaryItemInList(serialNum,category,name,calorie,1))
                        }
                    }
                }
                diaryAdapter2.addAll(diaryList)
                binding.lRecyclerView.adapter=diaryAdapter2
                binding.lRecyclerView.layoutManager=LinearLayoutManager(context)
            }
        })
        dViewModel.getDinnerAll().observe(this.viewLifecycleOwner, androidx.lifecycle.Observer{
            //저녁
            diaryAdapter3.removeAll()
            diaryList.clear()
            it?.let {
                diaryList.clear()
                for (i: Int in it.indices){
                    if (it[i].date==selectedDate){
                        val serialNum=it[i].serialNum
                        val category=it[i].category
                        val name=it[i].food_name
                        val calorie=it[i].food_calorie
                        if (category == "식사"){
                            diaryList.add(DiaryItemInList(serialNum,category,name,calorie,0))
                        }else{
                            diaryList.add(DiaryItemInList(serialNum,category,name,calorie,1))
                        }
                    }
                }
                diaryAdapter3.addAll(diaryList)
                binding.dRecyclerView.adapter=diaryAdapter3
                binding.dRecyclerView.layoutManager=LinearLayoutManager(context)
            }
        })
    }

    //time(아침/점심/저녁)에 따라 각 테이블에서 삭제
    fun delete(serialNum: Int, time: String)=scope.launch {
        when(time){
            "m"->{dViewModel.morningDelete(serialNum)}
            "l"->{dViewModel.lunchDelete(serialNum)}
            "d"->{dViewModel.dinnerDelete(serialNum)}
        }
    }

    //date 객체를 문자열로 변환
    fun Date.dateToString(format: String, local: Locale=Locale.getDefault()): String{
        val formatter=SimpleDateFormat(format, local)
        return formatter.format(this)
    }

    fun currentDate() { //오늘날짜로 calendar 셋팅
        currentDate=calendar.time
        calendar.time=currentDate
    }

    //각 버튼 클릭 시 발생하는 이벤트
    override fun onClick(v: View) {
        when(v.id){
            //추가 다이얼로그 호출
            R.id.m_add_btn->{callDialog(binding.morningText.text.toString())}
            R.id.l_add_btn->{callDialog(binding.lunchText.text.toString())}
            R.id.d_add_btn->{callDialog(binding.dinnerText.text.toString())}
            R.id.date_leftBtn->{ //이전 날짜로 이동
                calendar.add(Calendar.DATE,-1)
                currentDate=calendar.time
                selectedDate=currentDate.dateToString("yyyy-MM-dd")
                binding.todayDateText.text=selectedDate
                tab()
            }
            R.id.date_rightBtn->{ //다음 날짜로 이동
                calendar.add(Calendar.DATE,1)
                currentDate=calendar.time
                selectedDate=currentDate.dateToString("yyyy-MM-dd")
                binding.todayDateText.text=selectedDate
                tab()
            }
        }
    }

    //추가 다이얼로그 호출
    fun callDialog(text: String){
        val dialog=AddDialog(context as Activity)
        dialog.lifecycleOwner=this.viewLifecycleOwner
        dialog.timeText=text
        dialog.selectedDate=selectedDate
        dialog.showDialog()
    }

    companion object{
        private val TAG="TodayDiet"
    }
}