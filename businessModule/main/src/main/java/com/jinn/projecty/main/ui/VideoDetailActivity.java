package com.jinn.projecty.main.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jinn.projecty.base.BaseFragmentActivity;
import com.jinn.projecty.main.R;
import com.jinn.projecty.main.model.VideoDetailViewModel;
import com.jinn.projecty.main.model.ViewModelFactory;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProvider;

public class VideoDetailActivity extends BaseFragmentActivity<VideoDetailViewModel> {

    private ImageView mImageView;
    private String mImgUrl;
    private Context mContext;
    @Override
    protected ViewModelProvider.Factory onBindViewModelFactory() {
        return ViewModelFactory.getInstance(getApplication());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        super.onCreate(savedInstanceState);
        initEnterAnim();
        mContext = this;
        Intent intent = getIntent();
        mImgUrl = intent.getExtras().getString("url");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.video_detail_activity;
    }

    @Override
    protected void initView(View view) {
        mImageView = findViewById(R.id.image_view);
    }

    @Override
    protected Class<VideoDetailViewModel> onBindViewModel() {
        return VideoDetailViewModel.class;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Glide.with(mContext).load(mImgUrl).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                startAnim();
                return true;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                mImageView.setImageDrawable(resource);
                startAnim();
                return true;
            }
        }).into(mImageView);
    }

    private void initEnterAnim(){
        postponeEnterTransition();    //暂时阻止启动共享元素 Transition，与startPostponedEnterTransition搭配使用
                                      // why？Transitions 必须捕获目标 View 的起始和结束状态来构建合适的动画。因此，如果框架在共享元素获得它在调用它的 App 所给定的大小和位置前启动共享元素的过渡动画，这个 Transition 将不能
                                      // 正确捕获到共享元素的结束状态值,生成动画也会失败 https://blog.csdn.net/k_tiiime/article/details/44702681
        ViewCompat.setTransitionName(mImageView, "IMG_TRANSITION");   //设置transitionName
    }

    private void startAnim(){
        startPostponedEnterTransition();   //恢复动画，在共享元素测量和布局完毕后调用
    }

}
