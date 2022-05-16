package com.jinn.projecty.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jinn.projecty.settings.client.AidlClientManager
import com.jinn.projecty.settings.client.MessengerClientManager
import com.jinn.projecty.settings.databinding.SettingFragmentBinding
import com.jinn.projecty.settings.ui.CustomViewActivity
import com.jinn.projecty.utils.LogUtils
import kotlinx.coroutines.*

class SettingFragment : Fragment() ,CoroutineScope by MainScope(){

    companion object {
        fun newInstance() = SettingFragment()
        private const val TAG = "SettingFragment"
    }

    private lateinit var viewModel: SettingViewModel
    private lateinit var mViewBinding:SettingFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewBinding = SettingFragmentBinding.inflate(layoutInflater,container,false)
        return mViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    @DelicateCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[SettingViewModel::class.java]
    }


    private fun initView() {
        mViewBinding.button1.setOnClickListener {
            launch(Dispatchers.IO) {
                LogUtils.d(TAG, "insert");
                viewModel.insertData()
            }
        }


        mViewBinding.button2.setOnClickListener {
            viewModel.queryAll().observe(viewLifecycleOwner,{
                LogUtils.d(TAG,"getStudentLiveData,size:${it.size}")
            })
        }

        mViewBinding.button3.setOnClickListener{
            activity?.let { it1 -> AidlClientManager.bindService(it1) }
        }

        mViewBinding.button4.setOnClickListener{
            activity?.let { it1 ->
                MessengerClientManager.getInstance(it1).sendMessage()
            }
        }

        mViewBinding.button5.setOnClickListener{
            activity?.let { it1 ->
                val intent = Intent()
                intent.setClass(it1,CustomViewActivity::class.java)
                it1.startActivity(intent)
            }
        }
    }


}