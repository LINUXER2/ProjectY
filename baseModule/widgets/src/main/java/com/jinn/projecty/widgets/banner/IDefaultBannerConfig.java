package com.jinn.projecty.widgets.banner;


import android.graphics.Color;

import com.jinn.projecty.widgets.R;

public class IDefaultBannerConfig {
    static int PADDING_SIZE = 5;
    static int TIME = 5000; //默认轮播间隔
    static int DURATION = 500; //默认滑动时间
    static boolean IS_AUTO_PLAY = true; //默认自动轮播
    static boolean IS_SCROLL = true; //默认允许手动滑动轮播图
    static int DEFAULT_SELECTED_INDICATOR_DRAWABLE_ID = R.drawable.indicator_selected_drawable;
    static int DEFAULT_UNSELECTED_INDICATOR_DRAWABLE_ID = R.drawable.indicator_unselected_drwable;
    static final int TITLE_BACKGROUND = Color.parseColor("#FFFFFF");
    static final int TITLE_HEIGHT = 40;
    static final int TITLE_TEXT_COLOR = Color.parseColor("#000000");
    static final int TITLE_TEXT_SIZE = -1;
}
