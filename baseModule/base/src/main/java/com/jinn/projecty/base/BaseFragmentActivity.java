package com.jinn.projecty.base;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;

import com.jinn.projecty.utils.LogUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

/**
 * Created by jinnlee on 2021/1/20.
 */
public abstract class BaseFragmentActivity<VM extends BaseViewModel> extends FragmentActivity {

    private VM mViewModel;
    private ViewStub mViewStubLoading = null;
    private ViewStub mViewStubError = null;
    private View mLoadingView;
    private View mErrorView;
    private static final String TAG ="BaseFragmentActivity" ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_layout);
        initContentView();
        createViewModel();
        initBaseObservable();
    }

    private void createViewModel(){
        mViewModel = new ViewModelProvider(this,onBindViewModelFactory()).get(onBindViewModel());
    }

    private void initContentView(){
        FrameLayout contentLayout = findViewById(R.id.base_content);
        contentLayout.removeAllViews();
        View view = LayoutInflater.from(this).inflate(getLayoutId(),contentLayout,false);
        contentLayout.addView(view);
        mViewStubLoading =findViewById(R.id.base_loading_conent);
        mViewStubError = findViewById(R.id.base_error_conent);
    }

    private void initBaseObservable(){
        mViewModel.getLiveDataShowLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean showLoading) {
                LogUtils.d(TAG,"LiveDataShowLoading changed:"+showLoading);
            }
        });

        mViewModel.getLiveDataShowError().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean showError) {
                LogUtils.d(TAG,"LiveDataShowError changed:"+showError);
            }
        });

    }

    protected abstract ViewModelProvider.Factory onBindViewModelFactory();

    protected abstract int getLayoutId();

    protected abstract void initView(View view);

    protected abstract Class<VM> onBindViewModel();

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
