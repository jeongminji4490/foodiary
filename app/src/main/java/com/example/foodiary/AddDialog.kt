package com.example.foodiary

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.lang.IllegalStateException
import java.util.zip.Inflater


class AddDialog(private var context: Context){
    private val dialog=Dialog(context)

    fun showDialog(){
        dialog.setContentView(R.layout.add_diet_dialog)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        val saveBtn: Button=dialog.findViewById(R.id.dialogSaveBtn)
        val cancelBtn: Button=dialog.findViewById(R.id.dialogCancelBtn)
        dialog.show()
        saveBtn.setOnClickListener(View.OnClickListener {
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

}