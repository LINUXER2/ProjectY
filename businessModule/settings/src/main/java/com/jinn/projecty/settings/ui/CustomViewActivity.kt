package com.jinn.projecty.settings.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jinn.projecty.settings.databinding.ActivityCustomViewBinding

class CustomViewActivity : AppCompatActivity() {
    private lateinit var mViewBinding:ActivityCustomViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ActivityCustomViewBinding.inflate(layoutInflater)
        setContentView(mViewBinding.root)
        initView()
    }

    private fun initView(){

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}