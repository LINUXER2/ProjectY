package com.jinn.projecty.settings.ktx

import android.content.Context
import android.graphics.Rect
import android.view.MotionEvent
import android.view.TouchDelegate
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinn.projecty.frameapi.base.BaseApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// viewModel 扩展函数
fun ViewModel.launch(block: suspend CoroutineScope.() -> Unit) {
    viewModelScope.launch {
        block()
    }
}


// View 扩展函数
/**
 * View 扩大View点击区域，需要保证目标targetView有父View
 */
fun View.expandTouchView(expandSize: Int = 10) {
    val parentView = (parent as? View)
    parentView?.post {
        val rect = Rect()
        getHitRect(rect)
        rect.left -= expandSize
        rect.right += expandSize
        rect.top -= expandSize
        rect.bottom += expandSize
        parentView.touchDelegate = TouchDelegate(rect, this)
    }
}

// Int 扩展函数
val Int.resToString: String
    get() = BaseApplication.sInstance.getString(this)


// context 扩展函数
fun Context.dp2px(dipValue: Float): Int {
    val density = this.resources.displayMetrics.density
    return (dipValue * density + 0.5f).toInt()
}

fun Context.px2dp(pxValue: Float): Int {
    val density = this.resources.displayMetrics.density
    return (pxValue / density + 0.5f).toInt()
}

fun Context.showToast(str: String): Toast {
    val toast = Toast.makeText(this, str, Toast.LENGTH_SHORT)
    toast.show()
    return toast
}