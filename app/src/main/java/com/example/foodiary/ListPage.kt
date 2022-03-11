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
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.util.*
import kotlin.collections.ArrayList


class ListPage : Fragment() {
    private lateinit var adapter: DateAdapter
    private lateinit var dialogBinding: DateListDialogBinding
    private lateinit var listAdapter1: ListAdapter
    private lateinit var listAdapter2: ListAdapter
    private lateinit var listAdapter3: ListAdapter
    private lateinit var viewModel: diaryViewModel
    private var dateList= ArrayList<String>()
    private val scope by lazy { CoroutineScope(Dispatchers.IO) }
    private lateinit var dialog: Dialog
    private val mfoodList by lazy { ArrayList<FoodItemInList>() }
    private val lfoodList by lazy { ArrayList<FoodItemInList>() }
    private val dfoodList by lazy { ArrayList<FoodItemInList>() }
    private lateinit var listOfbinding: ListOfAllDataBinding
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

        viewModel=
            ViewModelProvider.AndroidViewModelFactory.getInstance((context as Activity).application).create(diaryViewModel::class.java)
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
                    //날짜 저장하는 데이터베이스 생성
                    val date=it[i].date
                    dateList.add(date)
                }
                dateList.sort()
                adapter.addAll(dateList)
                listOfbinding.gridView.adapter=adapter
            }
        })

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

        dialogBinding.trashBtn.setOnClickListener{
            //날짜에 해당하는걸 다 지워야함
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
            startActivity(intent)
        }
    }

    fun deleteDate(date: String){
        scope.launch {
            viewModel.deleteAll(date)
            viewModel.dateDelete(date)
        }
    }
    companion object{
        const val TAG="ListPage"
    }
}
