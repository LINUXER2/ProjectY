package com.jinn.projecty.settings.ktx

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 *  viewModel 扩展函数
 */
fun ViewModel.launch(block: suspend CoroutineScope.() -> Unit){
    viewModelScope.launch {
        block()
    }
}


/**
 * View 扩展函数
 */


/**
 *  context 扩展函数
 */
fun Context.dp2px(dipValue:Float):Int{
    val density = this.resources.displayMetrics.density
    return (dipValue* density+0.5f).toInt()
}

fun View.px2dp(pxValue:Float):Int{
    val density = this.resources.displayMetrics.density
    return (pxValue / density + 0.5f).toInt()
}

fun Context.showToast(str:String):Toast{
    val toast =Toast.makeText(this,str,Toast.LENGTH_SHORT)
    toast.show()
    return toast
}