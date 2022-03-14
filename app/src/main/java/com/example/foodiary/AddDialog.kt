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
import androidx.recyclerview.widget.RecyclerView
import com.example.foodiary.databinding.AddDietDialogBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable.start
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class AddDialog(private var context: Context){
    private val dialog by lazy { Dialog(context) } //다이얼로그 객체
    private val adapter by lazy { SearchResultAdapter(context) } //음식 리스트 어댑터
    private val selectedAdapter by lazy { SearchResultAdapter(context) } //음식 선택 결과 어댑터
    private val viewModel by lazy { foodViewModel() } //API 통신 결과를 실시간으로 반영하기 위한 뷰모델
    private val scope by lazy { CoroutineScope(Dispatchers.IO) } //coroutine scope
    lateinit var lifecycleOwner: LifecycleOwner //TodayDiet의 생명주기
    lateinit var timeText: String
    private val foodService by lazy { FoodClient.foodService }
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

        //API 통신
        foodService.getFoodName("af2bd97db6b846529d0e","I2790","json")
            .enqueue(object: Callback<FoodList> {
                override fun onResponse(call: Call<FoodList>, response: Response<FoodList>) {
                    if (response.isSuccessful.not()){
                        Log.e(TAG,"조회 실패")
                        return
                    }else{ //조회에 성공했다면
                        response.body()?.let {
                            dialogBinding.loadingText.visibility=View.GONE //"로딩중" 텍스트 unvisible
                            for (i: Int in 0..999){ //응답받은 데이터들 중 1000개의 데이터에 대해
                                val name=it.list.food[i].foodName //음식명
                                val kcal=it.list.food[i].kcal //1회 제공량당 kcal
                                viewModel.addItem(FoodItemInList(name, kcal)) //뷰모델에 추가
                            }
                        }
                    }
                }
                override fun onFailure(call: Call<FoodList>, t: Throwable) {
                    Log.e(TAG,"연결 실패 ㅠ")
                    Log.e(TAG,t.toString())
                }

            })
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
        viewModel.liveData.observe(lifecycleOwner, Observer {
            adapter.setData(it)
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

    fun morningInsert(diary: morningDiary)=scope.launch {
        dViewModel.morningInsert(diary)
    }

    fun lunchInsert(diary: lunchDiary)=scope.launch {
        dViewModel.lunchInsert(diary)
    }

    fun dinnerInsert(diary: dinnerDiary)=scope.launch {
        dViewModel.dinnerInsert(diary)
    }

    fun dateInsert(d: date)=scope.launch{
        dViewModel.dateInsert(d)
    }

    companion object{
        private const val TAG="AddDialog"
    }

}