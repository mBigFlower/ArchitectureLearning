package com.flowerfat.threearchitecture.mvp.model;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by 明明大美女 on 2016/5/14.
 */
public interface IMvpModel {

    interface GetPhoneDetailsCallback{
        void Success(String response, String history);
        void Error(String error);
    }

    void doResult(String result);
    List<String> getHistoryList();
    List<String> filterHistory(String inputStr);
    void clearHistory();
    void httpQueryPhoneLocation(@NonNull String phone,@NonNull GetPhoneDetailsCallback callback);
}
