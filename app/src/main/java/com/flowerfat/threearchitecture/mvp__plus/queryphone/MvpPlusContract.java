package com.flowerfat.threearchitecture.mvp__plus.queryphone;

import android.support.annotation.NonNull;

import com.flowerfat.threearchitecture.mvp__plus.BasePresenter;
import com.flowerfat.threearchitecture.mvp__plus.BaseView;

import java.util.List;

/**
 * Created by 明明大美女 on 2016/7/27.
 */
public interface MvpPlusContract {
    interface View extends BaseView<Presenter> {
        void showProgressDialog();
        void dismissProgressDialog();
        void showReslut(String result);
        void showPhoneError();
        void addHistory(String oneHistory);
        void showHistory(List<String> historyList);
    }
    interface Presenter extends BasePresenter {
        void queryPhone(@NonNull String phoneStr);
        void initHistory();
        void filterHistory(String inputStr);
        void clearHistory();
    }
}
