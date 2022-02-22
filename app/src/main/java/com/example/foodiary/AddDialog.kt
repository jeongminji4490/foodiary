package com.example.foodiary

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.graphics.drawable.ClipDrawable.VERTICAL
import android.icu.lang.UCharacter
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.lang.IllegalStateException
import java.util.zip.Inflater


class AddDialog(private var context: Context){
    private val dialog=Dialog(context)
    private var datas= arrayListOf<FoodItemInList>()
    private val adapter=SearchResultAdapter(context)
    private val viewModel=foodViewModel()
    lateinit var lifecycleOwner: LifecycleOwner

    @SuppressLint("NotifyDataSetChanged")
    fun showDialog(){
        val foodService=FoodClient.foodService

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
                                Log.e(TAG,name+","+kcal)
                                //datas.add(FoodItemInList(name,kcal))
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
        val decoration= DividerItemDecoration(context,LinearLayoutManager.VERTICAL)
        recyclerView.addItemDecoration(decoration)
//        adapter.list=datas
//        adapter.addAll(datas)

        viewModel.liveData.observe(lifecycleOwner, Observer {
            adapter.setData(it)
            recyclerView.adapter=adapter
            recyclerView.layoutManager=LinearLayoutManager(context)
        })

        //adapter.notifyDataSetChanged()

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