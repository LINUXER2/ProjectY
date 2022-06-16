package com.jinn.projecty.settings.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jinn.projecty.settings.databinding.ActivityCustomViewBinding
import com.jinn.projecty.utils.LogUtils
import com.jinn.projecty.widgets.FadeInTextView

class CustomViewActivity : AppCompatActivity() {
    private lateinit var mViewBinding:ActivityCustomViewBinding
    companion object{
        const val TAG = "CustomViewActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ActivityCustomViewBinding.inflate(layoutInflater)
        setContentView(mViewBinding.root)
        initView()
    }

    private fun initView(){

    }

    override fun onResume() {
        super.onResume()
        val fadeInTextView = mViewBinding.fadeInTextView
        fadeInTextView.Builder().setTextString("这是一个打印机aaaa").setDuration(100).setTextAnimListener {
            LogUtils.d(TAG,"FadeIn Anim end")
        }.build().startAnim()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}