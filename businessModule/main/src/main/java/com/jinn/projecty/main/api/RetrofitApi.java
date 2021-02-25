package com.jinn.projecty.main.api;

import com.jinn.projecty.main.bean.RecommandDataBean;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by jinnlee on 2021/1/27.
 */
public interface RetrofitApi {
  @GET("v2/feed?")
    Observable<RecommandDataBean>getMainData(@Query("key")String key);

  @GET("v2/feed")
    Observable<ResponseBody>getMainData2(@FieldMap Map<String,Object>map);

  @GET("v2/feed?")
    Call<ResponseBody>getMainData3(@Query("key")String key);

  //根据 nextPageUrl 请求数据下一页数据
  @GET
    Observable<RecommandDataBean>getMoreData(@Url String url);


}
