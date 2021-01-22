package com.jinn.projecty.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;

import com.jinn.projecty.utils.LogUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

/**
 * Created by jinnlee on 2021/1/20.
 */
public abstract class BaseFragment <VM extends BaseViewModel>extends Fragment {
    protected VM mViewModel;
    private ViewStub mViewStubLoading = null;
    private ViewStub mViewStubError = null;
    private View mLoadingView;
    private View mErrorView;
    private static final String TAG ="BaseFragment" ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createViewModel();
    }

    private void createViewModel(){
        mViewModel = ViewModelProviders.of(this,onBindViewModelFactory()).get(onBindViewModel());
    }

    protected abstract ViewModelProvider.Factory onBindViewModelFactory();

    protected abstract int getLayoutId();

    protected abstract void initView(View view);

    protected abstract Class<VM> onBindViewModel();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView  = inflater.inflate(R.layout.base_fragment_layout,container ,false);
        initBaseLayout(contentView);
        initBaseObservable();
        return contentView;
    }

    private void initBaseLayout(View contentView){
        FrameLayout contentLayout = contentView.findViewById(R.id.base_frag_content);
        contentLayout.removeAllViews();

        mViewStubLoading = (ViewStub)contentView.findViewById(R.id.base_frag_loading_conent);
        View view = LayoutInflater.from(getActivity()).inflate(getLayoutId(),contentLayout,false);
        contentLayout.addView(view);
        initView(view);
    }

    /**
     * 监听页面状态变化
     */
    private void initBaseObservable(){
         mViewModel.getLiveDataShowLoading().observe(this, new Observer<Boolean>() {
             @Override
             public void onChanged(Boolean showLoading) {
                 LogUtils.d(TAG,"showLoading,"+showLoading);
                 if(mLoadingView ==null){
                     mLoadingView = mViewStubLoading.inflate();
                 }
                 mLoadingView.setVisibility(showLoading?View.VISIBLE:View.GONE);
             }
         });

         mViewModel.getLiveDataShowError().observe(this, new Observer<Boolean>() {
             @Override
             public void onChanged(Boolean showError) {
                 LogUtils.d(TAG,"showError,"+showError);
                 if(mErrorView==null){
                     mErrorView = mViewStubError.inflate();
                 }
                 mErrorView.setVisibility(showError?View.VISIBLE:View.GONE);
             }
         });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
