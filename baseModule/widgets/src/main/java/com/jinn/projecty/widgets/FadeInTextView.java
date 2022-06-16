package com.jinn.projecty.widgets;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.transition.Fade;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

/**
 *  实现一个打印机效果的textView
 *  思路：1. 重绘文字
 *       2.利用属性动画动态改变文字内容
 *
 */
public class FadeInTextView extends TextView {
    private TextAnimListener mTextAnimListener;
    private String []attr;  // 拆分后的字符串数组
    private StringBuffer mStringBuffer = new StringBuffer();  //每一帧绘制的字符串
    private ValueAnimator mAnim = null;
    private int mDuration = 300;  //每个文字出现的时间,默认300ms
    private int mCurrentIndex = -1;
    private Rect textRect = new Rect();


    public FadeInTextView(Context context) {
        super(context);
    }

    public FadeInTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FadeInTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
         if(mStringBuffer!=null){
             drawText(canvas,mStringBuffer.toString());
         }
    }

    private void drawText(Canvas canvas,String str){
         textRect.left = getPaddingLeft();
         textRect.top = getPaddingTop();
         textRect.right = getWidth() - getPaddingRight();
         textRect.bottom = getHeight() - getPaddingBottom();
         Paint.FontMetricsInt fontMetricsInt = getPaint().getFontMetricsInt();
         int baseLine = (textRect.bottom+textRect.top - fontMetricsInt.bottom-fontMetricsInt.top)/2;  // why?
         canvas.drawText(str,getPaddingLeft(),baseLine,getPaint());
    }

    public void startAnim(){
        if(mAnim ==null){
            initAnimation();
        }
        if(mAnim.isRunning()){
            return;
        }
        mStringBuffer.setLength(0);  //重置srringBuffer和index
        mCurrentIndex=-1;
        mAnim.start();

    }

    public void stopAnim(){
          if(mAnim!=null){
              mAnim.end();
          }
    }

    private void initAnimation(){
         mAnim = ValueAnimator.ofInt(0,attr.length-1);
         mAnim.setDuration((attr.length-1)*mDuration);
         mAnim.setInterpolator(new LinearInterpolator());
         mAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
             @Override
             public void onAnimationUpdate(ValueAnimator animation) {
                 int index = (int)animation.getAnimatedValue();
                 if(mCurrentIndex!=index){   //去重，每个字只绘制一次
                    mStringBuffer.append(attr[index]);
                    mCurrentIndex = index;
                    if(mCurrentIndex==attr.length-1 && mTextAnimListener!=null){   //绘制完成后回调
                        mTextAnimListener.onAnimFinished();
                    }
                    invalidate(); // 或者直接调用setText也可以，不需要重写onDraw
                 }
             }
         });
    }


    public final class Builder {
        private String[] attr;
        private TextAnimListener textAnimListener;
        private int duration;

        public Builder setTextString(String text) {
            String[] array = new String[text.length()];
            for (int i = 0; i < text.length(); i++) {
                array[i] = text.substring(i, i + 1);
            }
            attr = array;
            return this;
        }

        public Builder setTextAnimListener(TextAnimListener listener) {
            textAnimListener = listener;
            return this;
        }

        public Builder setDuration(int dura){
            duration = dura;
            return this;
        }

        public FadeInTextView build() {
            FadeInTextView.this.attr = this.attr;
            FadeInTextView.this.mDuration = this.duration;
            FadeInTextView.this.mTextAnimListener = this.textAnimListener;
            return FadeInTextView.this;
        }

    }

    public interface TextAnimListener{
        void onAnimFinished();
    }


}
