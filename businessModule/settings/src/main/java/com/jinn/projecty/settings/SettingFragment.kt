package com.jinn.projecty.settings

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelStore
import com.jinn.projecty.settings.databinding.SettingFragmentBinding
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
       val button = view.findViewById<Button>(R.id.button1)
        button.setOnClickListener {
            launch(Dispatchers.IO) {
                LogUtils.d(TAG,"insert");
                viewModel.insertData()
            }
        }
    }

    @DelicateCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[SettingViewModel::class.java]
        viewModel.getStudentDataFromDb(viewLifecycleOwner)
        viewModel.getStudentLiveData().observe(viewLifecycleOwner,{
             LogUtils.d(TAG,"getStudentLiveData,size:${it.size}")
        })

    }

}