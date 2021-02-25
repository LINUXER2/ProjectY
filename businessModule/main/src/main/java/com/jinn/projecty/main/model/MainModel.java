package com.jinn.projecty.main.model;

import com.jinn.projecty.base.BaseModel;
import com.jinn.projecty.main.api.RetrofitApi;
import com.jinn.projecty.main.bean.RecommandDataBean;
import com.jinn.projecty.network.RetrofitManager;

import io.reactivex.Observable;

/**
 * Created by jinnlee on 2021/1/22.
 */
public class MainModel extends BaseModel {
    private final String BASE_URL = "https://baobab.kaiyanapp.com/api/";

    public Observable<RecommandDataBean> requestMainData(){
       return RetrofitManager.getInstance().createService(RetrofitApi.class,BASE_URL).getMainData("2");
    }

    public Observable<RecommandDataBean>requestRelateData(String url){
        return RetrofitManager.getInstance().createService(RetrofitApi.class,"").getMoreData(url);
    }
}
