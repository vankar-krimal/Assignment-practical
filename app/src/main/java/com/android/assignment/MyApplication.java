package com.android.assignment;

import android.app.Application;

import com.android.assignment.api.Apis;
import com.android.assignment.helper.ConnectionUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApplication extends Application {

    public static Gson gson;
    public static Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        // Fresco library for image displaying, initialization
        Fresco.initialize(this);

        initGson();

        initRetrofit();
    }

    private void initGson() {
        gson = new GsonBuilder()
                .setLenient()
                .create();
    }

    private void initRetrofit() {
        int cacheSize = 20 * 1024 * 1024; // Caching up to 20 MB
        Cache cache = new Cache(getCacheDir(), cacheSize);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .cache(cache) // for caching the response
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response originalResponse = chain.proceed(chain.request());
                        if (ConnectionUtils.isConnected(getApplicationContext())) {

                            int maxAge = 60 * 5; // read response from cache for 2 minute
                            return originalResponse.newBuilder()
                                    .header("Cache-Control", "public, max-age=" + maxAge)
                                    .build();

                        } else {
                            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                            return originalResponse.newBuilder()
                                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                    .build();
                        }
                    }
                })
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Apis.HOST)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    public static Gson getGson() {
        return gson;
    }



}
