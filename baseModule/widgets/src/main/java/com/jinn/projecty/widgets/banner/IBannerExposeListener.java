package com.jinn.projecty.widgets.banner;

public interface IBannerExposeListener<T> {
    //卡片推荐页banner划动
    String BANNER_MANAUL_SCROLL = "002|006|50|035";
    //卡片推荐页banner曝光
    String BANNER_EXPOSED = "002|006|02|035";
    //卡片推荐页banner点击
    String BANNER_CLICK = "002|006|01|035";

    void reportBannerExpose(String eventId, T banner);
}
