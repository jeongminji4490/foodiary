package com.example.foodiary

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*

class MorningDietPage : Fragment() {
    private lateinit var diaryAdapter: DiaryAdapter
    private lateinit var dViewModel: diaryViewModel
    private val scope= CoroutineScope(Dispatchers.IO)
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
        val addBtn: ImageButton =view.findViewById(R.id.m_add_btn)
        val morningText: TextView=view.findViewById(R.id.morning_text)
        val recyclerView: RecyclerView=view.findViewById(R.id.morning_recyclerView)
        val lifecycleOwner: LifecycleOwner=this.viewLifecycleOwner
        diaryAdapter= DiaryAdapter(context as Activity)
        dViewModel=
            ViewModelProvider.AndroidViewModelFactory.getInstance((context as Activity).application).create(diaryViewModel::class.java)
        //delete dialog views
        val deleteDialog=Dialog(context as Activity)
        deleteDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        deleteDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        deleteDialog.setContentView(R.layout.delete_diet_dialog)
        deleteDialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        deleteDialog.setCanceledOnTouchOutside(true)
        deleteDialog.setCancelable(true)
        val cancelBtn: ImageButton=deleteDialog.findViewById(R.id.delete_dialog_cancelBtn)
        val deleteOkBtn: ImageButton=deleteDialog.findViewById(R.id.delete_dialog_okBtn)
        //val num=dViewModel.getMorningCount() //이 코드는 메인쓰레드 에러, 따라서 코루틴스코프에서 실행
        //morningList=dViewModel.getMorningAll() //얘는 no error..? 왜??
        /**이슈: 백그라운드 스레드에서 Observe 사용 불가!!**/
        //그럼 num을 어떻게 갖고오지..?;
        //Observe를 ViewModel에서 호출??
//        scope.launch {
//            num=dViewModel.getMorningCount()
//            Log.e("in rowCount", num.toString())
//        }

        //diaryAdapter.list.clear()
        //추가) 날짜별로, 식사시간별로 리스트업하기
        dViewModel.getMorningAll().observe(this.viewLifecycleOwner, Observer {
            it?.let {
                for (i: Int in it.indices){
                    val serialNum=it[i].serialNum
                    val category=it[i].category
                    val name=it[i].food_name

                    val calorie=it[i].food_calorie
                    if (category == "식사"){
                        val item=DiaryItemInList(serialNum,category,name,calorie,0)
                        diaryAdapter.add(item)
                        Log.e("InsertViewType(식사)", item.viewType.toString())
                    }else{
                        val item=DiaryItemInList(serialNum,category,name,calorie,1)
                        diaryAdapter.add(item)
                        Log.e("InsertViewType(간식)", item.viewType.toString())
                    }
                    recyclerView.adapter=diaryAdapter
                    recyclerView.layoutManager=LinearLayoutManager(context)
                }
            }
        })

        //recyclerview item click event
        //아이템 삭제를 묻는 다이얼로그 띄우기
        diaryAdapter.itemClick=object : DiaryAdapter.ItemClick{
            override fun onClick(view: View, position: Int, list: ArrayList<DiaryItemInList>) {
                deleteDialog.show()
                //아이템 삭제(serial num으로 삭제)
                deleteOkBtn.setOnClickListener(View.OnClickListener {
                    //Log.e(TAG, list[position].serialNum.toString())
                    delete(list[position].serialNum)
                    deleteDialog.dismiss()
                    diaryAdapter.list.clear()
                })
                cancelBtn.setOnClickListener(View.OnClickListener {
                    deleteDialog.dismiss()
                })
            }
        }

        addBtn.setOnClickListener(View.OnClickListener {
            val dialog=AddDialog(context as Activity)
            dialog.lifecycleOwner=this.viewLifecycleOwner
            dialog.timeText=morningText.text.toString()
            dialog.showDialog()
        })
    }

    fun delete(serialNum: Int)=scope.launch {
        dViewModel.morningDelete(serialNum)
    }

    companion object{
        private const val TAG="MorningDietPage"
    }
}