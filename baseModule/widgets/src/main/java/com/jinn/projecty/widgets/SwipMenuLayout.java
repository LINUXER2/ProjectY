package com.jinn.projecty.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.jinn.projecty.utils.LogUtils;

import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.regex.Matcher;


/**
 *  一个实现横滑展示更多的父布局
 * https://github.com/anzaizai/EasySwipeMenuLayout
 */
public class SwipMenuLayout extends FrameLayout {

    private int mScaledTouchSlop = 0;
    private Scroller mScroller;
    private int mLeftViewResID;
    private int mRightViewResID;
    private int mContentViewResID;
    private View mLeftView;
    private View mRightView;
    private View mContentView;
    private boolean mCanLeftSwipe = true;  // 是否支持左边侧滑
    private boolean mCanRightSwipe = true; // 是否支持右边侧滑
    private float mFraction = 0.3f;  //滑动阈值
    private boolean isSwiping = false;
    private PointF mLastP;
    private PointF mFirstP;
    private float finalyDistanceX;
    private static State mStateCache;

    public enum State {
        LEFTOPEN,
        RIGHTOPEN,
        CLOSE,
    }

    private final String TAG = "SwipMenuLayout";
    public SwipMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SwipMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public SwipMenuLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs,defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs,int defStyleAttr){
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        mScaledTouchSlop = viewConfiguration.getScaledTouchSlop();
        mScroller = new Scroller(context);

        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.SwipMenuLayout);
        int indexCount = ta.getIndexCount();
        for(int i=0;i<indexCount;i++){
            int attr = ta.getIndex(i);
            if(attr == R.styleable.SwipMenuLayout_leftMenuView){
               mLeftViewResID = ta.getResourceId(R.styleable.SwipMenuLayout_leftMenuView,-1);
            }else if(attr ==R.styleable.SwipMenuLayout_contentView){
                mContentViewResID = ta.getResourceId(R.styleable.SwipMenuLayout_contentView,-1);
            }else if(attr == R.styleable.SwipMenuLayout_rightMenuView){
                mRightViewResID = ta.getResourceId(R.styleable.SwipMenuLayout_rightMenuView,-1);
            }else if(attr == R.styleable.SwipMenuLayout_canLeftSwipe){
                mCanLeftSwipe = ta.getBoolean(R.styleable.SwipMenuLayout_canLeftSwipe,false);
            }else if(attr == R.styleable.SwipMenuLayout_canRightSwipe){
                mCanRightSwipe = ta.getBoolean(R.styleable.SwipMenuLayout_canRightSwipe,false);
            }else if(attr == R.styleable.SwipMenuLayout_fraction){
                mFraction = ta.getFloat(R.styleable.SwipMenuLayout_fraction,0.5f);
            }
        }
        ta.recycle();
        LogUtils.d(TAG,"init attrs,canLeftSwip:"+mCanLeftSwipe+",leftViewRes:"+mLeftViewResID);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
          int count = getChildCount();
          int left = getPaddingLeft();
          int right = getPaddingRight();
          int top = getPaddingTop();
          int bottpm = getPaddingBottom();

          for(int i=0;i<count;i++){
              View child = getChildAt(i);
              if(mLeftView==null && child.getId()==mLeftViewResID){
                  // 找到左边可侧滑的布局
                  mLeftView = child;
                  mLeftView.setClickable(true);
              }
              else if(mRightView == null && child.getId()==mRightViewResID){
                  // 找到右侧可侧滑的布局
                  mRightView = child;
                  mRightView.setClickable(true);
              }else if(mContentView==null && child.getId()==mContentViewResID){
                  mContentView = child;
                  mContentView.setClickable(true);
              }
          }
        MarginLayoutParams contentViewLp=null;
          if(mContentView!=null){
              contentViewLp= (MarginLayoutParams)mContentView.getLayoutParams();
              int contentTop = top+contentViewLp.topMargin;
              int contentLeft = left + contentViewLp.leftMargin;
              int contentRight = left+contentViewLp.leftMargin+mContentView.getMeasuredWidth();
              int contentBottom = contentTop+mContentView.getMeasuredHeight();
              mContentView.layout(contentLeft,contentTop,contentRight,contentBottom);
          }

          if(mLeftView!=null){
              MarginLayoutParams leftViewLp = (MarginLayoutParams)mLeftView.getLayoutParams();
              int lTop = top +  leftViewLp.topMargin;
              int lLeft = 0 - mLeftView.getMeasuredWidth() + leftViewLp.leftMargin + leftViewLp.rightMargin;
              int lRight = 0 - leftViewLp.rightMargin;
              int lBottom  = lTop + mLeftView.getMeasuredHeight();
              mLeftView.layout(lLeft,lTop,lRight,lBottom);
          }

          if(mRightView!=null && contentViewLp!=null){
              MarginLayoutParams rightViewLp = (MarginLayoutParams)mRightView.getLayoutParams();
              int rLeft = mContentView.getRight()+ contentViewLp.rightMargin + rightViewLp.leftMargin;
              int rRight = rLeft + mRightView.getMeasuredWidth();
              int rTop = top + rightViewLp.topMargin;
              int rBottom = rTop+ mRightView.getMeasuredHeight();
              mRightView.layout(rLeft,rTop,rRight,rBottom);
          }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                isSwiping = false;
                if(mLastP==null){
                    mLastP = new PointF();
                }
                mLastP.set(ev.getRawX(),ev.getRawY());
                if(mFirstP==null){
                    mFirstP = new PointF();
                }
                mFirstP.set(ev.getRawX(),ev.getRawY());
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                float distanceX = mLastP.x - ev.getRawX();
                float distanceY = mLastP.y - ev.getRawY();
                if(Math.abs(distanceY)>mScaledTouchSlop && Math.abs(distanceY)>Math.abs(distanceX)){
                    break;
                }

                scrollBy((int)distanceX,0);
                 // 越界糾正
                if(getScrollX()<0){
                    if(!mCanRightSwipe || mLeftView==null){
                        scrollTo(0,0);
                    }else{
                        if(getScrollX()<mLeftView.getLeft()){  //左滑过界
                            scrollTo(mLeftView.getLeft(),0);
                        }
                    }
                }else if(getScrollX()>0){
                    if(!mCanLeftSwipe || mRightView==null){
                        scrollTo(0,0);
                    }else{
                        if(getScrollX()>mRightView.getRight() - mContentView.getRight()){
                            scrollTo(mRightView.getRight() - mContentView.getRight(),0);
                        }
                    }
                }

                // 当左右滑动时，阻止父类事件拦截
                if(Math.abs(distanceX)>mScaledTouchSlop){
                     getParent().requestDisallowInterceptTouchEvent(true);
                }
                mLastP.set(ev.getRawX(),ev.getRawY());
                break;
            case MotionEvent.ACTION_UP:
                finalyDistanceX = mFirstP.x-ev.getRawX();
                if(Math.abs(finalyDistanceX)>mScaledTouchSlop){
                    isSwiping = true;
                }
                State state = getCurrentState(getScrollX());
                handlerSwipeMenu(state);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if(Math.abs(finalyDistanceX)>mScaledTouchSlop){
                    //当手指拖动值大于mScaledTouchSlop值时，认为应该进行滚动，拦截子控件的事件
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if(isSwiping){
                    isSwiping = false;
                    finalyDistanceX = 0;
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            invalidate();
        }
    }

    private void handlerSwipeMenu(State state) {
        if (state == State.LEFTOPEN) {
            mScroller.startScroll(getScrollX(), 0, mLeftView.getLeft() - getScrollX(), 0);
            mStateCache = state;
        } else if (state == State.RIGHTOPEN) {
            mScroller.startScroll(getScrollX(), 0, mRightView.getRight() - mContentView.getRight() - getScrollX(), 0);
            mStateCache = state;
        } else {
            mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0);
            mStateCache = null;
        }
        invalidate();
    }


    /**
     *  根据当前scrollY判断松手后应处于什么状态
     * @param scrollX
     * @return
     */
    private State getCurrentState(int scrollX) {
        if (!(mScaledTouchSlop < Math.abs(finalyDistanceX))) {
            return mStateCache;
        }
        LogUtils.i(TAG, ">>>finalyDistanceX:" + finalyDistanceX);
        if (finalyDistanceX < 0) {
            //➡滑动
            //1、展开左边按钮
            //获得leftView的测量长度
            if (getScrollX() < 0 && mLeftView != null) {
                if (Math.abs(mLeftView.getWidth() * mFraction) < Math.abs(getScrollX())) {
                    return State.LEFTOPEN;
                }
            }
            //2、关闭右边按钮

            if (getScrollX() > 0 && mRightView != null) {
                return State.CLOSE;
            }
        } else if (finalyDistanceX > 0) {
            //⬅️滑动
            //3、开启右边菜单按钮
            if (getScrollX() > 0 && mRightView != null) {

                if (Math.abs(mRightView.getWidth() * mFraction) < Math.abs(getScrollX())) {
                    return State.RIGHTOPEN;
                }

            }
            //关闭左边
            if (getScrollX() < 0 && mLeftView != null) {
                return State.CLOSE;
            }
        }

        return State.CLOSE;

    }
}
