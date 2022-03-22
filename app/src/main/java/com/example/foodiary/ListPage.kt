package com.example.foodiary

import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ClipDescription
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.foodiary.databinding.DateListDialogBinding
import com.example.foodiary.databinding.ListOfAllDataBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.util.*
import kotlin.collections.ArrayList


class ListPage : Fragment() {
    private lateinit var adapter: DateAdapter //일정 어댑터
    private lateinit var dialogBinding: DateListDialogBinding //식단목록에 대한 바인딩 객체
    private val viewModel: diaryViewModel by inject()
    private var dateList= ArrayList<String>() //일정(문자열)을 저장하기 위한 ArrayList
    private val scope by lazy { CoroutineScope(Dispatchers.IO) } //coroutine scope
    private lateinit var dialog: Dialog
    private lateinit var listAdapter1: ListAdapter //아침식단 어댑터
    private lateinit var listAdapter2: ListAdapter //점심식단 어댑터
    private lateinit var listAdapter3: ListAdapter //저녁식단 어댑터
    private val mfoodList by lazy { ArrayList<FoodItemInList>() } //아침식단 ArrayList
    private val lfoodList by lazy { ArrayList<FoodItemInList>() } //점심식단 ArrayList
    private val dfoodList by lazy { ArrayList<FoodItemInList>() } //저녁식단 ArrayList
    private lateinit var listOfbinding: ListOfAllDataBinding //모아보기 화면에 대한 바인딩 객체
    private lateinit var date: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listOfbinding=DataBindingUtil.inflate(inflater, R.layout.list_of_all_data, container,false)
        return listOfbinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter= DateAdapter(context as Activity)
        listAdapter1= ListAdapter(context as Activity)
        listAdapter2= ListAdapter(context as Activity)
        listAdapter3= ListAdapter(context as Activity)

        dialog=Dialog(context as Activity)
        dialogBinding= DateListDialogBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(dialogBinding.root)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

        dateList.clear()

        viewModel.getDateAll().observe(this.viewLifecycleOwner, Observer {
            it?.let {
                for (i: Int in it.indices){
                    val date=it[i].date
                    dateList.add(date)
                }
                dateList.sort() //이른 순대로 날짜 정렬
                adapter.addAll(dateList)
                listOfbinding.gridView.adapter=adapter
            }
        })

        //그리드뷰의 각 아이템 클릭 시 해당 날짜에 대한 아침/점심/저녁 식단을 보여주는 다이얼로그 호출
        listOfbinding.gridView.onItemClickListener = OnItemClickListener { a_parent, a_view, a_position, a_id ->
                val selectedDate=adapter.getItem(a_position)
                date= selectedDate as String
                viewModel.getMorningAll().observe(this.viewLifecycleOwner, Observer {
                    mfoodList.clear()
                    listAdapter1.removeAll()
                    it?.let {
                        for (i: Int in it.indices){
                            Log.e(TAG,it[i].food_name)
                            if(it[i].date==selectedDate){
                                Log.e(TAG,selectedDate)
                                val name=it[i].food_name
                                val calorie=it[i].food_calorie
                                mfoodList.add(FoodItemInList(name, calorie))
                            }
                        }
                        listAdapter1.addAll(mfoodList)
                        dialogBinding.morningListView.adapter=listAdapter1
                    }
                })
                viewModel.getLunchAll().observe(this.viewLifecycleOwner, Observer {
                    it?.let {
                        lfoodList.clear()
                        listAdapter2.removeAll()
                        for (i: Int in it.indices){
                            Log.e(TAG,it[i].food_name)
                            if(it[i].date==selectedDate){
                                Log.e(TAG,selectedDate)
                                val name=it[i].food_name
                                val calorie=it[i].food_calorie
                                lfoodList.add(FoodItemInList(name, calorie))
                            }
                        }
                        listAdapter2.addAll(lfoodList)
                        dialogBinding.lunchListView.adapter=listAdapter2
                    }
                })
                viewModel.getDinnerAll().observe(this.viewLifecycleOwner, Observer {
                    it?.let {
                        dfoodList.clear()
                        listAdapter3.removeAll()
                        for (i: Int in it.indices){
                            Log.e(TAG,it[i].food_name)
                            if(it[i].date==selectedDate){
                                Log.e(TAG,selectedDate)
                                val name=it[i].food_name
                                val calorie=it[i].food_calorie
                                dfoodList.add(FoodItemInList(name, calorie))
                            }
                        }
                        listAdapter3.addAll(dfoodList)
                        dialogBinding.dinnerListView.adapter=listAdapter3
                    }
                })
                dialog.show()
            }

        //클릭한 날짜에 해당하는 모든 식단을 삭제
        dialogBinding.trashBtn.setOnClickListener{
            deleteDate(date)
            val intent= Intent(context,MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            dialog.dismiss()
            MotionToast.darkColorToast(
                context as Activity,
                "완료",
                "일기 삭제",
                MotionToastStyle.DELETE,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(context as Activity, www.sanju.motiontoast.R.font.helvetica_regular)
            )
            startActivity(intent) //메인화면으로 이동
        }
    }

    fun deleteDate(date: String){
        viewModel.deleteAll(date)
        viewModel.dateDelete(date)
    }
    companion object{
        const val TAG="ListPage"
    }
}
