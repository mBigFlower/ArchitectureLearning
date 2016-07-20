package com.flowerfat.threearchitecture;

import android.app.Application;

import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by 明明大美女 on 2016/5/12.
 */
public class ArchApplication extends Application {

    private static ArchApplication sInstance ;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this ;

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }

    public static ArchApplication get(){
        return sInstance;
    }
}
