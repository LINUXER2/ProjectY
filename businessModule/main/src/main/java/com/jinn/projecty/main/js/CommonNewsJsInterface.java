package com.jinn.projecty.main.js;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.jinn.projecty.main.constant.Constant;
import com.jinn.projecty.main.ui.NewsLandingActivity;
import com.jinn.projecty.utils.LogUtils;

import org.json.JSONObject;

public class CommonNewsJsInterface {

    private NewsLandingActivity mActivity;
    private WebView mWebView;
    private final String TAG = "CommonNewsJsInterface";
    private static final String FUN_JUMP_BROWSER = "jumpBrowser";
    private static final String FUN_SHOW_COMMENT = "showComment";


    public CommonNewsJsInterface(WebView webView) {
        mWebView = webView;
    }

    public void attachActivity(NewsLandingActivity activity){
        mActivity = activity;
    }

    public void detachActivity(){
        mActivity = null;
    }

    /**
     *  暴露给前端的接口
     * @param name
     * @param params
     * @param callBackId
     */
    @JavascriptInterface
    public void invokeLocal(String name,String params,String callBackId){
         switch (name){
             case FUN_JUMP_BROWSER:
                 //TODO
                 JSONObject json1 = new JSONObject();
                 callBackToJs(callBackId,json1.toString());
                 break;
             case FUN_SHOW_COMMENT:
                 //TODO
                 JSONObject json2 = new JSONObject();
                 callBackToJs(callBackId,json2.toString());
                 break;
             default:
                 break;
         }
    }


    /**
     * 回传给前端的数据
     * @param callBackId：前端定义的方法名
     * @param result：回调结果，json串
     */
    private void callBackToJs(String callBackId,String result){
        if(TextUtils.isEmpty(callBackId)||TextUtils.isEmpty(result)){
            return;
        }
        if(mActivity!=null){
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:$callbackID('$callbackStr')");
                }
            });
        }
    }


    /**
     *  調用前端提供的方法,不會刷新頁面
     */
    public void notifyJs(String funName){
        mWebView.evaluateJavascript("javascript:$funName", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                LogUtils.d(TAG,"onReceiveValue:"+value);
            }
        });
    }

}
