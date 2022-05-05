package com.jinn.projecty.main.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jinn.projecty.base.BaseFragmentActivity;
import com.jinn.projecty.main.R;
import com.jinn.projecty.main.js.CommonNewsJsInterface;
import com.jinn.projecty.main.model.NewsLandingViewModel;
import com.jinn.projecty.main.model.ViewModelFactory;
import com.jinn.projecty.main.ui.widget.CustomWebView;
import com.jinn.projecty.utils.LogUtils;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProvider;

import java.io.IOException;

public class NewsLandingActivity extends BaseFragmentActivity<NewsLandingViewModel> {

    private ImageView mImageView;
    private String mImgUrl;
    private Context mContext;
    private String mVideoUrl;
    private String mNewsUrl;
    private SurfaceView mVideoView;
    private SeekBar mSeekBar;
    private MediaPlayer mMediaPlayer;
    private Button mPlayButton;
    private Button mPauseButton;
    private CustomWebView mWebView;
    private final String TAG ="NewsLandingActivity";
    private final String TAG_WEB ="NewsLandingActivity_WEB";


    @Override
    protected ViewModelProvider.Factory onBindViewModelFactory() {
        return ViewModelFactory.getInstance(getApplication());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        mContext = this;
        Intent intent = getIntent();
        mImgUrl = intent.getExtras().getString("img_url");
        mVideoUrl = intent.getExtras().getString("video_url");
        mNewsUrl = intent.getExtras().getString("news_url");
        LogUtils.d(TAG,"onCreate:img:"+mImgUrl+",video:"+mVideoUrl);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.news_detail_activity;
    }

    @Override
    protected void initView(View view) {
        mImageView = findViewById(R.id.image_view);
        mVideoView = findViewById(R.id.media_player);
        mSeekBar = findViewById(R.id.seek_bar);
        mPlayButton = findViewById(R.id.play);
        mPauseButton = findViewById(R.id.pause);
        mWebView = findViewById(R.id.webview);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });
        mPauseButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                pause();
            }
        });
        initEnterAnim();
        initPlayer();
        loadWebView();
    }

    private void loadWebView(){
        mWebView.addJavascriptInterface(new CommonNewsJsInterface(mWebView),"CommonJs");
        mWebView.loadUrl(mNewsUrl);
        mWebView.setWebChromeClient(webChromeClient);
        mWebView.setWebViewClient(webViewClient);
    }

    private WebViewClient webViewClient = new WebViewClient(){
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            LogUtils.d(TAG_WEB,"shouldOverrideUrlLoading,"+request.getUrl());
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            LogUtils.d(TAG_WEB,"onPageStarted");
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            LogUtils.d(TAG_WEB,"onPageFinished");
            //注入一段js，监控webview加载性能
            mWebView.evaluateJavascript("javascript:if(window.performance){window.performance.timing.toJSON()}", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                     LogUtils.d(TAG_WEB,"onReceiveValue:"+value);
                }
            });
            super.onPageFinished(view, url);
        }
    };

    private WebChromeClient webChromeClient = new WebChromeClient(){
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            LogUtils.d(TAG_WEB,"onProgressChanged,"+newProgress);
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            LogUtils.d(TAG_WEB,"onReceivedTitle,"+title);
            super.onReceivedTitle(view, title);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            LogUtils.d(TAG_WEB,"onJsConfirm,"+ message);
            return super.onJsConfirm(view, url, message, result);
        }
    };


    private void initPlayer(){
        if(mMediaPlayer==null){
            mMediaPlayer = new MediaPlayer();
            AudioAttributes attr = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
                    .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                    .build();
            mMediaPlayer.setAudioAttributes(attr);
        }
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(mVideoUrl);
            mMediaPlayer.prepareAsync();
        }catch (IOException e){
            LogUtils.e(TAG,"play error,"+e.toString());
        }
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                LogUtils.d(TAG,"onPrepared:");
                play();
            }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                LogUtils.d(TAG,"onCompletion:");
            }
        });
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                LogUtils.d(TAG,"onError:"+what+","+extra);
                return false;
            }
        });
        mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                LogUtils.d(TAG,"onBufferingUpdate:"+percent);
            }
        });
        mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                LogUtils.d(TAG,"onSeekComplete:");
            }
        });
    }

    private void releasePlayer(){
        if(mMediaPlayer!=null){
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer=null;
        }
    }

    private void play(){
        if(mMediaPlayer.isPlaying()){
            return;
        }
        mMediaPlayer.setDisplay(mVideoView.getHolder());
        mMediaPlayer.start();
        int duration = mMediaPlayer.getDuration();
        LogUtils.d(TAG,"play media,duration:"+duration);
        mSeekBar.setMax(duration);
        mImageView.setVisibility(View.GONE);
    }

    private void pause(){
        mMediaPlayer.pause();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    protected Class<NewsLandingViewModel> onBindViewModel() {
        return NewsLandingViewModel.class;
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
