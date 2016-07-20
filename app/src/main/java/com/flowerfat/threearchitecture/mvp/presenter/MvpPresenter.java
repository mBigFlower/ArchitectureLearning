package com.flowerfat.threearchitecture.mvp.presenter;

import com.flowerfat.threearchitecture.mvp.model.IMvpModel;
import com.flowerfat.threearchitecture.mvp.model.MvpModelImpl;
import com.flowerfat.threearchitecture.mvp.view.IMvpView;

/**
 * Created by 明明大美女 on 2016/5/13.
 */
public class MvpPresenter {

    // 持有View的接口，有啥事儿就通过这个接口通知View做事情
    private IMvpView mIMvpView;
    // 持有Model
    private MvpModelImpl mModel;

    /**
     * 这个构造方法：在activity调用的时候，把其view的接口传递过来，让次presenter持有，方便交互
     * @param mIMvpView
     */
    public MvpPresenter(IMvpView mIMvpView, MvpModelImpl model) {
        this.mIMvpView = mIMvpView;
        this.mModel = model;
    }

    public void queryPhone(String phone){
        if (phone.length() != 11 || !phone.startsWith("1")) {
            mIMvpView.showPhoneError();
            return;
        }
        httpQueryPhoneLocation(phone);
    }
    /**
     * 查询号码归属地的网络请求
     *
     * @param phone 电话号码
     */
    private void httpQueryPhoneLocation(String phone) {
        mModel.httpQueryPhoneLocation(phone, new IMvpModel.GetPhoneDetailsCallback() {
            @Override
            public void Success(String response, String history) {
                mIMvpView.showReslut(response);
                if(history != null) {
                    mIMvpView.addHistory(history);
                }
            }

            @Override
            public void Error(String error) {
                mIMvpView.showReslut(error);
            }
        });
    }

    public void initHistory(){
        mIMvpView.showHistory(mModel.getHistoryList());
    }

    public void filterHistroy(String inputStr){
        mIMvpView.showHistory(mModel.filterHistory(inputStr));
    }

    public void clearHistory(){
        mModel.clearHistory();
        mIMvpView.showHistory(null);
    }

}
