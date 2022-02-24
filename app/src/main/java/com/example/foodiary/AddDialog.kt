package com.example.foodiary

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.graphics.drawable.ClipDrawable.VERTICAL
import android.icu.lang.UCharacter
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.lang.IllegalStateException
import java.lang.IndexOutOfBoundsException
import java.util.zip.Inflater


class AddDialog(private var context: Context){
    private val dialog=Dialog(context)
    private var datas= arrayListOf<FoodItemInList>()
    private val adapter=SearchResultAdapter(context)
    private val selectedAdapter=SearchResultAdapter(context)
    private val viewModel=foodViewModel()
    private val scope= CoroutineScope(Dispatchers.IO)
    lateinit var lifecycleOwner: LifecycleOwner
    lateinit var timeText: String
    lateinit var selectedDate: String
    private lateinit var dViewModel: diaryViewModel

    fun showDialog(){
        //room db는 메인쓰레드에서 생성 불가
        dViewModel=ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as Application).create(diaryViewModel::class.java)
        val foodService=FoodClient.foodService
        App.prefs.get("myDatePrefs")?.let { it1 ->
            selectedDate=it1
        }
        Log.d(TAG,timeText)

        foodService.getFoodName("af2bd97db6b846529d0e","I2790","json")
            .enqueue(object: Callback<FoodList> {
                override fun onResponse(call: Call<FoodList>, response: Response<FoodList>) {
                    if (response.isSuccessful.not()){
                        Log.e(TAG,"조회 실패")
                        return
                    }else{
                        response.body()?.let {
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

        dialog.setContentView(R.layout.add_diet_dialog)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        //views
        val saveBtn: Button=dialog.findViewById(R.id.dialogSaveBtn)
        val cancelBtn: Button=dialog.findViewById(R.id.dialogCancelBtn)
        val spinner: Spinner=dialog.findViewById(R.id.category_spinner)
        val recyclerView: RecyclerView=dialog.findViewById(R.id.search_recyclerView)
        val sRecyclerView: RecyclerView=dialog.findViewById(R.id.searchSelect_recyclerView)
        val decoration= DividerItemDecoration(context,LinearLayoutManager.VERTICAL)
        val directAddBtn: Button=dialog.findViewById(R.id.directInput_Btn)
        val name_edit: EditText=dialog.findViewById(R.id.directName_Edit)
        val calorie_edit: EditText=dialog.findViewById(R.id.directCalorie_Edit)
        val serialNum: Int=0
        recyclerView.addItemDecoration(decoration)
//        adapter.list=datas
//        adapter.addAll(datas)

        //category spinner
        ArrayAdapter.createFromResource(
            context,
            R.array.category,
            android.R.layout.simple_spinner_dropdown_item
        ).also {arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter=arrayAdapter
        }

        //UI 실시간 업데이트
        viewModel.liveData.observe(lifecycleOwner, Observer {
            adapter.setData(it)
            recyclerView.adapter=adapter
            recyclerView.layoutManager=LinearLayoutManager(context)
        })

        //recyclerview item click event
        adapter.itemClick=object : SearchResultAdapter.ItemClick{
            override fun onClick(view: View, position: Int, list: ArrayList<FoodItemInList>) {
                //Toast.makeText(context,list[position].name,Toast.LENGTH_SHORT).show()
                val name=list[position].name
                val calorie=list[position].calorie
                selectedAdapter.add(FoodItemInList(name, calorie))
                Log.e(TAG,name+calorie)
                sRecyclerView.adapter=selectedAdapter
                sRecyclerView.layoutManager=LinearLayoutManager(context)
            }
        }

        //직접 입력한 데이터 추가
        directAddBtn.setOnClickListener(View.OnClickListener {
            if (name_edit.text.toString().isEmpty()) { //식품명이 비어있다면(칼로리는 안적어도됨!){
                Toast.makeText(context,"식품명을 적어주세요",Toast.LENGTH_SHORT).show()
            }else{
                selectedAdapter.add(FoodItemInList(name_edit.text.toString(),calorie_edit.text.toString()))
                sRecyclerView.adapter=selectedAdapter
                sRecyclerView.layoutManager=LinearLayoutManager(context)
            }
        })

        dialog.show()

        //save & cancel button click event
        //serial_num, 날짜, 식사시간, 카테고리, 음식이름, 칼로리 db에 저장
        saveBtn.setOnClickListener(View.OnClickListener {
            //sRecyclerView에 있는거 다 저장, 즉 selectedAdapter의 모든 아이템들을 저장해야함
            for (i: Int in 0..selectedAdapter.itemCount){
                try {
                    val name=selectedAdapter.getName(i)
                    val calorie=selectedAdapter.getCalorie(i)
                    insert(morningDiary(serialNum, selectedDate,spinner.selectedItem.toString(),name,calorie))
                }catch (e: IndexOutOfBoundsException){
                    Log.e(TAG,"IndexOutOfBouncsException")
                }
            }
            MotionToast.createColorToast(
                context as Activity,
                "완료",
                "일기 저장",
                MotionToastStyle.SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(context as Activity, www.sanju.motiontoast.R.font.helvetica_regular)
            )
            dialog.dismiss()
        })
        cancelBtn.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
    }

    fun insert(diary: morningDiary)=scope.launch {
        dViewModel.morningInsert(diary)
    }

    companion object{
        private const val TAG="AddDialog"
    }

}