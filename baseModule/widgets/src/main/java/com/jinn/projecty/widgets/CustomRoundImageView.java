package com.jinn.projecty.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.jinn.projecty.utils.LogUtils;


/**
 * 可自定义圆角的imageView
 * Created by jinnlee on 2019/6/6.
 */

public class CustomRoundImageView extends ImageView {

    private static final String TAG = "CustomRoundImageView";
    private int defaultRadius = 0;
    private int radius;
    private int leftTopRadius;
    private int rightTopRadius;
    private int rightBottomRadius;
    private int leftBottomRadius;
    private Path mPath = new Path();
    private Paint mPaint = new Paint();
    private RectF mRectF = new RectF();
    private float[] mRadii = new float[8];

    public CustomRoundImageView(Context context) {
        this(context, null);
    }

    public CustomRoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomRoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.custom_round_iv);
        radius = array.getDimensionPixelOffset(R.styleable.custom_round_iv_radius_all, defaultRadius);
        leftTopRadius = array.getDimensionPixelOffset(R.styleable.custom_round_iv_radius_left_top, defaultRadius);
        rightTopRadius = array.getDimensionPixelOffset(R.styleable.custom_round_iv_radius_right_top, defaultRadius);
        rightBottomRadius = array.getDimensionPixelOffset(R.styleable.custom_round_iv_radius_right_bottom, defaultRadius);
        leftBottomRadius = array.getDimensionPixelOffset(R.styleable.custom_round_iv_radius_left_bottom, defaultRadius);

        if (defaultRadius == leftTopRadius) {
            leftTopRadius = radius;
        }
        if (defaultRadius == rightTopRadius) {
            rightTopRadius = radius;
        }
        if (defaultRadius == rightBottomRadius) {
            rightBottomRadius = radius;
        }
        if (defaultRadius == leftBottomRadius) {
            leftBottomRadius = radius;
        }
        array.recycle();

        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mPath.setFillType(Path.FillType.INVERSE_EVEN_ODD);
    }


    private void buildPath() {
        mPath.reset();
        mRectF.set(0, 0, getWidth(), getHeight());
        mRadii[0] = mRadii[1] = leftTopRadius;
        mRadii[2] = mRadii[3] = rightTopRadius;
        mRadii[4] = mRadii[5] = rightBottomRadius;
        mRadii[6] = mRadii[7] = leftBottomRadius;

        mPath.addRoundRect(mRectF, mRadii, Path.Direction.CW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        LogUtils.d(TAG,"onDraw:"+leftTopRadius+","+rightTopRadius);
        int maxLeft = Math.max(leftTopRadius, leftBottomRadius);
        int maxRight = Math.max(rightTopRadius, rightBottomRadius);
        int minWidth = maxLeft + maxRight;
        int maxTop = Math.max(leftTopRadius, rightTopRadius);
        int maxBottom = Math.max(leftBottomRadius, rightBottomRadius);
        int minHeight = maxTop + maxBottom;
        if (getWidth() >= minWidth && getHeight() > minHeight) {
            int layerId = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
            try {
                super.onDraw(canvas);
            }catch (Exception e){
                LogUtils.e(TAG, "error drawing:");
            }
            buildPath();
            canvas.drawPath(mPath, mPaint);
            canvas.restoreToCount(layerId);
        } else {
            try {
                super.onDraw(canvas);
            }catch (Exception e){
                LogUtils.e(TAG, "error drawing:");
            }
        }
    }

    public void setCornerRadius(int leftTopRadius, int rightTopRadius, int rightBottomRadius, int leftBottomRadius) {
        this.leftBottomRadius = leftBottomRadius;
        this.leftTopRadius = leftTopRadius;
        this.rightBottomRadius = rightBottomRadius;
        this.rightTopRadius = rightTopRadius;
        invalidate();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        if (bm == null) {
            return;
        }
        if (!bm.isRecycled()) {
            super.setImageBitmap(bm);
        } else {
            LogUtils.e(TAG, "try to set a recycled bitmap to ImageView");
        }
    }
}