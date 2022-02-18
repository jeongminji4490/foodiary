package com.example.foodiary

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class MorningDietPage : Fragment() {

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

        addBtn.setOnClickListener(View.OnClickListener {
            MotionToast.createColorToast(
                context as Activity,
                "완료!",
                "일기 저장",
                MotionToastStyle.SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(context as Activity, www.sanju.motiontoast.R.font.helvetica_regular)
            )
        })
    }
}