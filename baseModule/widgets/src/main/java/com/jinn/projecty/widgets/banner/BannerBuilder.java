package com.jinn.projecty.widgets.banner;

public class BannerBuilder {
    private static final int DEFAULT_NULL = -1;
    private int bannerHeight = DEFAULT_NULL;
    private int indicatorMargin = DEFAULT_NULL;
    private int indicatorWidth = DEFAULT_NULL;
    private int indicatorHeight = DEFAULT_NULL;
    private int bannerBackgroundImage = DEFAULT_NULL;
    private int indicatorSelectedResId = DEFAULT_NULL;
    private int indicatorUnselectedResId = DEFAULT_NULL;
    private BannerStyle bannerStyle;
    private int delayTime = DEFAULT_NULL;
    private int scrollTime = DEFAULT_NULL;
    private boolean isAutoPlay = IDefaultBannerConfig.IS_AUTO_PLAY;
    private boolean isScroll = IDefaultBannerConfig.IS_SCROLL;
    private int titleHeight = DEFAULT_NULL;
    private int titleBackground = DEFAULT_NULL;
    private int titleTextColor = DEFAULT_NULL;
    private int titleTextSize = DEFAULT_NULL;

    private int numberTextColor = DEFAULT_NULL;
    private int numberTextSize = DEFAULT_NULL;
    private int numberBg = DEFAULT_NULL;
    private int numberHeight = DEFAULT_NULL;
    private int numberWidth = DEFAULT_NULL;
    private int numberMargin = DEFAULT_NULL;
    private int cornerRadius = DEFAULT_NULL;

    private Banner banner;

    public BannerBuilder(Banner banner){
        this.banner = banner;
    }

    public BannerBuilder setBannerHeight(int bannerHeight) {
        this.bannerHeight = bannerHeight;
        return this;
    }

    public BannerBuilder setBannerCornerRadius(int cornerRadius){
        this.cornerRadius = cornerRadius;
        return this;
    }
    public BannerBuilder setIndicatorMargin(int indicatorMargin) {
        this.indicatorMargin = indicatorMargin;
        return this;
    }

    public BannerBuilder setIndicatorWidth(int indicatorWidth) {
        this.indicatorWidth = indicatorWidth;
        return this;
    }

    public BannerBuilder setIndicatorHeight(int indicatorHeight) {
        this.indicatorHeight = indicatorHeight;
        return this;
    }

    public BannerBuilder setBannerBackgroundImage(int bannerBackgroundImage) {
        this.bannerBackgroundImage = bannerBackgroundImage;
        return this;
    }

    public BannerBuilder setBannerStyle(BannerStyle bannerStyle) {
        this.bannerStyle = bannerStyle;
        return this;
    }

    public BannerBuilder setDelayTime(int delayTime) {
        this.delayTime = delayTime;
        return this;
    }

    public BannerBuilder setScrollTime(int scrollTime) {
        this.scrollTime = scrollTime;
        return this;
    }

    public BannerBuilder setAutoPlay(boolean autoPlay) {
        isAutoPlay = autoPlay;
        return this;
    }

    public BannerBuilder setScroll(boolean scroll) {
        isScroll = scroll;
        return this;
    }

    public BannerBuilder setIndicatorSelectedResId(int indicatorSelectedResId) {
        this.indicatorSelectedResId = indicatorSelectedResId;
        return this;
    }

    public BannerBuilder setIndicatorUnselectedResId(int indicatorUnselectedResId) {
        this.indicatorUnselectedResId = indicatorUnselectedResId;
        return this;
    }

    public BannerBuilder setTitleHeight(int titleHeight) {
        this.titleHeight = titleHeight;
        return this;
    }

    public BannerBuilder setTitleBackground(int titleBackground) {
        this.titleBackground = titleBackground;
        return this;
    }

    public BannerBuilder setTitleTextColor(int titleTextColor) {
        this.titleTextColor = titleTextColor;
        return this;
    }

    public BannerBuilder setTitleTextSize(int titleTextSize) {
        this.titleTextSize = titleTextSize;
        return this;
    }

    public BannerBuilder setNumberTextColor(int numberTextColor) {
        this.numberTextColor = numberTextColor;
        return this;
    }

    public BannerBuilder setNumberTextSize(int numberTextSize) {
        this.numberTextSize = numberTextSize;
        return this;
    }

    public BannerBuilder setNumberBg(int numberBg) {
        this.numberBg = numberBg;
        return this;
    }

    public BannerBuilder setNumberHeight(int numberHeight) {
        this.numberHeight = numberHeight;
        return this;
    }

    public BannerBuilder setNumberWidth(int numberWidth) {
        this.numberWidth = numberWidth;
        return this;
    }

    public BannerBuilder setNumberMargin(int numberMargin) {
        this.numberMargin = numberMargin;
        return this;
    }

    public Banner build(){
        if (bannerHeight != DEFAULT_NULL){
            banner.setBannerHeight(bannerHeight);
        }

        if (cornerRadius != DEFAULT_NULL){
            banner.setBannerCornerRadius(cornerRadius);
        }
        if (indicatorMargin != DEFAULT_NULL){
            banner.setIndicatorMargin(indicatorMargin);
        }

        if (indicatorWidth != DEFAULT_NULL ){
            banner.setIndicatorWidth(indicatorWidth);
        }

        if (indicatorHeight != DEFAULT_NULL){
            banner.setIndicatorHeight(indicatorHeight);
        }

        if (bannerBackgroundImage != DEFAULT_NULL){
            banner.setBannerBackgroundImage(bannerBackgroundImage);
        }

        if (bannerStyle != null){
            banner.setBannerStyle(bannerStyle);
        }

        if (delayTime != DEFAULT_NULL){
            banner.setDelayTime(delayTime);
        }

        if (scrollTime != DEFAULT_NULL){
            banner.setScrollTime(scrollTime);
        }

        banner.setAutoPlay(isAutoPlay);
        banner.setScroll(isScroll);
        if (indicatorSelectedResId != DEFAULT_NULL){
            banner.setIndicatorSelectedResId(indicatorSelectedResId);
        }
        if (indicatorUnselectedResId != DEFAULT_NULL){
            banner.setIndicatorUnselectedResId(indicatorUnselectedResId);
        }
        if (titleHeight != DEFAULT_NULL){
            banner.setTitleHeight(titleHeight);
        }
        if (titleBackground != DEFAULT_NULL){
            banner.setTitleBackground(titleBackground);
        }

        if (titleTextColor != DEFAULT_NULL){
            banner.setTitleTextColor(titleTextColor);
        }

        if (titleTextSize != DEFAULT_NULL){
            banner.setTitleTextSize(titleTextSize);
        }

        if (numberTextColor != DEFAULT_NULL){
            banner.setNumberTextColor(numberTextColor);
        }

        if (numberTextSize != DEFAULT_NULL){
            banner.setNumberTextSize(numberTextSize);
        }

        if (numberBg != DEFAULT_NULL){
            banner.setNumberBg(numberBg);
        }

        if (numberWidth != DEFAULT_NULL){
            banner.setNumberWidth(numberWidth);
        }

        if (numberHeight != DEFAULT_NULL){
            banner.setNumberHeight(numberHeight);
        }

        if (numberMargin != DEFAULT_NULL){
            banner.setNumberMargin(numberMargin);
        }
        return banner;
    }
}
