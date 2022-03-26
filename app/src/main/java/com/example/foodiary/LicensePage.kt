package com.example.foodiary

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.foodiary.databinding.ActivityMainBinding
import com.example.foodiary.databinding.LicensePageBinding

class LicensePage: Fragment(), View.OnClickListener {
    private lateinit var binding: LicensePageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= LicensePageBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
    }

    private fun setOnClickListener(){
        val btnSequence=binding.container.children
        btnSequence.forEach {
            //뷰 그룹의 모든 자식 뷰들에 리스너 등록
            it.setOnClickListener(this)
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.licenseBtn1->{
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Droppers/AnimatedBottomBar#license"))
                startActivity(intent)
            }
            R.id.licenseBtn2->{
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Spikeysanju/MotionToast#license"))
                startActivity(intent)
            }
        }
    }


}
