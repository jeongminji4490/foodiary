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
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodiary.databinding.AddDietDialogBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class AddDialog(private var context: Context){
    private val dialog=Dialog(context)
    private val adapter=SearchResultAdapter(context)
    private val selectedAdapter=SearchResultAdapter(context)
    private val viewModel=foodViewModel()
    private val scope= CoroutineScope(Dispatchers.IO)
    lateinit var lifecycleOwner: LifecycleOwner
    lateinit var timeText: String
    lateinit var selectedDate: String
    private lateinit var dViewModel: diaryViewModel
    private lateinit var dialogBinding: AddDietDialogBinding
    private var num: Int=0

    fun showDialog(){
        //room db는 메인쓰레드에서 생성 불가
        dialogBinding= AddDietDialogBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(dialogBinding.root)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

        dViewModel=ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as Application).create(diaryViewModel::class.java)
        val foodService=FoodClient.foodService
        App.prefs.get("myDatePrefs")?.let { it1 ->
            selectedDate=it1
        }
        Log.d(TAG,timeText)

        val decoration= DividerItemDecoration(context,LinearLayoutManager.VERTICAL)
        val serialNum: Int=0

        foodService.getFoodName("af2bd97db6b846529d0e","I2790","json")
            .enqueue(object: Callback<FoodList> {
                override fun onResponse(call: Call<FoodList>, response: Response<FoodList>) {
                    if (response.isSuccessful.not()){
                        Log.e(TAG,"조회 실패")
                        return
                    }else{
                        response.body()?.let {
                            dialogBinding.loadingText.visibility=View.GONE
                            for (i: Int in 0..50){
                                val name=it.list.food[i].foodName
                                val kcal=it.list.food[i].kcal
                                viewModel.addItem(FoodItemInList(name, kcal))
                            }
                        }
                    }
                }
                override fun onFailure(call: Call<FoodList>, t: Throwable) {
                    Log.e(TAG,"연결 실패 ㅠ")
                    Log.e(TAG,t.toString())
                }

            })
        dialogBinding.searchRecyclerView.addItemDecoration(decoration)
//        adapter.list=datas
//        adapter.addAll(datas)

        //category spinner
        ArrayAdapter.createFromResource(
            context,
            R.array.category,
            android.R.layout.simple_spinner_dropdown_item
        ).also {arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            dialogBinding.categorySpinner.adapter=arrayAdapter
        }

        //UI 실시간 업데이트
        viewModel.liveData.observe(lifecycleOwner, Observer {
            adapter.setData(it)
            dialogBinding.searchRecyclerView.adapter=adapter
            dialogBinding.searchRecyclerView.layoutManager=LinearLayoutManager(context)
        })

        //recyclerview item click event
        adapter.itemClick=object : SearchResultAdapter.ItemClick{
            override fun onClick(view: View, position: Int, list: ArrayList<FoodItemInList>) {
                //Toast.makeText(context,list[position].name,Toast.LENGTH_SHORT).show()
                val name=list[position].name
                val calorie=list[position].calorie
                selectedAdapter.add(FoodItemInList(name, calorie))
                Log.e(TAG,name+calorie)
                dialogBinding.searchSelectRecyclerView.adapter=selectedAdapter
                dialogBinding.searchSelectRecyclerView.layoutManager=LinearLayoutManager(context)
            }
        }

        //직접 입력한 데이터 추가
        dialogBinding.directInputBtn.setOnClickListener(View.OnClickListener {
            if (dialogBinding.directNameEdit.text.toString().isEmpty()) { //식품명이 비어있다면(칼로리는 안적어도됨!){
                Toast.makeText(context,"식품명을 적어주세요",Toast.LENGTH_SHORT).show()
            }else{
                selectedAdapter.add(FoodItemInList(dialogBinding.directNameEdit.text.toString(),dialogBinding.directCalorieEdit.text.toString()))
                dialogBinding.searchSelectRecyclerView.adapter=selectedAdapter
                dialogBinding.searchSelectRecyclerView.layoutManager=LinearLayoutManager(context)
            }
        })

        dialog.show()

        //save & cancel button click event
        //serial_num, 날짜, 식사시간, 카테고리, 음식이름, 칼로리 db에 저장
        //일지 제대로 저장되는지 확인하고(ok), 메인페이지에서 리스트업하기!
        dialogBinding.dialogSaveBtn.setOnClickListener(View.OnClickListener {
            //sRecyclerView에 있는거 다 저장, 즉 selectedAdapter의 모든 아이템들을 저장해야함
            for (i: Int in 0..selectedAdapter.itemCount){
                try {
                    val name=selectedAdapter.getName(i)
                    val calorie=selectedAdapter.getCalorie(i)
                    dateInsert(date(serialNum, selectedDate))

                    if(timeText=="아침"){ //date 테이블에 selectedDate가 존재한다면 date 테이블에 삽입 금지
                        morningInsert(morningDiary(serialNum, selectedDate,dialogBinding.categorySpinner.selectedItem.toString(),name,calorie))
                    }else if(timeText=="점심"){
                        lunchInsert(lunchDiary(serialNum, selectedDate,dialogBinding.categorySpinner.selectedItem.toString(),name,calorie))
                    }else{
                        dinnerInsert(dinnerDiary(serialNum, selectedDate,dialogBinding.categorySpinner.selectedItem.toString(),name,calorie))
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
                    val intent= Intent(context,MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    dialog.dismiss()
                    context.startActivity(intent)
                }catch (e: IndexOutOfBoundsException){
                    Log.e(TAG,"IndexOutOfBouncsException") //이부분 오류발생, 근데 저장은 잘 됨...
                }
            }
        })
        dialogBinding.dialogCancelBtn.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
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