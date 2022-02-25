package com.example.foodiary

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MorningDietPage : Fragment() {
    private lateinit var diaryAdapter: DiaryAdapter
    private lateinit var dViewModel: diaryViewModel
    private val scope= CoroutineScope(Dispatchers.IO)

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
        val addBtn: Button=view.findViewById(R.id.m_add_btn)
        val morningText: TextView=view.findViewById(R.id.morning_text)
        val recyclerView: RecyclerView=view.findViewById(R.id.morning_recyclerView)
        diaryAdapter= DiaryAdapter(context as Activity)
        dViewModel=
            ViewModelProvider.AndroidViewModelFactory.getInstance(context as Application).create(diaryViewModel::class.java)

        addBtn.setOnClickListener(View.OnClickListener {
            val dialog=AddDialog(context as Activity)
            dialog.lifecycleOwner=this.viewLifecycleOwner
            dialog.timeText=morningText.text.toString()
            dialog.showDialog()
//            MotionToast.createColorToast(
//                context as Activity,
//                "완료",
//                "일기 저장",
//                MotionToastStyle.SUCCESS,
//                MotionToast.GRAVITY_BOTTOM,
//                MotionToast.LONG_DURATION,
//                ResourcesCompat.getFont(context as Activity, www.sanju.motiontoast.R.font.helvetica_regular)
//            )
        })
    }

    fun selectAll()=scope.launch {
        //dViewModel.morningInsert(diary)
    }
}