package com.jinn.projecty.main.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.jinn.projecty.utils.LogUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by jinnlee on 2022/4/18.
 */

public class MainRecycleView extends RecyclerView{
    private final String TAG = "MainRecycleView";
    public MainRecycleView(@NonNull Context context) {
        super(context);
    }

    public MainRecycleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MainRecycleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        //LogUtils.d(TAG,"onScrolled,dy="+dy);
        super.onScrolled(dx, dy);
    }

    @Override
    public void onScrollStateChanged(int state) {
        LogUtils.d(TAG,"onScrollStateChanged,"+state);
        switch (state){
            case RecyclerView.SCROLL_STATE_IDLE:
                int firstPos = ((LinearLayoutManager)getLayoutManager()).findFirstVisibleItemPosition();
                int firstCompleteVisiblePos =((LinearLayoutManager)getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                int lastPos=((LinearLayoutManager)getLayoutManager()).findLastVisibleItemPosition();
                int lastCompleteVisiblePos=((LinearLayoutManager)getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int childCount = getLayoutManager().getChildCount();
                int itemCount = getLayoutManager().getItemCount();
                LogUtils.d(TAG,"firstPos:"+firstPos+",firstCompleteVisiblePos:"+firstCompleteVisiblePos+",lastPos:"+lastPos+
                        ",lastCompleteVisiblePos:"+lastCompleteVisiblePos+",itemCount:"+itemCount);
                if(lastPos>= itemCount-1){
                    LogUtils.d(TAG,"reach bottom");
                }
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING:
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                break;
        }
        super.onScrollStateChanged(state);
    }
}
