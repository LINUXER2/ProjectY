package com.jinn.projecty.main.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jinn.projecty.base.BaseFragment;
import com.jinn.projecty.main.R;
import com.jinn.projecty.main.adapter.RecommandAdapter;
import com.jinn.projecty.main.bean.RecommandDataBean;
import com.jinn.projecty.main.model.ViewModelFactory;
import com.jinn.projecty.main.model.MainViewModel;
import com.jinn.projecty.utils.LogUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainFragment extends BaseFragment<MainViewModel> {

    private RecyclerView mRecycleView;
    private SmartRefreshLayout mRefreshLayout;
    private final String TAG = "MainFragment";
    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.main_fragment;
    }

    @Override
    protected void initView(View view) {
        mRecycleView = view.findViewById(R.id.main_recycle_view);
      //  mRefreshLayout = view.findViewById(R.id.main_refresh_layout);
        mViewModel.getLiveDataShowLoading().postValue(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecommandAdapter adapter = new RecommandAdapter(getActivity());
        mRecycleView.setAdapter(adapter);
        mViewModel.getVideoLists().observe(this, new Observer<RecommandDataBean>() {
            @Override
            public void onChanged(RecommandDataBean bean) {
                LogUtils.d(TAG,"onChanged,"+bean.getNextPageUrl());
                adapter.initData(bean);

            }
        });
        mViewModel.requestMainData();
    }

    @Override
    protected Class onBindViewModel() {
        return MainViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory onBindViewModelFactory() {
        return ViewModelFactory.getInstance(getActivity().getApplication());
    }
}
