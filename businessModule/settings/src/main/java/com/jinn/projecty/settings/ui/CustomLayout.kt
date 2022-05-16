package com.jinn.projecty.settings.ui

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout
import com.jinn.projecty.utils.LogUtils

/**
 * ViewGroup事件分发：https://www.jianshu.com/p/38015afcdb58
 */
class CustomLayout @JvmOverloads constructor(context: Context, attr: AttributeSet?=null, defAttr:Int = 0):LinearLayout(context,attr,defAttr){

    companion object{
        const val TAG = "CustomLayout"
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
         LogUtils.d(TAG,"viewGroup:onLayout:$changed,$l,$t,$r,$b")
         super.onLayout(changed, l, t, r, b)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        LogUtils.d(TAG,"=============viewGroup=============:onTouchEvent,${event?.action}")
        when(event?.action){
            MotionEvent.ACTION_DOWN->{

            }
            MotionEvent.ACTION_MOVE->{

            }
            MotionEvent.ACTION_UP->{

            }else ->{

            }
        }
        return super.onTouchEvent(event)
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        val ret = super.dispatchTouchEvent(event)
        LogUtils.d(TAG,"=============viewGroup=============:dispatchTouchEvent:${event?.action},ret:$ret")
        return ret
    }

    /**
     * 是否拦截事件，返回true，拦截事件，不再向下分法，调用view本身的onTouchEvent
     *            范回false，不拦截事件，分发给子view的dispatchTouchEvent
     */
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        //LogUtils.d(TAG,"=============viewGroup=============:onInterceptTouchEvent:${ev?.action}")
        return super.onInterceptTouchEvent(ev)
    }
}