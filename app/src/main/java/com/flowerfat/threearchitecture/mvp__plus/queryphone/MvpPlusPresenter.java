package com.flowerfat.threearchitecture.mvp__plus.queryphone;

import android.support.annotation.NonNull;

import com.flowerfat.threearchitecture.mvp.model.IMvpModel;

/**
 * Created by 明明大美女 on 2016/7/27.
 */
public class MvpPlusPresenter implements MvpPlusContract.Presenter {

    private final MvpPlusContract.View mMvpPlusView ;
    private final MvpPlusModel mMvpPlusModel ;

    public MvpPlusPresenter(MvpPlusContract.View mMvpPlusView, MvpPlusModel mMvpPlusModel) {
        this.mMvpPlusView = mMvpPlusView;
        this.mMvpPlusModel = mMvpPlusModel;

//        this.mMvpPlusView.setPresenter(this);
    }

    @Override
    public void start() {
        initHistory();
    }

    @Override
    public void queryPhone(@NonNull String phone) {
        if (phone.length() != 11 || !phone.startsWith("1")) {
            mMvpPlusView.showPhoneError();
            return;
        }
        mMvpPlusView.showProgressDialog();
        mMvpPlusModel.httpQueryPhoneLocation(phone, new IMvpModel.GetPhoneDetailsCallback() {
            @Override
            public void Success(String response, String history) {
                mMvpPlusView.dismissProgressDialog();
                mMvpPlusView.showReslut(response);
                if(history != null) {
                    mMvpPlusView.addHistory(history);
                }
            }

            @Override
            public void Error(String error) {
                mMvpPlusView.dismissProgressDialog();
                mMvpPlusView.showReslut(error);
            }
        });
    }

    @Override
    public void initHistory() {
        mMvpPlusView.showHistory(mMvpPlusModel.getHistoryList());
    }

    @Override
    public void filterHistory(String inputStr) {
        mMvpPlusView.showHistory(mMvpPlusModel.filterHistory(inputStr));
    }

    @Override
    public void clearHistory() {
        mMvpPlusModel.clearHistory();
        mMvpPlusView.showHistory(null);
    }
}
