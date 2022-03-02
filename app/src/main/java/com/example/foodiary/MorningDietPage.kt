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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodiary.databinding.DeleteDietDialogBinding
import com.example.foodiary.databinding.MorningPageBinding
import kotlinx.coroutines.*

class MorningDietPage : Fragment() {
    private lateinit var diaryAdapter: DiaryAdapter
    private lateinit var dViewModel: diaryViewModel
    private val scope= CoroutineScope(Dispatchers.IO)
    private lateinit var morningList: LiveData<List<morningDiary>>
    private lateinit var selectedDate: String
    private lateinit var morningPageBinding: MorningPageBinding
    private lateinit var dialogBinding: DeleteDietDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return inflater.inflate(R.layout.morning_page, container, false)
        morningPageBinding=DataBindingUtil.inflate(inflater,R.layout.morning_page,container,false)
        return morningPageBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        diaryAdapter= DiaryAdapter(context as Activity)
        dViewModel=
            ViewModelProvider.AndroidViewModelFactory.getInstance((context as Activity).application).create(diaryViewModel::class.java)
        //delete dialog
        val deleteDialog=Dialog(context as Activity)
        deleteDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        deleteDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialogBinding= DeleteDietDialogBinding.inflate(LayoutInflater.from(context))
        deleteDialog.setContentView(dialogBinding.root)
        deleteDialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        deleteDialog.setCanceledOnTouchOutside(true)
        deleteDialog.setCancelable(true)

        //val num=dViewModel.getMorningCount() //이 코드는 메인쓰레드 에러, 따라서 코루틴스코프에서 실행
        //morningList=dViewModel.getMorningAll() //얘는 no error..? 왜??
        /**이슈: 백그라운드 스레드에서 Observe 사용 불가!!**/
        //그럼 num을 어떻게 갖고오지..?;
        //Observe를 ViewModel에서 호출??

        //diaryAdapter.list.clear()
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
                    }else{
                        val item=DiaryItemInList(serialNum,category,name,calorie,1)
                        diaryAdapter.add(item)
                    }
                    morningPageBinding.mRecyclerView.adapter=diaryAdapter
                    morningPageBinding.mRecyclerView.layoutManager=LinearLayoutManager(context)
                }
            }
        })

        //recyclerview item click event
        //아이템 삭제를 묻는 다이얼로그 띄우기
        diaryAdapter.itemClick=object : DiaryAdapter.ItemClick{
            override fun onClick(view: View, position: Int, list: ArrayList<DiaryItemInList>) {
                deleteDialog.show()
                //아이템 삭제(serial num으로 삭제)
                dialogBinding.deleteDialogOkBtn.setOnClickListener(View.OnClickListener {
                    //Log.e(TAG, list[position].serialNum.toString())
                    delete(list[position].serialNum)
                    deleteDialog.dismiss()
                    diaryAdapter.list.clear()
                })
                dialogBinding.deleteDialogCancelBtn.setOnClickListener(View.OnClickListener {
                    deleteDialog.dismiss()
                })
            }
        }

        morningPageBinding.mAddBtn.setOnClickListener(View.OnClickListener {
            val dialog=AddDialog(context as Activity)
            dialog.lifecycleOwner=this.viewLifecycleOwner
            dialog.timeText=morningPageBinding.morningText.text.toString()
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