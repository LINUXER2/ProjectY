package com.jinn.projecty.main.ui;

import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jinn.projecty.main.R;
import com.jinn.projecty.main.constant.Constant;
import com.jinn.projecty.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    private List<NewsFragment>mFragments = null;
    private ViewPager2 mViewPager=null;
    private final String TAG ="MainFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        LogUtils.d(TAG,"onCreateView");
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtils.d(TAG,"onViewCreated");
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        initView(view);
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        LogUtils.d(TAG,"onHiddenChanged,"+hidden);
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onResume() {
        LogUtils.d(TAG,"onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        LogUtils.d(TAG,"onPause");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        LogUtils.d(TAG,"onDestroy");
        super.onDestroy();
    }

    private void initView(View view){
        mViewPager = view.findViewById(R.id.main_view_pager);
        mFragments = new ArrayList<NewsFragment>();

        // 在主线程空闲时提前加载后面的fragment
        Looper.getMainLooper().getQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                LogUtils.d(TAG,"OnIdleHandler,initFragment");
                for(int i=0;i< Constant.tabList.size();i++){
                    Bundle bundle = new Bundle();
                    bundle.putString("tab_name",Constant.tabList.get(i));
                    mFragments.add(NewsFragment.newInstance(bundle));
                }
                mViewPager.setOffscreenPageLimit(1);   // 提前加载下1个fragment
                return false;
            }
        });

        mViewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                LogUtils.d(TAG,"createFragment,"+position);
                if(mFragments==null || mFragments.size()<=position){
                    Bundle bundle = new Bundle();
                    bundle.putString("tab_name",Constant.tabList.get(position));
                    Fragment fragment = NewsFragment.newInstance(bundle);
                    return fragment;
                }
                return mFragments.get(position);
            }

            @Override
            public int getItemCount() {
                return mFragments.size();
            }
        });
    }
}