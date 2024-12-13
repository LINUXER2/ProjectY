package com.jinn.projecty.settings

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jinn.projecty.base.BaseFragment
import com.jinn.projecty.settings.client.AidlClientManager
import com.jinn.projecty.settings.client.MessengerClientManager
import com.jinn.projecty.settings.databinding.SettingFragmentBinding
import com.jinn.projecty.settings.model.SettingViewModel
import com.jinn.projecty.settings.ui.CustomViewActivity
import com.jinn.projecty.utils.LogUtils
import kotlinx.coroutines.*

class SettingFragment : BaseFragment<SettingViewModel>(), CoroutineScope by MainScope() {

    companion object {
        fun newInstance() = SettingFragment()
        private const val TAG = "SettingFragment"
    }

    private lateinit var mViewBinding: SettingFragmentBinding


    private fun initView() {
        mViewBinding.button1.setOnClickListener {
            activity?.let { it1 -> RestartActivity.launch(it1) }
        }


        mViewBinding.button2.setOnClickListener {
            mViewModel.viewModelScope.launch(Dispatchers.Main){
                mViewModel.insertStudentData()
                mViewModel.queryContentProvider()
            }
            mViewModel.queryAllStudent().observe(viewLifecycleOwner) {
                LogUtils.d(TAG, "getStudentLiveData,size:${it.size}")
            }
        }

        mViewBinding.button3.setOnClickListener {
            activity?.let { it1 -> AidlClientManager.bindService(it1) }
        }

        mViewBinding.button4.setOnClickListener {
            activity?.let { it1 ->
                MessengerClientManager.getInstance(it1).sendMessage()
            }
        }

        mViewBinding.button5.setOnClickListener {
            activity?.let { it1 ->
                val intent = Intent()
                intent.setClass(it1, CustomViewActivity::class.java)
                it1.startActivity(intent)
            }
        }
    }

    override fun onBindViewModelFactory(): ViewModelProvider.Factory? {
        return null
    }

    override fun getLayoutId(): Int {
        return R.layout.setting_fragment
    }

    override fun initView(view: View) {
        mViewBinding = SettingFragmentBinding.bind(view)
        initView()
    }

    override fun onBindViewModel(): Class<SettingViewModel> {
        return SettingViewModel::class.java;
    }



}