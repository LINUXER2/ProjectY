package com.jinn.projecty.main.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinn.projecty.main.R;
import com.jinn.projecty.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinnlee on 2021/1/27.
 */
public class TabLayout extends LinearLayout {
    private Context mContext;
    private List<String> mTabs = new ArrayList<>();
    private OnTabClickListener mTabListener;
    private final String TAG = "TabLayout";

    public TabLayout(Context context) {
        super(context);
        mContext = context;
    }

    public TabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        this.setBackgroundColor(getContext().getResources().getColor(R.color.main_tab_bg_color, null));
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LogUtils.d(TAG,"onMeasure,widthMeasureSpec:"+widthMeasureSpec+",heightMeasureSpec:"+heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        for(int i =0;i< getChildCount();i++){
            View child = getChildAt(i);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)child.getLayoutParams();
            params.width  = getMeasuredWidth()/getChildCount();
            params.gravity = Gravity.CENTER;
            child.setLayoutParams(params);
        }
        LogUtils.d(TAG, "onLayout,changed:" + changed + ",l=" + l + ",t=" + t + ",r=" + r + ",b=" + b);
    }

    public void setTabContent(List<String>titles){
        if(titles!=null){
            mTabs.clear();
            mTabs.addAll(titles);
        }
        for(int i=0;i<mTabs.size();i++){
            String tab = mTabs.get(i);
            View view = generateTabView(tab);
            this.addView(view);
            view.setTag(i);
            view.setOnClickListener(mOnClickListener);
        }
    }

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
             if(mTabListener!=null){
                 mTabListener.onTabClicked((int)v.getTag());
             }
        }
    };

    private View generateTabView(String text){
        TextView textView = new TextView(mContext);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    public void setTabClickListener(OnTabClickListener listener){
        this.mTabListener = listener;
    }

    public interface OnTabClickListener{
        public void onTabClicked(int index);
    }
}
