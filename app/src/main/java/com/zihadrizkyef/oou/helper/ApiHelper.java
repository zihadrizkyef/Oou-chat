package com.zihadrizkyef.oou.helper;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created by zihadrizkyef on 01/08/17.
 */

public class ApiHelper {
    public static String API_BASE_URL = "http://192.168.100.25/OouAPI/";
    private static OkHttpClient okHttpClient;
    private static OouApiClient oouApiClient;

    public static OouApiClient getOouApiClient() {
        if (oouApiClient != null) {
            return oouApiClient;
        } else {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            oouApiClient = retrofit.create(OouApiClient.class);
            return oouApiClient;
        }
    }
}
