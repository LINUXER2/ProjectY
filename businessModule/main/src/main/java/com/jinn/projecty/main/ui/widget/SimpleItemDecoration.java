package com.jinn.projecty.main.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.jinn.projecty.main.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 *
 * Created by jinnlee on 2022/4/19.
 */

public class SimpleItemDecoration extends RecyclerView.ItemDecoration {
    private Context mContext;
    private Paint mDividerPaint;
    private int mDividerHeight;
    private int mDividerLineHeight;


    public SimpleItemDecoration(Context context) {
        mContext = context;
        mDividerPaint = new Paint();
        mDividerPaint.setColor(Color.GRAY);
        mDividerHeight = mContext.getResources().getDimensionPixelOffset(R.dimen.recycle_view_divider_height);
        mDividerLineHeight = mContext.getResources().getDimensionPixelOffset(R.dimen.recycle_view_divider_line_height);
    }


    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < childCount - 1; i++) {
            View view = parent.getChildAt(i);
            float top = view.getBottom()+mDividerHeight/2;
            float bottom = top+mDividerLineHeight;
            c.drawRect(left, top, right, bottom, mDividerPaint);
        }
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    /**
     *  实现类似padding的效果
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = mDividerHeight;  //设置两个item的间距
    }
}
