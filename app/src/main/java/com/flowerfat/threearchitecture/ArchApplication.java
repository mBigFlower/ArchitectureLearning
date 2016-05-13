package com.flowerfat.threearchitecture;

import android.app.Application;

import com.flowerfat.volleyutil.main.VolleyUtils;

/**
 * Created by 明明大美女 on 2016/5/12.
 */
public class ArchApplication extends Application {

    private static ArchApplication sInstance ;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this ;
        VolleyUtils.getInstance().init(this);
    }

    public static ArchApplication get(){
        return sInstance;
    }
}
