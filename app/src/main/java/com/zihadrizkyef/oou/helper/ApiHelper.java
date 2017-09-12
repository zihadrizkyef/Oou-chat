package com.zihadrizkyef.oou.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created by zihadrizkyef on 01/08/17.
 */

public class ApiHelper {
    public static String API_BASE_URL = "http://192.168.100.24/OouAPI/";
    public static OouApiClient getApiClient() {
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                MediaType contentType = response.body().contentType();
                String bodyString = response.body().string();
                ResponseBody body = ResponseBody.create(contentType, bodyString);
                Log.i("URL", chain.request().url().url().toString());
                Log.i("Server-Response", bodyString);
                return response.newBuilder().body(body).build();
            }
        };
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(OouApiClient.class);
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
