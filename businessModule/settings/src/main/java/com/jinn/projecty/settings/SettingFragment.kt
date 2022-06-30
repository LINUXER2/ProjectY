package com.jinn.projecty.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jinn.projecty.frameapi.service.IMainAppService
import com.jinn.projecty.frameapi.service.ServiceManager
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
            val intent = Intent()
            intent.setClassName("com.jinn.projecty","com.jinn.projecty.main.ui.NewsLandingActivity")
            intent.putExtra("img_url", "https://image.baidu.com/search/detail?z=0&word=%E5%9F%8E%E5%B8%82%E5%BB%BA%E7%AD%91%E6%91%84%E5%BD%B1%E4%B8%93%E9%A2%98&hs=0&pn=0&spn=0&di=&pi=3977&tn=baiduimagedetail&is=&ie=utf-8&oe=utf-8&cs=1595072465%2C3644073269&os=&simid=&adpicid=0&lpn=0&fr=albumsdetail&fm=&ic=0&sme=&cg=&bdtype=&oriquery=&objurl=https%3A%2F%2Ft7.baidu.com%2Fit%2Fu%3D1595072465%2C3644073269%26fm%3D193%26f%3DGIF&fromurl=ipprf_z2C%24qAzdH3FAzdH3Fooo_z%26e3Bev2_z%26e3Bv54AzdH3Fv6jwptejAzdH3Fb88cc0c0a&gsm=0&islist=&querylist=&album_tab=%E5%BB%BA%E7%AD%91&album_id=7")
            intent.putExtra("video_url", "")
            intent.putExtra("news_url", "")
            ServiceManager.getInstance().getService<IMainAppService>(ServiceManager.SERVICE_MAIN_APP).startActivity(intent,context)
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