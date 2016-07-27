package com.flowerfat.threearchitecture.mvp.model;

import com.flowerfat.threearchitecture.Contracts;
import com.flowerfat.threearchitecture.SpManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by 明明大美女 on 2016/5/14.
 * <p>
 * 数据处理，包括 http请求
 * 有的朋友把这个Model叫做Server
 */
public class MvpModelImpl implements IMvpModel {

    List<String> historyList = new ArrayList<>();
    List<String> showList = new ArrayList<>();
    private TelInfoMvp mTelInfoMvp;

    /**
     * 查询号码归属地的网络请求
     *
     * @param phone 电话号码
     */
    @Override
    public void httpQueryPhoneLocation(String phone, final GetPhoneDetailsCallback callback) {
        OkHttpUtils.get()
                .url(Contracts.Url)
                .addParams("tel", phone)
                .addHeader("apikey", Contracts.Header)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.Error(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        showBeautifulResult(response, callback);
                    }
                });
    }

    private void showBeautifulResult(String response, final GetPhoneDetailsCallback callback) {
        mTelInfoMvp = new Gson().fromJson(response, TelInfoMvp.class);
        String result;
        // 判断查询结果
        if (mTelInfoMvp.getErrNum() == 0) {
            result = mTelInfoMvp.getRetData().getProvince() + "  " + mTelInfoMvp.getRetData().getCarrier();
            String history = doAboutHistory(mTelInfoMvp.getRetData().getTelString() + "  --  " + result);
            callback.Success(result, history);
        } else {
            result = mTelInfoMvp.getErrMsg();
            callback.Error(result);
        }
    }

    @Override
    public String doAboutHistory(String result) {
        if (!isThisHistoryExist(result)) {
            // 保存
            historyList.add(result);
            SpManager.get().putStr(SpManager.KEY_HISTORY_LIST, new Gson().toJson(historyList));
            return result;
        }
        return null ;
    }

    /**
     * 判断是否添加过
     *
     * @param result
     * @return
     */
    private boolean isThisHistoryExist(String result) {
        int size = historyList.size();
        for (int i = 0; i < size; i++) {
            if (result.equals(historyList.get(i))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> getHistoryList() {
        // 获取数据
        String data = SpManager.get().getStr(SpManager.KEY_HISTORY_LIST);
        if (data != null) {
            historyList = new Gson().fromJson(data, new TypeToken<List<String>>() {
            }.getType());
            showList.addAll(historyList);
            return showList;
        }
        return null;
    }

    @Override
    public List<String> filterHistory(String searchedStr) {
        showList.clear();
        for (String str : historyList) {
            if(str.contains(searchedStr)) {
                showList.add(str);
            }
        }
        return showList ;
    }

    @Override
    public void clearHistory() {
        historyList.clear();
        showList.clear();
        SpManager.get().putStr(SpManager.KEY_HISTORY_LIST, null);
    }


}
