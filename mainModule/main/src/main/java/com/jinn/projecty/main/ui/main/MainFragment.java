package com.jinn.projecty.main.ui.main;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jinn.projecty.base.BaseFragment;
import com.jinn.projecty.main.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

public class MainFragment extends BaseFragment<MainViewModel> {

    private TextView mTextView;
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
        mTextView = view.findViewById(R.id.message);
        mViewModel.getLiveDataShowLoading().postValue(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mViewModel.getLiveDataShowLoading().postValue(false);
            }
        },2000);
    }

    @Override
    protected Class onBindViewModel() {
        return MainViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory onBindViewModelFactory() {
        return new MainViewModelFactory(getActivity().getApplication());
    }
}
