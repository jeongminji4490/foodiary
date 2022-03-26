package com.example.foodiary

import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodiary.DiaryRepository.Companion.TAG
import com.example.foodiary.FoodClient.foodService
import com.example.foodiary.databinding.ActivityMainBinding
import com.example.foodiary.databinding.AddDietDialogBinding
import org.koin.core.component.KoinComponent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class AddDialog(private var context: Context) : KoinComponent{
    private val dialog by lazy { Dialog(context) } //다이얼로그 객체
    private val adapter by lazy { SearchResultAdapter(context) } //음식 리스트 어댑터
    private val selectedAdapter by lazy { SearchResultAdapter(context) } //음식 선택 결과 어댑터
    lateinit var lifecycleOwner: LifecycleOwner //TodayDiet의 생명주기
    lateinit var timeText: String
    lateinit var selectedDate: String //선택된 날짜
    private lateinit var dViewModel: diaryViewModel //다이어리 DB를 사용하기 위한 다이어리 뷰모델
    private lateinit var dialogBinding: AddDietDialogBinding //추가 다이얼로그에 대한 바인딩 객체

    fun showDialog(){
        dialogBinding= AddDietDialogBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(dialogBinding.root)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

        dViewModel=ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as Application).create(diaryViewModel::class.java)

        val decoration= DividerItemDecoration(context,LinearLayoutManager.VERTICAL)
        val serialNum: Int=0

        dViewModel.getFoodList()
        dialogBinding.searchRecyclerView.addItemDecoration(decoration) //recyclerview 아이템 사이에 줄 표시

        //카테고리 스피너
        ArrayAdapter.createFromResource(
            context,
            R.array.category,
            android.R.layout.simple_spinner_dropdown_item
        ).also {arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            dialogBinding.categorySpinner.adapter=arrayAdapter
        }

        //viewmodel에 데이터가 추가될 때마다 searchRecyclerView 실시간 업데이트
        //livedata는 todaydiet의 생명주기에 따라 행동
        //뷰모델 싱글톤으로 바꾸고 해결..!
        foodViewModel.liveData.observe(lifecycleOwner, Observer {
            adapter.setData(it)
            dialogBinding.loadingText.visibility=View.GONE //"로딩중" 텍스트 unvisible
            dialogBinding.searchRecyclerView.adapter=adapter
            dialogBinding.searchRecyclerView.layoutManager=LinearLayoutManager(context)
        })

        //아이템 삭제
        adapter.itemClick=object : SearchResultAdapter.ItemClick{
            override fun onClick(view: View, position: Int, list: ArrayList<FoodItemInList>) {
                val name=list[position].name
                val calorie=list[position].calorie
                selectedAdapter.add(FoodItemInList(name, calorie))
                dialogBinding.searchSelectRecyclerView.adapter=selectedAdapter
                dialogBinding.searchSelectRecyclerView.layoutManager=LinearLayoutManager(context)
            }
        }

        //직접 입력한 데이터 추가
        dialogBinding.directInputBtn.setOnClickListener {
            if (dialogBinding.directNameEdit.text.toString().isEmpty()) { //식품명이 비어있다면{
                Toast.makeText(context,"식품명을 적어주세요",Toast.LENGTH_SHORT).show()
            }else{
                selectedAdapter.add(FoodItemInList(dialogBinding.directNameEdit.text.toString(),dialogBinding.directCalorieEdit.text.toString()))
                dialogBinding.searchSelectRecyclerView.adapter=selectedAdapter
                dialogBinding.searchSelectRecyclerView.layoutManager=LinearLayoutManager(context)
            }
        }

        dialog.show()

        //save & cancel 버튼 클릭 이벤트
        //serial_num, 날짜, 식사시간, 카테고리, 음식이름, 칼로리 db에 저장
        dialogBinding.dialogSaveBtn.setOnClickListener {
            for (i: Int in 0..selectedAdapter.itemCount){
                try {
                    val name=selectedAdapter.getName(i)
                    val calorie=selectedAdapter.getCalorie(i)
                    dateInsert(date(serialNum, selectedDate))

                    when(timeText){
                        "아침"->{morningInsert(morningDiary(serialNum, selectedDate,dialogBinding.categorySpinner.selectedItem.toString(),name,calorie))}
                        "점심"->{lunchInsert(lunchDiary(serialNum, selectedDate,dialogBinding.categorySpinner.selectedItem.toString(),name,calorie))}
                        "저녁"->{dinnerInsert(dinnerDiary(serialNum, selectedDate,dialogBinding.categorySpinner.selectedItem.toString(),name,calorie))}
                    }
                    MotionToast.darkColorToast(
                        context as Activity,
                        "완료",
                        "일기 저장",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(context as Activity, www.sanju.motiontoast.R.font.helvetica_regular)
                    )
                }catch (e: IndexOutOfBoundsException){
                    Log.e(TAG,"IndexOutOfBouncsException")
                }
            }
            val intent= Intent(context,MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            dialog.dismiss()
            context.startActivity(intent) //다이얼로그 종료 후 메인화면으로 아동
        }
        dialogBinding.dialogCancelBtn.setOnClickListener { dialog.dismiss() }
    }

    fun morningInsert(diary: morningDiary){
        dViewModel.morningInsert(diary)
    }

    fun lunchInsert(diary: lunchDiary) {
        dViewModel.lunchInsert(diary)
    }

    fun dinnerInsert(diary: dinnerDiary) {
        dViewModel.dinnerInsert(diary)
    }

    fun dateInsert(d: date){
        dViewModel.dateInsert(d)
    }

    companion object{
        private const val TAG="AddDialog"
    }

}