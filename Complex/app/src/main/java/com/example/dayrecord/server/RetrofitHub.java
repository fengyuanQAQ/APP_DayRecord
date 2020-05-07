package com.example.dayrecord.server;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHub {
    private static WebApi sWebApi=null;
            private static String url="http://101.200.230.4:9120";

    public static WebApi getWebApi(){
        if (sWebApi == null) {
//            //客户端设置连接时间
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10000, TimeUnit.MILLISECONDS)//10s
                    .build();

            //retrofit框架
            Retrofit retrofit=new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(url)
                    .build();

            sWebApi =retrofit.create(WebApi.class);
        }
        return sWebApi;
    }

}
