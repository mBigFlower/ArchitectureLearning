package com.flowerfat.threearchitecture.mvp_dagger2;

import android.support.annotation.NonNull;

import com.flowerfat.threearchitecture.mvp.model.IMvpModel;
import com.flowerfat.threearchitecture.mvp__plus.queryphone.MvpPlusContract;
import com.flowerfat.threearchitecture.mvp__plus.queryphone.MvpPlusModel;

import javax.inject.Inject;

/**
 * Created by 明明大美女 on 2016/8/13.
 */
public class MvpDaggerPresenter implements MvpPlusContract.Presenter {

    private final MvpPlusContract.View mMvpPlusView;
    private final MvpPlusModel mMvpPlusModel;

    @Inject
    public MvpDaggerPresenter(MvpPlusContract.View mMvpPlusView, MvpPlusModel mMvpPlusModel) {
        this.mMvpPlusView = mMvpPlusView;
        this.mMvpPlusModel = mMvpPlusModel;
    }

    @Inject
    void setupListeners() {
        mMvpPlusView.setPresenter(this);
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
                if (history != null) {
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
