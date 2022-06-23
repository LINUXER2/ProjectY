package com.jinn.projecty.settings.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.jinn.projecty.settings.R
import com.jinn.projecty.utils.LogUtils
import com.jinn.projecty.utils.SystemPropertyUtils

class CustomView @JvmOverloads constructor(context:Context,attr:AttributeSet?=null,defAttr:Int = 0):View(context,attr,defAttr){
    companion object{
        const val TAG = "CustomView"
    }

    private val paint = Paint()
    private val mBitmap1 by lazy {
        val optios = BitmapFactory.Options()
        optios.inSampleSize = 1  //设置采样率
        val tempBitmap = BitmapFactory.decodeResource(context.resources,R.drawable.dog,optios)
        getByteCount(tempBitmap)
        val scaledBitmap = Bitmap.createScaledBitmap(tempBitmap,SystemPropertyUtils.getScreenWidth(context),tempBitmap.height*SystemPropertyUtils.getScreenWidth(context)/tempBitmap.width,false)
        if(tempBitmap?.hashCode()!=scaledBitmap?.hashCode()&& !tempBitmap.isRecycled){
            tempBitmap.recycle()
        }
        getByteCount(scaledBitmap)
        scaledBitmap
    }

    private val mBitmap2 by lazy {
        val tempBitmap = BitmapFactory.decodeResource(context.resources,R.drawable.cat)
        val scaledBitmap = Bitmap.createScaledBitmap(tempBitmap,SystemPropertyUtils.getScreenWidth(context),tempBitmap.height*SystemPropertyUtils.getScreenWidth(context)/tempBitmap.width,false)
        if(tempBitmap?.hashCode()!=scaledBitmap?.hashCode()&& !tempBitmap.isRecycled){
            tempBitmap.recycle()
        }
        scaledBitmap
    }

    init{
        paint.color=Color.GREEN
        paint.style = Paint.Style.STROKE  // 設置绘制模式（线条）
        paint.strokeWidth = 5f   //设置线条宽度
        paint.isAntiAlias = true // 设置抗锯齿
    }


    override fun onFinishInflate() {
        super.onFinishInflate()
    }

    /**
     * https://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2016/0116/3874.html
     * bitmap 实际占用的内存大小，与三个因素有关：
     * 1.色彩格式，如果是 ARGB8888 那么就是一个像素4个字节，如果是 RGB565 那就是2个字节
     * 2.原始文件存放的资源目录（是 hdpi 还是 xxhdpi ）
     * 3.目标屏幕的密度（所以同等条件下，红米在资源方面消耗的内存肯定是要小于三星S6的）
     * total = bitmap.rowBytes*bitmap.height
     * 一张522*686的PNG 图片，我把它放到 drawable-xxhdpi 目录下，在三星s6上加载，占用内存2547360B，其中 density 对应 xxhdpi 为480，targetDensity 对应三星s6的密度为640：
     *  522/480 * 640 * 686/480 *640 * 4 = 2546432B
     */
    private fun getByteCount(bitmap :Bitmap){
        val rowBytes = bitmap.rowBytes
        val height = bitmap.height
        val width = bitmap.width
        val total1 = rowBytes*height/1024
        val targetDensityDpi = context.resources.displayMetrics.densityDpi
        val scale = targetDensityDpi/480
        val total2 = width*scale* height*scale*4/1024    // 放在xxhdpi下，資源density =480，如果是drawable，则是160
        LogUtils.d(TAG,"getByteCount,densityDpi:$targetDensityDpi,rowBytes:$rowBytes,width:$width,height:$height,total1:$total1 KB ,total2=$total2 KB")
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        LogUtils.d(TAG,"onDraw")
        val shader1 = BitmapShader(mBitmap1,Shader.TileMode.CLAMP,Shader.TileMode.CLAMP)
        val shader2 = BitmapShader(mBitmap2,Shader.TileMode.CLAMP,Shader.TileMode.CLAMP)
        val composShader = ComposeShader(shader1,shader2,PorterDuff.Mode.SRC_OVER)      // 混合着色器，实际就是shader2将以什么样的方式绘制到shader1上
        paint.shader = composShader
        paint.style=Paint.Style.FILL
        canvas?.drawRoundRect(0f,0f, mBitmap1.width.toFloat(), mBitmap1.height.toFloat(),20f,20f,paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        LogUtils.d(TAG,"childview:onMeasure")
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        LogUtils.d(TAG,"childview:onLayout,$changed,$l,$t,$r,$b")
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        LogUtils.d(TAG,"=============childview=============:onTouchEvent:${event?.action}")
        when(event?.action){
            MotionEvent.ACTION_DOWN->{

            }
            MotionEvent.ACTION_MOVE->{

            }
            MotionEvent.ACTION_UP->{

            }else ->{

        }
        }
        // 只要设置了clickable,super方法返回的一定是true，代表事件已被消费
        return super.onTouchEvent(event)
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        val ret = super.dispatchTouchEvent(event)
        LogUtils.d(TAG,"=============childview=============:dispatchTouchEvent:${event?.action},ret:$ret")
        return ret
    }



    private fun drawBasicViews(canvas: Canvas?){
        canvas?.drawColor(Color.GRAY)     // 绘制背景

        canvas?.drawCircle(200f,200f,100f,paint)   //绘制圆

        canvas?.drawRect(Rect(400,100,800,300),paint)   //绘制方形

        paint.strokeCap=Paint.Cap.SQUARE    //设置点的形状
        canvas?.drawPoint(1000f,200f,paint)     // 绘制点

        canvas?.drawOval(RectF(100f,600f,500f,800f),paint)  //绘制椭圆（只能绘制横着的或者竖着的椭圆，参数为四个点）

        canvas?.drawLine(600f,600f,800f,800f,paint)   //绘制线条
        val points = floatArrayOf(100f,1000f,300f,1000f,100f,1100f,300f,1100f)
        canvas?.drawLines(points,paint)    //绘制线条组，每两个點一组

        val path = Path()
        //Path 有两类方法，一类是直接描述路径的，另一类是辅助的设置或计算。
        // 第一类，addXXX ,添加完整的封闭图形
        path.addCircle(200f,1400f,100f,Path.Direction.CW)   // CW，顺时针
        path.addCircle(300f,1400f,100f,Path.Direction.CW)   // CW，顺时针
        canvas?.drawPath(path,paint)

        // 辅助计算xxxTo,画线
        path.moveTo(500f,1300f)
        path.lineTo(700f,1500f)  //由当前位置向（100,100）画一条直线，当前位置为最后一次path的终点位置，初始为（0,0）
        path.rLineTo(100f,0f)  //由当前位置向由绘制100个像素
        path.close()                   //封閉当前图形，等价于path.lineTo(500,1300)
        canvas?.drawPath(path,paint)

        path.moveTo(100f,1700f)
        path.quadTo(300f,1750f,400f,2000f)  // 绘制二次贝塞尔曲线，前面两个为控制点，后两个为终点
        canvas?.drawPath(path,paint)

        // Path 第二类，辅助设置
        path.fillType =Path.FillType.EVEN_ODD        //设置填充方式，当两个图形交汇时填充算法(交叉填充)，默認為全填充（WINDING）
        path.addCircle(600f,1800f,100f,Path.Direction.CW)   // CW，顺时针
        path.addCircle(700f,1800f,100f,Path.Direction.CW)   // CW，顺时针
        canvas?.drawPath(path,paint)
    }
}