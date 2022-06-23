package com.jinn.projecty.utils

import android.graphics.*
import android.graphics.drawable.Drawable


/**
 *  bitmap圆角处理等工具类
 *  https://www.jianshu.com/p/d59d0deab15b
 *  https://www.jianshu.com/p/7482147a1f97
 *  https://blog.csdn.net/xiaohanluo/article/details/52945791
 */
object ImageUtils {


    /**
     * 圆角处理方式1：使用bitmapShader实现bitmap缩放和圆角处理
     * @param oriBitmap : 原始bitmap
     * @param _targetWidth: 目标imageView的宽
     * @param _targetHeight: 目标imageView的高
     * @param radius : 圓角大小
     * @return bitmap: 返回圆角图片
     */
    fun toRoundBitmapWithShader(oriBitmap:Bitmap, _targetWidth:Int, _targetHeight:Int, radius:Float):Bitmap {
        var targetWidth = _targetWidth
        var targetHeight = _targetHeight
        if(targetWidth<=0||targetHeight<=0){
            targetWidth = oriBitmap.width
            targetHeight = oriBitmap.height
        }
        val widthScale = targetWidth*1.0f/oriBitmap.width
        val heightScale = targetHeight*1.0f/oriBitmap.height
        val matrix = Matrix()
        matrix.setScale(widthScale, heightScale)

        // 初始化绘制纹理图,也可以用缩放后的bitmap
        val bitmapShader = BitmapShader(oriBitmap,Shader.TileMode.CLAMP,Shader.TileMode.CLAMP)
        // 根据控件大小对纹理图进行拉伸缩放处理
        bitmapShader.setLocalMatrix(matrix)
        // 初始化目标bitmap
        val tarBitmap = Bitmap.createBitmap(targetWidth,targetHeight,Bitmap.Config.ARGB_8888)
        // 初始化目标画布
        val canvas = Canvas(tarBitmap)

        // 初始化画笔
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = bitmapShader

        // 利用画笔将纹理图绘制到画布上面
        canvas.drawRoundRect(RectF(0f,0f,targetWidth.toFloat(),targetHeight.toFloat()),radius,radius,paint)

        if(!oriBitmap.isRecycled){
            oriBitmap.recycle()
        }
        LogUtils.d("jinn2","getBitmapSize,${tarBitmap.byteCount},${tarBitmap.width},${tarBitmap.height}")
        return tarBitmap
    }


    /**
     *  圆角处理方式2:：利用Xfermode
     *  先绘制一个圆角矩形，后绘制原图（or原图缩放后的bitmap），二者叠加
     *  */
    fun toRoundBitmapWithXferm(oriBitmap:Bitmap, _targetWidth:Int, _targetHeight:Int, radius:Float):Bitmap {
        var targetWidth = _targetWidth
        var targetHeight = _targetHeight
        if(targetWidth<=0||targetHeight<=0){
            targetWidth = oriBitmap.width
            targetHeight = oriBitmap.height
        }
        val widthScale = targetWidth*1.0f/oriBitmap.width
        val heightScale = targetHeight*1.0f/oriBitmap.height
        val matrix = Matrix()
        matrix.setScale(widthScale, heightScale)

      //  val scaledBitmap = Bitmap.createScaledBitmap(oriBitmap,targetWidth,targetHeight,true)
        val scaledBitmap = Bitmap.createBitmap(oriBitmap,0,0,oriBitmap.width,oriBitmap.height,matrix,true)

        // 初始化目标bitmap
        val tarBitmap = Bitmap.createBitmap(targetWidth,targetHeight,Bitmap.Config.ARGB_8888)
        // 初始化目标画布
        val canvas = Canvas(tarBitmap)
        // 初始化画笔
        val paint = Paint()
        paint.isAntiAlias = true
        paint.color=Color.CYAN
        canvas.drawRoundRect(0f,0f,targetWidth.toFloat(),targetHeight.toFloat(),radius,radius,paint)

        //设置叠加模式
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)  //显示上层交集部分
        //在画布上绘制原图
        val rect = Rect(0,0,targetWidth,targetHeight)
        canvas.drawBitmap(scaledBitmap,rect,rect,paint)

        if(!oriBitmap.isRecycled){
            oriBitmap.recycle()
        }
        if(!scaledBitmap.isRecycled){
            scaledBitmap.recycle()
        }
        return tarBitmap
    }


    /**
     * 將drawable转换为bitmap
     * @param drawable
     * @return bitmap
     */
    fun drawableToBitmap(drawable:Drawable):Bitmap?{
        val width = drawable.intrinsicWidth
        val height= drawable.intrinsicHeight
         if(width==0||height ==0){
             return null
         }
        val op =BitmapFactory.Options()
        op.inJustDecodeBounds = true
        val bitmap =Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0,0,width,height)
        drawable.draw(canvas)
        return bitmap
    }
}