package com.flowerfat.threearchitecture.mvp.model;

import android.support.annotation.NonNull;

/**
 * Created by 明明大美女 on 2016/5/14.
 */
public interface IMvpModel {

    interface GetPhoneDetailsCallback{
        void Success(String response);
        void Error(String error);
    }

    void doResult(String result);
    void httpQueryPhoneLocation(@NonNull String phone,@NonNull GetPhoneDetailsCallback callback);
}
