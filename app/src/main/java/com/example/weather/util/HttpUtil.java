package com.example.weather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
        //callback参数为okhttp库中自带的回调接口，newCall()之后没有直接执行execute()方法
        //而是调用enqueue方法，并把callback传入，在enqueue方法内部已经开启了子线程
        //会在子线程中去执行HTTP请求，并将最终结果回调到callback中
    }
}
