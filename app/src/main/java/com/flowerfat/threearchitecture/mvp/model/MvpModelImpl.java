package com.flowerfat.threearchitecture.mvp.model;

import com.flowerfat.threearchitecture.Contracts;
import com.flowerfat.threearchitecture.mvc.TelInfoMvc;
import com.flowerfat.volleyutil.callback.StringCallback;
import com.flowerfat.volleyutil.main.VolleyUtils;
import com.google.gson.Gson;

/**
 * Created by 明明大美女 on 2016/5/14.
 *
 * 数据处理，包括 http请求
 * 有的朋友把这个Model叫做Server
 */
public class MvpModelImpl implements IMvpModel {

    /**
     * 查询号码归属地的网络请求
     *
     * @param phone 电话号码
     */
    @Override
    public void httpQueryPhoneLocation(String phone, final GetPhoneDetailsCallback callback) {
        VolleyUtils.getInstance()
                .get(Contracts.Url)
                .addHeader("apikey", Contracts.Header)
                .addParam("tel", phone)
                .Go(new StringCallback() {
                    @Override
                    public void onSuccess(String response) {
                        showBeautifulResult(response, callback);
                    }

                    @Override
                    public void onError(String e) {
                        callback.Error(e);
                    }
                });
    }

    private TelInfoMvc mTelInfoMvc;
    private void showBeautifulResult(String response, final GetPhoneDetailsCallback callback) {
        mTelInfoMvc = new Gson().fromJson(response, TelInfoMvc.class);
        String result;
        // 判断查询结果
        if (mTelInfoMvc.getErrNum() == 0) {
            result = mTelInfoMvc.getRetData().getProvince() + "  " + mTelInfoMvc.getRetData().getCarrier();
            callback.Success(result);
        } else {
            result = mTelInfoMvc.getErrMsg();
            callback.Error(result);
        }
    }

    @Override
    public void doResult(String result) {

    }
}
