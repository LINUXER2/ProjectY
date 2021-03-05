package com.jinn.projecty.main.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
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
import androidx.appcompat.app.AppCompatActivity;
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
        super.onCreate(savedInstanceState);
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
        initEnterAnim();
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
        postponeEnterTransition();    //先停止动画
        ViewCompat.setTransitionName(mImageView, "IMG_TRANSITION");   //设置transitionName
    }

    private void startAnim(){
        startPostponedEnterTransition();   //开始动画
    }

}
