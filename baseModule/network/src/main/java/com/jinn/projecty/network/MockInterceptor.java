package com.jinn.projecty.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by jinnlee on 2021/2/4.
 * http请求拦截器，用于mock数据
 */
public class MockInterceptor implements Interceptor {
    private String mResponseString;

    public MockInterceptor(String responseString) {
        mResponseString = responseString;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = new Response.Builder()
                .message(mResponseString)
                .code(200)
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create(MediaType.parse("application/json"), mResponseString.getBytes()))
                .addHeader("content-type", "application/json")
                .build();
        return response;
    }
}
