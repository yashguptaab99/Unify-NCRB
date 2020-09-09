package com.example.unify.api;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static  final  String Base_URL = "http://164.100.44.122/";
    private static  RetrofitClient mInstance;
    private static Retrofit retrofit;

    private RetrofitClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(Base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Retrofit getRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES).build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(Base_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient).build();

        }
        return retrofit;
    }

    public static synchronized RetrofitClient getInstance(){
        if(mInstance == null){
            mInstance = new RetrofitClient();
        }
        return  mInstance;
    }

    public Api getApi()
    {
        return retrofit.create(Api.class);

    }
}
