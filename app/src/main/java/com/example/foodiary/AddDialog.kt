package com.example.foodiary

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.lang.IllegalStateException
import java.util.zip.Inflater


class AddDialog(private var context: Context){
    private val dialog=Dialog(context)

    fun showDialog(){
        val foodService=FoodClient.foodService

        foodService.getFoodName("af2bd97db6b846529d0e","I2790","json","1","1")
            .enqueue(object: Callback<FoodList> {
                override fun onResponse(call: Call<FoodList>, response: Response<FoodList>) {
                    if (response.isSuccessful.not()){
                        //Log.e(TAG,"조회 실패")
                        return
                    }else{
                        //Log.d(TAG,response.body().toString())
                        response.body()?.let {
                            val foodName=it.list.food[0].foodName
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

        //spinner
        ArrayAdapter.createFromResource(
            context,
            R.array.category,
            android.R.layout.simple_spinner_dropdown_item
        ).also {arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter=arrayAdapter
        }

        dialog.show()

        //save button click event
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

    companion object{
        private const val TAG="AddDialog"
    }
}