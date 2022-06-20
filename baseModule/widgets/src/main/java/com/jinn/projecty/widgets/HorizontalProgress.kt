package com.jinn.projecty.widgets

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.LinearInterpolator
import com.jinn.projecty.utils.LogUtils

/**
 *  自定义view实现进度条
 *  https://juejin.cn/post/6844903481329860615
 *  https://github.com/lygttpod/AndroidCustomView/blob/master/app/src/main/java/com/allen/androidcustomview/widget/HorizontalProgressBar.java
 */
class HorizontalProgress @JvmOverloads constructor(context: Context, attr:AttributeSet?=null,defAttr:Int =0):View(context,attr,defAttr) {

    companion object{
        const val TAG = "HorizontalProgress"
    }

    private var bgPaint: Paint? = null
    private var progressPaint: Paint? = null

    private var tipPaint: Paint? = null
    private var textPaint: Paint? = null

    private var mWidth = 0
    private var mHeight = 0
    private var mViewHeight = 0

    /**
     * 最大进度
     */
    private var mMaxProgress = 100f

    /**
     * 当前进度(距离)
     */
    private var currentProgress = 0f

    /**
     * 进度动画
     */
    private var progressAnimator: ValueAnimator? = null

    /**
     * 动画执行时间
     */
    private val duration = 1000

    /**
     * 动画延时启动时间
     */
    private val startDelay = 500

    /**
     * 进度条画笔的宽度
     */
    private var progressPaintWidth = 0

    /**
     * 百分比提示框画笔的宽度
     */
    private var tipPaintWidth = 0

    /**
     * 百分比提示框的高度
     */
    private var tipHeight = 0

    /**
     * 百分比提示框的宽度
     */
    private var tipWidth = 0

    /**
     * 画三角形的path
     */
    private val path = Path()

    /**
     * 三角形的高
     */
    private var triangleHeight = 0

    /**
     * 进度条距离提示框的高度
     */
    private var progressMarginTop = 0

    /**
     * 进度移动的距离
     */
    private var moveDis = 0f

    private val textRect = Rect()
    private var textString = "0"

    /**
     * 百分比文字字体大小
     */
    private var textPaintSize = 0

    /**
     * 进度条背景颜色
     */
    private val bgColor = -0x1e1a18

    /**
     * 进度条颜色
     */
    private val progressColor = -0x994ee

    /**
     * 绘制提示框的矩形
     */
    private val rectF = RectF()

    /**
     * 圆角矩形的圆角半径
     */
    private var roundRectRadius = 0


    init {
        progressPaintWidth = dp2px(4)
        tipHeight = dp2px(15)
        tipWidth = dp2px(30)
        tipPaintWidth = dp2px(1)
        triangleHeight = dp2px(3)
        roundRectRadius = dp2px(2)
        textPaintSize = dp2px(10)
        progressMarginTop = dp2px(8)
        //view真实的高度
        mViewHeight = tipHeight + tipPaintWidth + triangleHeight + progressPaintWidth + progressMarginTop

        bgPaint = getPaint(progressPaintWidth,bgColor,Paint.Style.STROKE)
        progressPaint = getPaint(progressPaintWidth,progressColor,Paint.Style.STROKE)
        tipPaint = getPaint(tipPaintWidth,progressColor,Paint.Style.FILL)
        textPaint = Paint()
        textPaint!!.textSize = textPaintSize.toFloat()
        textPaint!!.color = Color.WHITE
        textPaint!!.isAntiAlias = true
        textPaint!!.textAlign = Paint.Align.CENTER

    }

    private fun getPaint(strokeWidth: Int, color: Int, style: Paint.Style): Paint {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.strokeWidth = strokeWidth.toFloat()
        paint.color = color
        paint.isAntiAlias = true
        paint.strokeCap = Paint.Cap.ROUND
        paint.style = style
        return paint
    }

    public fun setMaxProgress(maxProgress:Float):HorizontalProgress{
        mMaxProgress = maxProgress
        initAnimation()
        return this
    }

    private fun initAnimation(){
        progressAnimator =ValueAnimator.ofFloat(0f,mMaxProgress)
        progressAnimator?.let {
            it.duration = duration.toLong()
            it.startDelay = startDelay.toLong()
            it.interpolator = LinearInterpolator()
            it.addUpdateListener {
                var value =  it.animatedValue as Float
                textString = value.toInt().toString()
                currentProgress = value*mWidth/mMaxProgress
                if((currentProgress>=tipWidth/2)&&currentProgress<=(mWidth-tipWidth/2)){
                    moveDis = currentProgress - tipWidth/2
                }
               invalidate()
            }
        }
    }

    public fun startProgressAnim(){
        if(progressAnimator?.isRunning == false){
            LogUtils.d(TAG,"startProgressAnim")
            progressAnimator?.start()
        }
    }

    public fun stopProgressAnim(){
        LogUtils.d(TAG,"endProgressAnim")
        progressAnimator?.end()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //绘制灰色背景
        canvas?.drawLine(paddingLeft.toFloat(),tipHeight+progressMarginTop.toFloat(),width.toFloat(),tipHeight+progressMarginTop.toFloat(),bgPaint!!)
        //绘制进度条
        canvas?.drawLine(paddingLeft.toFloat(),tipHeight+progressMarginTop.toFloat(),currentProgress,tipHeight+progressMarginTop.toFloat(),progressPaint!!)
        //绘制提示圆角矩形
        rectF.set(moveDis,0f,tipWidth+moveDis,tipHeight.toFloat())
        canvas?.drawRoundRect(rectF,roundRectRadius.toFloat(),roundRectRadius.toFloat(),tipPaint!!)
        //绘制提示小三角
        path.moveTo(moveDis+tipWidth/2-triangleHeight,tipHeight.toFloat())
        path.lineTo(moveDis+tipWidth/2,tipHeight.toFloat()+triangleHeight)
        path.lineTo(moveDis+tipWidth/2+triangleHeight,tipHeight.toFloat())
        canvas?.drawPath(path,tipPaint!!)
        path.reset()
        //绘制文本
        textRect.left = moveDis.toInt()
        textRect.top = 0
        textRect.right = tipWidth+moveDis.toInt()
        textRect.bottom = tipHeight
        val fontMetrics:Paint.FontMetricsInt = textPaint!!.fontMetricsInt
        val baseLine = (textRect.bottom-fontMetrics.bottom+textRect.top-fontMetrics.top)/2
        canvas?.drawText("$textString%", textRect.centerX().toFloat(), baseLine.toFloat(),textPaint!!)
    }



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
         super.onMeasure(widthMeasureSpec, heightMeasureSpec)
         mWidth = measuredWidth
         mHeight = measuredHeight
         val widthMode = MeasureSpec.getMode(widthMeasureSpec)
         val width = MeasureSpec.getSize(widthMode)
         val heightMode = MeasureSpec.getMode(heightMeasureSpec)
         val height  = MeasureSpec.getSize(heightMode)
         //setMeasuredDimension(measureWidth(widthMode,width),measureHeight(heightMode,height))
    }


    private fun measureWidth(mode:Int,width:Int):Int{
        when(mode){
            MeasureSpec.UNSPECIFIED,
            MeasureSpec.AT_MOST->{
                mWidth = 1080
            }
            MeasureSpec.EXACTLY->{
                mWidth = width
            }

        }
        mWidth = 1080
        LogUtils.d(TAG,"getMeasuredWidth,mode:${mode},width:${width},measureWidth:$mWidth")
        return mWidth
    }

    private fun measureHeight(mode:Int,height: Int):Int{
        when(mode){
            MeasureSpec.UNSPECIFIED,
            MeasureSpec.AT_MOST->{
                mHeight = mViewHeight
            }
            MeasureSpec.EXACTLY->{
                mHeight = height
            }
        }
        LogUtils.d(TAG,"getMeasuredHeight,mode:${mode},height:${height},measureHeight:$mHeight")
        return mHeight
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

     private fun dp2px(dpVal: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpVal.toFloat(), resources.displayMetrics
        ).toInt()
    }


}