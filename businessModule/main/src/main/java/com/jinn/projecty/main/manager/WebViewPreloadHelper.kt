package com.jinn.projecty.main.manager

import android.content.Context
import android.content.MutableContextWrapper
import android.os.Looper
import com.jinn.projecty.frameapi.base.BaseApplication
import com.jinn.projecty.main.ui.widget.CustomWebView
import com.jinn.projecty.utils.LogUtils
import java.util.*

/**
 * webview 预加载 ：https://juejin.cn/post/7016883220025180191
 *  1. 通过 MutableContextWrapper 解决预热的webiew context不是activity的问题
 *  2。通过 IdleHandler 在线程空闲时预热weview
 */
object WebViewPreloadHelper {
    private const val TAG = "WebViewPreloadHelper"
    private val webviewPool = Stack<CustomWebView>()
    private const val MAX_CACHE_SIZE = 3

    fun prepareWebView(){
        LogUtils.d(TAG,"prepareWebView,stack size:${webviewPool.size}")
        if(webviewPool.size< MAX_CACHE_SIZE){
            Looper.getMainLooper().queue.addIdleHandler {
                if(webviewPool.size< MAX_CACHE_SIZE){
                    webviewPool.push(createWebView(MutableContextWrapper(BaseApplication.sInstance)))
                }
                false
            }
        }
    }

    fun acquireWebView(context: Context):CustomWebView{
        LogUtils.d(TAG,"acquireWebView,stack size:${webviewPool.size}")
        if(webviewPool.isEmpty()){
            LogUtils.d(TAG,"createWebView")
            return createWebView(context)
        }
        val webView = webviewPool.pop()
        val contextWrapper = webView.context as MutableContextWrapper
        contextWrapper.baseContext = context
        return webView
    }

    private fun createWebView(context:Context):CustomWebView{
        return CustomWebView(context)
    }
}