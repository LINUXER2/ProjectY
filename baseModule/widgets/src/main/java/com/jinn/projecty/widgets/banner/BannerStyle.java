package com.jinn.projecty.widgets.banner;

import android.util.SparseArray;

public enum BannerStyle {
    NO_INDICATOR(0),//没有任何提示，只有轮播的图片
    CIRCLE_INDICATOR(1),//圆形的提示，有几张图片则有几个
    NUM_INDICATOR(2),//数字提示
    NUM_INDICATOR_TITLE(3),//必须设置title list，调用setBannerTitles（）
    CIRCLE_INDICATOR_TITLE(4),//必须设置title list，调用setBannerTitles（）
    CIRCLE_INDICATOR_TITLE_INSIDE(5),//必须设置title list，调用setBannerTitles（）
    IMAGE_With_title(6);//图片和文字

    private static final SparseArray<BannerStyle>  styles =  new SparseArray<>();

    static {
        for (BannerStyle type : BannerStyle.values()) {
            styles.put(type.value, type);
        }
    }

    private int value = 0;

    public int getValue() {
        return value;
    }

    BannerStyle(int value) {
        this.value = value;
    }

    public static BannerStyle valueOf(int value) {
        BannerStyle type = styles.get(value);
        if (type == null)
            return BannerStyle.NO_INDICATOR;
        return type;
    }

    public enum IndicatorGravity{//圆形提示的位置，居左，居中，居右,CIRCLE_INDICATOR,CIRCLE_INDICATOR_TITLE
        LEFT,CENTER,RIGHT;
    }
}
