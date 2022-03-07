package com.example.foodiary

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import android.widget.ListView
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager


class ListPage : Fragment() {
    private lateinit var adapter: DateAdapter
    private lateinit var listAdapter1: ListAdapter
    private lateinit var listAdapter2: ListAdapter
    private lateinit var listAdapter3: ListAdapter
    private lateinit var viewModel: diaryViewModel
    private lateinit var dialog: Dialog
    private var list=ArrayList<String>()
    private var mfoodList=ArrayList<FoodItemInList>()
    private var lfoodList=ArrayList<FoodItemInList>()
    private var dfoodList=ArrayList<FoodItemInList>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.list_of_all_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gridView: GridView=view.findViewById(R.id.gridView)

        viewModel=
            ViewModelProvider.AndroidViewModelFactory.getInstance((context as Activity).application).create(diaryViewModel::class.java)
        adapter= DateAdapter(context as Activity)
        listAdapter1= ListAdapter(context as Activity)
        listAdapter2= ListAdapter(context as Activity)
        listAdapter3= ListAdapter(context as Activity)

        dialog= Dialog(context as Activity)
        dialog.setContentView(R.layout.date_list_dialog)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        val mListView: ListView =dialog.findViewById(R.id.morning_listView)
        val lListView: ListView=dialog.findViewById(R.id.lunch_listView)
        val dListView: ListView=dialog.findViewById(R.id.dinner_listView)

        list.clear()

        //세로도 고려
        viewModel.getDateAll().observe(this.viewLifecycleOwner, Observer {
            it?.let {
                for (i: Int in it.indices){
                    //날짜 저장하는 데이터베이스 생성
                    val date=it[i].date
                    list.add(date)
                }
                adapter.addAll(list)
                gridView.adapter=adapter
            }
        })

        gridView.onItemClickListener = OnItemClickListener { a_parent, a_view, a_position, a_id ->
                val selectedDate=adapter.getItem(a_position)
                viewModel.getMorningAll().observe(this.viewLifecycleOwner, Observer {
                    mfoodList.clear()
                    listAdapter1.removeAll()
                    it?.let {
                        for (i: Int in it.indices){
                            if(it[i].date==selectedDate){
                                val name=it[i].food_name
                                val calorie=it[i].food_calorie
                                mfoodList.add(FoodItemInList(name, calorie))
                            }
                        }
                        listAdapter1.addAll(mfoodList)
                        mListView.adapter=listAdapter1
                    }
                })
                viewModel.getLunchAll().observe(this.viewLifecycleOwner, Observer {
                    it?.let {
                        lfoodList.clear()
                        listAdapter2.removeAll()
                        for (i: Int in it.indices){
                            if(it[i].date==selectedDate){
                                val name=it[i].food_name
                                val calorie=it[i].food_calorie
                                lfoodList.add(FoodItemInList(name, calorie))
                            }
                        }
                        listAdapter2.addAll(lfoodList)
                        lListView.adapter=listAdapter2
                    }
                })
                viewModel.getDinnerAll().observe(this.viewLifecycleOwner, Observer {
                    it?.let {
                        dfoodList.clear()
                        listAdapter3.removeAll()
                        for (i: Int in it.indices){
                            if(it[i].date==selectedDate){
                                val name=it[i].food_name
                                val calorie=it[i].food_calorie
                                dfoodList.add(FoodItemInList(name, calorie))
                            }
                        }
                        listAdapter3.addAll(dfoodList)
                        dListView.adapter=listAdapter3
                    }
                })
                dialog.show()
            }
    }
}