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
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
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
import com.jinn.projecty.main.manager.WebViewPreloadHelper;
import com.jinn.projecty.main.model.NewsLandingViewModel;
import com.jinn.projecty.main.model.ViewModelFactory;
import com.jinn.projecty.main.ui.widget.CustomWebView;
import com.jinn.projecty.utils.LogUtils;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProvider;

import java.io.IOException;

public class NewsLandingActivity extends BaseFragmentActivity<NewsLandingViewModel> {

    private String mImgUrl;
    private Context mContext;
    private String mVideoUrl;
    private String mNewsUrl;
    private FrameLayout mWebContainerLayout;
    private CustomWebView mWebView;
    private final String TAG ="NewsLandingActivity";


    @Override
    protected ViewModelProvider.Factory onBindViewModelFactory() {
        return ViewModelFactory.getInstance(getApplication());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        mContext = this;
        Intent intent = getIntent();
        mVideoUrl = intent.getExtras().getString("video_url");
        mNewsUrl = intent.getExtras().getString("news_url");
        LogUtils.d(TAG,"onCreate,mNewsUrl:"+mNewsUrl);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.news_detail_activity;
    }

    @Override
    protected void initView(View view) {
        mWebContainerLayout = view.findViewById(R.id.web_container);
        mWebView = WebViewPreloadHelper.INSTANCE.acquireWebView(this);
        mWebContainerLayout.addView(mWebView, FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);
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
            LogUtils.d(TAG,"shouldOverrideUrlLoading,"+request.getUrl());
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            LogUtils.d(TAG,"onPageStarted");
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            LogUtils.d(TAG,"onPageFinished");
            //注入一段js，监控webview加载性能
            mWebView.evaluateJavascript("javascript:if(window.performance){window.performance.timing.toJSON()}", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                     LogUtils.d(TAG,"onReceiveValue:"+value);
                }
            });
            super.onPageFinished(view, url);
        }
    };

    private WebChromeClient webChromeClient = new WebChromeClient(){
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            LogUtils.d(TAG,"onProgressChanged,"+newProgress);
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            LogUtils.d(TAG,"onReceivedTitle,"+title);
            super.onReceivedTitle(view, title);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            LogUtils.d(TAG,"onJsConfirm,"+ message);
            return super.onJsConfirm(view, url, message, result);
        }
    };


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebViewPreloadHelper.INSTANCE.prepareWebView();
    }

    @Override
    protected Class<NewsLandingViewModel> onBindViewModel() {
        return NewsLandingViewModel.class;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



}
