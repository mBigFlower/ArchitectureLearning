package com.flowerfat.threearchitecture.mvp.view;

import java.util.List;

/**
 * Created by 明明大美女 on 2016/5/13.
 */
public interface IMvpView {
    void showProgressDialog();
    void dismissProgressDialog();
    void showReslut(String result);
    void showPhoneError();
    String getPhoneStr();
    void addHistory(String oneHistory);
    void showHistory(List<String> historyList);
}
