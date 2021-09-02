package com.azzida.rest;


import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    public static final String BASE_URL = ApiUrls.BASE_URL;
    public static final String BASE_URL_MAP = ApiUrls.BASE_URL_MAP;
    public static final String BASE_URL_Checker = ApiUrls.BASE_URL_Checker;
    public static int unique_id;
    private static Retrofit retrofit = null;
    private static Retrofit retrofitMap = null;
    private static Retrofit retrofitChecker = null;

    public static Retrofit getClient() {
        OkHttpClient client = getOkHttpClient();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
/*
                    .addConverterFactory(StringConverterFactory.create())
*/
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getNewClient() {
        OkHttpClient client = getOkHttpClient();
        if (retrofitMap == null) {
            retrofitMap = new Retrofit.Builder()
                    .baseUrl(BASE_URL_MAP)
/*
                    .addConverterFactory(StringConverterFactory.create())
*/
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofitMap;
    }

    public static Retrofit getNewlClient() {
        OkHttpClient client = getOkHttpClient();
        if (retrofitChecker == null) {
            retrofitChecker = new Retrofit.Builder()
                    .baseUrl(BASE_URL_Checker)
/*
                    .addConverterFactory(StringConverterFactory.create())
*/
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofitChecker;
    }

    private static OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder okClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //    httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        okClientBuilder.addInterceptor(httpLoggingInterceptor);
        okClientBuilder.addNetworkInterceptor(new StethoInterceptor());
        long CONNECTION_TIMEOUT = 100;
        okClientBuilder.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        okClientBuilder.readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        okClientBuilder.writeTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        return okClientBuilder.build();
    }
}


