package com.jinn.projecty.main.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
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

import org.jetbrains.annotations.NotNull;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    private List<NewsFragment>mFragments = null;
    private ViewPager2 mViewPager=null;
    private TabLayout mTabLayout = null;
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
        mTabLayout = view.findViewById(R.id.main_tab);

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

        mViewPager.setAdapter(new VPStateAdapter(this));

        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
              //  LogUtils.d(TAG,"onPageScrolled,position:"+position);
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                LogUtils.d(TAG,"onPageSelected,position:"+position+",tabCount"+mTabLayout.getTabCount());
                super.onPageSelected(position);

                //设置选中状态效果
                int tabCount = mTabLayout.getTabCount();
                for (int i=0;i<tabCount;i++){
                    TabLayout.Tab tab = mTabLayout.getTabAt(i);
                    TextView textView = (TextView) tab.getCustomView();
                    if(tab.getPosition()==position){
                        textView.setTypeface(Typeface.DEFAULT_BOLD);
                    }else{
                        textView.setTypeface(Typeface.DEFAULT);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
              //  LogUtils.d(TAG,"onPageScrollStateChanged,state:"+state);
                super.onPageScrollStateChanged(state);
            }
        });

        new TabLayoutMediator(mTabLayout, mViewPager,true,true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {
                LogUtils.d(TAG,"onConfigureTab:"+position);
                TextView textView = new TextView(MainFragment.this.getActivity());
                tab.setCustomView(textView);
                textView.setText("频道"+position);
                textView.setTextColor(getResources().getColor(R.color.design_default_color_error));
            }
        }).attach();
    }

    class VPStateAdapter extends FragmentStateAdapter{
        public VPStateAdapter(@NonNull @NotNull Fragment fragment) {
            super(fragment);
        }

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
            return Constant.tabList.size();
        }
    }
}