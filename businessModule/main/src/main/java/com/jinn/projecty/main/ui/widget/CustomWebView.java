package com.jinn.projecty.main.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.jinn.projecty.main.BuildConfig;

public class CustomWebView extends WebView {
    public CustomWebView(Context context) {
        this(context,null);
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDefaultConfig();
    }



    private void initDefaultConfig(){
        if(BuildConfig.DEBUG){
            WebView.setWebContentsDebuggingEnabled(true);
        }
        this.setBackgroundColor(Color.WHITE);
        this.setLayerType(View.LAYER_TYPE_NONE,null);

        WebSettings webSettings = this.getSettings();
        //设置缓存
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCachePath(getContext().getCacheDir().getAbsolutePath());
        getSettings().setDatabaseEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//设置使用默认的cache加载模式
        //启用地理定位
        webSettings.setGeolocationEnabled(true);
        webSettings.setDomStorageEnabled(true);//设置可以使用localStorage
        webSettings.setDefaultTextEncodingName("UTF-8");//设置默认的网页编码
        webSettings.setBuiltInZoomControls(false);//不显示缩放按钮

        webSettings.setLoadWithOverviewMode(true);// 缩放至屏幕的大小
        webSettings.setUseWideViewPort(true);//设置支持html的viewport属性
        webSettings.setJavaScriptEnabled(true);//允许js
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);  // 支持通过JS打开新窗口
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);//设置高渲染优先级
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        //设置页面布局，把所有内容放大webview等宽的一列中
        webSettings.setLoadsImagesAutomatically(true);//允许加载图片
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        //MIXED_CONTENT_ALWAYS_ALLOW：允许从任何来源加载内容，即使起源是不安全的；
        // MIXED_CONTENT_NEVER_ALLOW：不允许Https加载Http的内容，即不允许从安全的起源去加载一个不安全的资源；
        // MIXED_CONTENT_COMPATIBILITY_MODE：当涉及到混合式内容时，WebView 会尝试去兼容最新Web浏览器的风格。
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setAllowFileAccess(false);
        webSettings.setTextZoom(100);
        //设置h5内嵌的视频自动播放
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        setDrawingCacheEnabled(true);

    }
}
