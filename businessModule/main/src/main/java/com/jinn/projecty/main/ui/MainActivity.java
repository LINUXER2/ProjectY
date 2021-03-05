package com.jinn.projecty.main.ui;

import android.os.Bundle;
import android.view.Window;

import com.jinn.projecty.main.R;
import com.jinn.projecty.main.ui.widget.TabLayout;
import com.jinn.projecty.utils.LogUtils;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
        initViews();
    }

    private void initViews(){
        mTabLayout =(TabLayout)this.findViewById(R.id.tab_layout);
        ArrayList<String>tabs = new ArrayList<>();
        tabs.add("每日精选");
        tabs.add("发现");
        tabs.add("热门");
        tabs.add("我的");
        mTabLayout.setTabContent(tabs);
        mTabLayout.setTabClickListener(new TabLayout.OnTabClickListener(){
            @Override
            public void onTabClicked(int index) {
                LogUtils.d(TAG,"onTabClicked,"+index);
                //TODO
            }
        });

    }
}
