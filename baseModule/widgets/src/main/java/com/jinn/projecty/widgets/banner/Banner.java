package com.jinn.projecty.widgets.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager2.widget.ViewPager2;


import com.bumptech.glide.Glide;
import com.jinn.projecty.utils.LogUtils;
import com.jinn.projecty.widgets.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class Banner <T>extends FrameLayout{

    private String TAG = "Banner";
    protected int cornerRadius;
    private int indicatorMargin = IDefaultBannerConfig.PADDING_SIZE;
    private int indicatorWidth;
    private int indicatorHeight;
    private int indicatorSize;
    private int bannerBackgroundImage;
    private BannerStyle bannerStyle = BannerStyle.CIRCLE_INDICATOR;
    private int delayTime = IDefaultBannerConfig.TIME;
    private int scrollTime = IDefaultBannerConfig.DURATION;
    private boolean isAutoPlay = IDefaultBannerConfig.IS_AUTO_PLAY;
    private boolean isScroll = IDefaultBannerConfig.IS_SCROLL;
    private int indicatorSelectedResId = IDefaultBannerConfig.DEFAULT_SELECTED_INDICATOR_DRAWABLE_ID;
    private int indicatorUnselectedResId = IDefaultBannerConfig.DEFAULT_UNSELECTED_INDICATOR_DRAWABLE_ID;
    private int layoutResId = R.layout.banner;
    private int titleHeight;
    private int titleBackground;
    private int titleTextColor;
    private int titleTextSize;

    protected int count = 0;
    protected int currentItem;
    private int gravity = -1;
    private int lastPosition = 1;
    private List<String> titles;
    private List<String> imageUrls;
    private List<ImageView> indicatorImages; //indicator对应的imageView lists
    protected Context context;

    protected ViewPager2 viewPager;
    private ViewGroup bannerContainer;
    protected TextView bannerTitle, numIndicatorInside, numIndicator;
    protected LinearLayout indicator, indicatorInside, titleView;
    protected ImageView bannerDefaultImage;
    private BannerPagerAdapter bannerPagerAdapter;
    private ViewPager2.OnPageChangeCallback onPageChangeListener;
    protected BannerScroller scroller;

    private DisplayMetrics dm;
    private boolean isAutoPlaying = false;
    private long actionUpTime = -1;
    private int lastRepPosition = -1;//上次上报的位置，埋点用

    private WeakHandler handler;
    protected List<T> bannerList = new ArrayList<>();
    protected Context glideImgContex;

    private IBannerExposeListener bannerExposeListener;
    private IBannerClickListener bannerClickListener;

    public Banner(Context context) {
        this(context, null);
    }

    public Banner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        titles = new ArrayList<>();
        imageUrls = new ArrayList<>();
        indicatorImages = new ArrayList<>();
        handler = new WeakHandler(context.getMainLooper());
        dm = context.getResources().getDisplayMetrics();
        indicatorSize = dm.widthPixels / 80;
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        handleTypedArray(context, attrs);
        View view = LayoutInflater.from(context).inflate(layoutResId, this, true);
        bannerContainer = view.findViewById(R.id.bannerContainer);
        bannerDefaultImage = view.findViewById(R.id.bannerDefaultImage);
        viewPager = view.findViewById(R.id.bannerViewPager);
        titleView = view.findViewById(R.id.titleView);
        indicator = view.findViewById(R.id.circleIndicator);
        indicatorInside = view.findViewById(R.id.indicatorInside);
        bannerTitle = view.findViewById(R.id.bannerTitle);
        numIndicator = view.findViewById(R.id.numIndicator);
        numIndicatorInside = view.findViewById(R.id.numIndicatorInside);
        bannerDefaultImage.setImageResource(bannerBackgroundImage);
        initViewPagerScroll();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    private void handleTypedArray(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Banner);
        indicatorWidth = typedArray.getDimensionPixelSize(R.styleable.Banner_indicator_width, indicatorSize);
        indicatorHeight = typedArray.getDimensionPixelSize(R.styleable.Banner_indicator_height, indicatorSize);
        indicatorMargin = typedArray.getDimensionPixelSize(R.styleable.Banner_indicator_margin, 0);
        indicatorSelectedResId = typedArray.getResourceId(R.styleable.Banner_indicator_drawable_selected, IDefaultBannerConfig.DEFAULT_SELECTED_INDICATOR_DRAWABLE_ID);
        indicatorUnselectedResId = typedArray.getResourceId(R.styleable.Banner_indicator_drawable_unselected, IDefaultBannerConfig.DEFAULT_UNSELECTED_INDICATOR_DRAWABLE_ID);
        delayTime = typedArray.getInt(R.styleable.Banner_delay_time, IDefaultBannerConfig.TIME);
        scrollTime = typedArray.getInt(R.styleable.Banner_scroll_time, IDefaultBannerConfig.DURATION);
        isAutoPlay = typedArray.getBoolean(R.styleable.Banner_is_auto_play, IDefaultBannerConfig.IS_AUTO_PLAY);
        titleBackground = typedArray.getColor(R.styleable.Banner_title_background,
                getResources().getColor(R.color.design_default_color_background, null));
        titleHeight = typedArray.getDimensionPixelSize(R.styleable.Banner_title_height, 0);
        titleTextColor = typedArray.getColor(R.styleable.Banner_title_textcolor, getResources().getColor(R.color.design_default_color_background, null));
        titleTextSize = typedArray.getDimensionPixelSize(R.styleable.Banner_title_textsize, IDefaultBannerConfig.TITLE_TEXT_SIZE);
        layoutResId = typedArray.getResourceId(R.styleable.Banner_banner_layout, layoutResId);
        bannerBackgroundImage = typedArray.getResourceId(R.styleable.Banner_banner_default_image, R.drawable.banner_default_drawable);
        typedArray.recycle();
    }

    /**
     * 反射修改viewPager切换速度
     */
    private void initViewPagerScroll() {
        try {
            Field mField = ViewPager2.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            scroller = new BannerScroller(viewPager.getContext());
            scroller.setDuration(scrollTime);
            mField.set(viewPager, scroller);
        } catch (Exception e) {
            LogUtils.d(TAG, "init views pager scroll error");
        }
    }

    public void setBannerHeight(int height){
        if (bannerContainer != null){
            ViewGroup.LayoutParams params = bannerContainer.getLayoutParams();
            params.height = height;
            bannerContainer.setLayoutParams(params);
        }
    }

    public void setBannerCornerRadius(int cornerRadius){
        this.cornerRadius = cornerRadius;
    }

    public void setIndicatorMargin(int indicatorMargin) {
        this.indicatorMargin = indicatorMargin;
    }

    public void setIndicatorWidth(int indicatorWidth) {
        this.indicatorWidth = indicatorWidth;
    }

    public void setIndicatorHeight(int indicatorHeight) {
        this.indicatorHeight = indicatorHeight;
    }

    public void setBannerBackgroundImage(int bannerBackgroundImage) {
        this.bannerBackgroundImage = bannerBackgroundImage;
        if (bannerDefaultImage != null ) {
            bannerDefaultImage.setImageResource(bannerBackgroundImage);
        }
    }

     public void setScrollTime(int scrollTime) {
        this.scrollTime = scrollTime;
    }

    /**
     * 设置是否允许手动滑动轮播图（默认true）
     * @param scroll
     * @return
     */
    public void setScroll(boolean scroll) {
        isScroll = scroll;
    }

    public void setIndicatorSelectedResId(int indicatorSelectedResId) {
        this.indicatorSelectedResId = indicatorSelectedResId;
    }

    public void setIndicatorUnselectedResId(int indicatorUnselectedResId) {
        this.indicatorUnselectedResId = indicatorUnselectedResId;
    }

    public void setTitleHeight(int titleHeight) {
        if (titleView != null){
            this.titleHeight = titleHeight;
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) titleView.getLayoutParams();
            layoutParams.height = titleHeight;
            titleView.setLayoutParams(layoutParams);
        }
    }

    public void setTitleBackground(int titleBackground) {
        if (titleView != null ) {
            this.titleBackground = titleBackground;
            titleView.setBackgroundColor(titleBackground);
        }
    }

    public void setTitleTextColor(int titleTextColor) {
        if (bannerTitle != null ){
            this.titleTextColor = titleTextColor;
            bannerTitle.setTextColor(titleTextColor);
            if (numIndicatorInside != null) {
                numIndicatorInside.setTextColor(titleTextColor);
            }
        }
    }

    public void setTitleTextSize(int titleTextSize) {
        if (bannerTitle != null ) {
            this.titleTextSize = titleTextSize;
            bannerTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
        }
    }

    /**
     * 设置是否自动轮播（默认自动）
     * @param isAutoPlay
     * @return
     */
    public void setAutoPlay(boolean isAutoPlay) {
        this.isAutoPlay = isAutoPlay;
    }

    /**
     * 设置自动轮播图片间隔时间（单位毫秒，默认为2000）
     * @param delayTime
     * @return
     */
    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    /**
     * 	设置轮播样式（默认为CIRCLE_INDICATOR）
     * @param bannerStyle
     * @return
     */
    public void setBannerStyle(BannerStyle bannerStyle) {
        this.bannerStyle = bannerStyle;
        updateBannerStyle(bannerStyle);
    }


    public void setNumberBg(int numberBg) {
        if (numIndicator != null) {
            numIndicator.setBackground(getResources().getDrawable(numberBg, null));
        }
    }

    public void setNumberHeight(int numberHeight) {
        if (numIndicator != null){
            ViewGroup.LayoutParams params = numIndicator.getLayoutParams();
            params.height = numberHeight;
            numIndicator.setLayoutParams(params);
        }
    }

    public void setNumberWidth(int numberWidth) {
        if (numIndicator != null){
            ViewGroup.LayoutParams params = numIndicator.getLayoutParams();
            params.width = numberWidth;
            numIndicator.setLayoutParams(params);
        }
    }

    public void setNumberMargin(int numberMargin) {
       if (numIndicator != null){
           ViewGroup.LayoutParams params = numIndicator.getLayoutParams();
           MarginLayoutParams marginLayoutParams = new MarginLayoutParams(params.width, params.height);
           marginLayoutParams.leftMargin = numberMargin;
           marginLayoutParams.rightMargin = numberMargin;
           numIndicator.setLayoutParams(marginLayoutParams);
       }
    }

    public void setNumberTextColor(int numberTextColor) {
        if (numIndicator != null) {
            numIndicator.setTextColor(numberTextColor);
        }

    }

    public void setNumberTextSize(int numberTextSize) {
        if (numIndicator != null ) {
            numIndicator.setTextSize(numberTextSize);
        }
    }

    /**
     * 	设置指示器位置（没有标题默认为右边,有标题时默认左边）
     * @param type
     * @return
     */
    public Banner setIndicatorGravity(BannerStyle.IndicatorGravity type) {
        if (type == BannerStyle.IndicatorGravity.LEFT){
            this.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        } else if (type == BannerStyle.IndicatorGravity.CENTER){
            this.gravity = Gravity.CENTER;
        } else if (type == BannerStyle.IndicatorGravity.RIGHT){
            this.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        }
        return this;
    }



    /**
     * 设置轮播要显示的标题和图片对应（如果不传默认不显示标题）
     * @param titles
     * @return
     */
    public Banner setBannerTitles(List<String> titles) {
        this.titles = titles;
        return this;
    }

    /**
     * 设置轮播图片(所有设置参数方法都放在此方法之前执行)
     * @param imageUrls
     * @return
     */
    public Banner setBannerImages(Context context, List<String> imageUrls) {
        this.imageUrls = imageUrls;
        this.count = imageUrls.size();
        this.glideImgContex = context;
        return this;
    }

    public void setBannerList(List<T> bannerList) {
        this.bannerList = bannerList;
    }

    /**
     * start之后手动调用自动轮播
     * @return
     */
    public Banner start() {
        setBannerStyleUI();
        setData();
        setImageList(imageUrls);
        return this;
    }

    /**
     * 更新图片和标题
     */
    public void updateImageTitleData(List<String> imageUrls, List<String> titles) {
        this.titles.clear();
        this.titles.addAll(titles);
        updateImageData(imageUrls);
    }

    /**
     * 更新图片
     * @param imageUrls
     */
    public void updateImageData(List<String> imageUrls) {
        this.imageUrls.clear();
        this.indicatorImages.clear();
        this.imageUrls.addAll(imageUrls);
        this.count = this.imageUrls.size();
        //start();
    }

    /**
     * 更新轮播样式
     * @param bannerStyle
     */
    public void updateBannerStyle(BannerStyle bannerStyle) {
        resetIndicator();
        this.bannerStyle = bannerStyle;
        start();
        startAutoPlay();
    }

    private void resetIndicator(){
        indicator.setVisibility(GONE);
        numIndicator.setVisibility(GONE);
        numIndicatorInside.setVisibility(GONE);
        indicatorInside.setVisibility(GONE);
        bannerTitle.setVisibility(View.GONE);
        titleView.setVisibility(View.GONE);
    }
    private void setBannerStyleUI() {
        int visibility =count > 1 ? View.VISIBLE :View.GONE;
        if (bannerStyle ==  BannerStyle.CIRCLE_INDICATOR ){
            indicator.setVisibility(visibility);
        } else if (bannerStyle == BannerStyle.NUM_INDICATOR ){
            numIndicator.setVisibility(visibility);
        } else if (bannerStyle == BannerStyle.NUM_INDICATOR_TITLE ){
            numIndicatorInside.setVisibility(visibility);
            setTitleStyleUI();
        } else if (bannerStyle == BannerStyle.CIRCLE_INDICATOR_TITLE ){
            indicator.setVisibility(visibility);
            setTitleStyleUI();
        } else if (bannerStyle == BannerStyle.CIRCLE_INDICATOR_TITLE_INSIDE){
            indicatorInside.setVisibility(visibility);
            setTitleStyleUI();
        } else if (bannerStyle == BannerStyle.IMAGE_With_title) {
            setTitleStyleUI();
        }
    }

    private void setTitleStyleUI() {
        if (titles != null && imageUrls!= null && titles.size() != imageUrls.size()) {
            LogUtils.e(TAG, "[Banner] --> The number of titles and images is different " +
                    "titles.size() = " + titles.size() + " imageUrls.size() = "+ imageUrls.size());
            return;
        }
        if (titleBackground != IDefaultBannerConfig.TITLE_BACKGROUND) {
            titleView.setBackgroundColor(titleBackground);
        }
        if (titleHeight != IDefaultBannerConfig.TITLE_HEIGHT) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) titleView.getLayoutParams();
            layoutParams.height = titleHeight;
            titleView.setLayoutParams(layoutParams);
        }
        if (titleTextColor != IDefaultBannerConfig.TITLE_TEXT_COLOR) {
            bannerTitle.setTextColor(titleTextColor);
        }
        if (titleTextSize != IDefaultBannerConfig.TITLE_TEXT_SIZE) {
            bannerTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
        }

        if (titles != null && titles.size() > 0) {
            bannerTitle.setText(titles.get(0));
            bannerTitle.setVisibility(View.VISIBLE);
            titleView.setVisibility(View.VISIBLE);
        }
    }

    protected void setImageList(List<?> imagesUrl) {
        if (imagesUrl == null || imagesUrl.size() <= 0) {
            bannerDefaultImage.setVisibility(VISIBLE);
            LogUtils.e(TAG, "The image data set is empty.");
            return;
        }
        bannerDefaultImage.setVisibility(GONE);
        initImages();
        bannerPagerAdapter.initData(imageUrls);
    }

    public List<String> getImageUrlList(){
        return new ArrayList<String>(imageUrls);
    }

    protected void initImages() {
        if (bannerStyle == BannerStyle.CIRCLE_INDICATOR ||
                bannerStyle == BannerStyle.CIRCLE_INDICATOR_TITLE ||
                bannerStyle == BannerStyle.CIRCLE_INDICATOR_TITLE_INSIDE) {
            createIndicator();
        } else if (bannerStyle == BannerStyle.NUM_INDICATOR_TITLE) {
            numIndicatorInside.setText("1/" + count);
        } else if (bannerStyle == BannerStyle.NUM_INDICATOR) {
            numIndicator.setText("1/" + count);
        }
    }

    public void createIndicator() {
        indicatorImages.clear();
        indicator.removeAllViews();
        indicatorInside.removeAllViews();
        for (int i = 0; i < count; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(indicatorWidth, indicatorHeight);
            params.leftMargin = indicatorMargin;
            params.rightMargin = indicatorMargin;
            if (i == 0) {
                imageView.setImageResource(indicatorSelectedResId);
            } else {
                imageView.setImageResource(indicatorUnselectedResId);
            }
            indicatorImages.add(imageView);
            if (bannerStyle == BannerStyle.CIRCLE_INDICATOR ||
                    bannerStyle == BannerStyle.CIRCLE_INDICATOR_TITLE){
                indicator.addView(imageView, params);
            } else if(bannerStyle == BannerStyle.CIRCLE_INDICATOR_TITLE_INSIDE){
                indicatorInside.addView(imageView, params);
            }
        }
    }

    private void setData() {
        currentItem = 1;
        if (bannerPagerAdapter == null) {
            bannerPagerAdapter = new BannerPagerAdapter(getContext());
            bannerPagerAdapter.setBannerClickListener(new IBannerClickListener() {
                @Override
                public void OnBannerClick(int position) {
                    if (bannerClickListener != null){
                        bannerClickListener.OnBannerClick(toRealPosition(position));
                    }
                }
            });
            viewPager.registerOnPageChangeCallback(mPageChangeCallBack);
        }
        viewPager.setAdapter(bannerPagerAdapter);
        viewPager.setFocusable(true);
        viewPager.setCurrentItem(1);
        if (gravity != -1){
            indicator.setGravity(gravity);
        }
    }


    public void startAutoPlay() {
        LogUtils.d(TAG,"start auto play isPlaying:"+ isAutoPlaying+"  count:"+count);
        if(!isAutoPlay || isAutoPlaying){
            return;
        }
        isAutoPlaying = true;
        handler.removeCallbacks(task);
        handler.postDelayed(task, delayTime);
    }

    public void stopAutoPlay() {
        LogUtils.d(TAG,"stop auto play isPlaying:"+ isAutoPlaying);
//        if(!isAutoPlaying){
//            return;
//        }
        isAutoPlaying = false;
        handler.removeCallbacks(task);
    }

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if (count > 1 && isAutoPlay) {
                currentItem = currentItem % (count + 1) + 1;
                LogUtils.d(TAG, "curr:" + currentItem + " count:" + count);
                if (currentItem == 1) {
                    viewPager.setCurrentItem(currentItem, false);
                    handler.post(task);
                } else {
                    viewPager.setCurrentItem(currentItem);
                    handler.postDelayed(task, delayTime);
                }
            }
        }
    };
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogUtils.i(TAG, ev.getAction() + "--" + isAutoPlay);
        if (isAutoPlay) {
            int action = ev.getAction();
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                    || action == MotionEvent.ACTION_OUTSIDE) {
                actionUpTime = SystemClock.elapsedRealtime();
                startAutoPlay();
            } else if (action == MotionEvent.ACTION_DOWN) {
                stopAutoPlay();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private ViewPager2.OnPageChangeCallback mPageChangeCallBack = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            LogUtils.d(TAG,"onPageSelected position:"+position);
            currentItem = position;
            if (onPageChangeListener != null) {
                onPageChangeListener.onPageSelected(toRealPosition(position));
            }
            if (bannerStyle == BannerStyle.CIRCLE_INDICATOR ||
                    bannerStyle == BannerStyle.CIRCLE_INDICATOR_TITLE ||
                    bannerStyle == BannerStyle.CIRCLE_INDICATOR_TITLE_INSIDE) {
                indicatorImages.get((lastPosition - 1 + count) % count).setImageResource(indicatorUnselectedResId);
                indicatorImages.get((position - 1 + count) % count).setImageResource(indicatorSelectedResId);
                lastPosition = position;
            }
            if (position == 0) position = count;
            if (position > count) position = 1;
            if (bannerStyle == BannerStyle.NUM_INDICATOR){
                numIndicator.setText(position + "/" + count);
            } else if (bannerStyle == BannerStyle.NUM_INDICATOR_TITLE){
                numIndicatorInside.setText(position + "/" + count);
                bannerTitle.setText(titles.get(position - 1));
            } else if (bannerStyle == BannerStyle.CIRCLE_INDICATOR_TITLE){
                bannerTitle.setText(titles.get(position - 1));
            } else if (bannerStyle ==BannerStyle.CIRCLE_INDICATOR_TITLE_INSIDE){
                bannerTitle.setText(titles.get(position - 1));
            } else if (bannerStyle ==BannerStyle.IMAGE_With_title) {
                bannerTitle.setText(titles.get(position - 1));
            }

            if(lastRepPosition != position){
                reportBannerExposed(IBannerExposeListener.BANNER_EXPOSED);
            }
            if(actionUpTime != -1 && (SystemClock.elapsedRealtime() - actionUpTime) < IDefaultBannerConfig.DURATION){
                reportBannerExposed(IBannerExposeListener.BANNER_MANAUL_SCROLL);
                actionUpTime = -1;
            }
            lastRepPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
            LogUtils.d(TAG, "onPageScrollStateChanged state: " + state);
            switch (state) {
                case 0://No operation
                    if (currentItem == 0) {
                        viewPager.setCurrentItem(count, false);
                    } else if (currentItem == count + 1) {
                        viewPager.setCurrentItem(1, false);
                    }
                    break;
                case 1://start Sliding
                    if (currentItem == count + 1) {
                        viewPager.setCurrentItem(1, false);
                    } else if (currentItem == 0) {
                        viewPager.setCurrentItem(count, false);
                    }
                    break;
                case 2://end Sliding
                    break;
            }
        }
    };





    /**
     * 点击事件，下标从0开始
     * @param bannerClickListener
     * @return
     */
    public Banner setOnBannerListener(IBannerClickListener bannerClickListener) {
        this.bannerClickListener = bannerClickListener;
        return this;
    }



    public void releaseBanner() {
        handler.removeCallbacks(task);
        handler.removeCallbacksAndMessages(null);
        unRegisterBannerExposeListener();
    }

    public void registerBannerExposeListener(IBannerExposeListener bannerClickListner){
        this.bannerExposeListener = bannerClickListner;
    }

    public void unRegisterBannerExposeListener(){
        if (this.bannerExposeListener != null){
            this.bannerExposeListener = null;
        }
    }

    //banner曝光 or 滑动banner埋点
    public void reportBannerExposed(String eventId){
        LogUtils.d(TAG,"reportBannerExposed currentItem:"+currentItem+" eventId:"+eventId);
        if(toRealPosition(currentItem) >= bannerList.size()){
            return;
        }
        if (bannerExposeListener != null){
            bannerExposeListener.reportBannerExpose(eventId, bannerList.get(toRealPosition(currentItem)));
        }
    }

    /**
     * 返回真实的位置
     *
     * @param position
     * @return 下标从0开始
     */
    public int toRealPosition(int position) {
        if(count == 0){
            return 0;
        }
        int realPosition = (position - 1) % count;
        if (realPosition < 0)
            realPosition += count;
        return realPosition;
    }
}
