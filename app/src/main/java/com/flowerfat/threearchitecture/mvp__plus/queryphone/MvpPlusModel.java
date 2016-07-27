package com.flowerfat.threearchitecture.mvp__plus.queryphone;

import com.flowerfat.threearchitecture.Contracts;
import com.flowerfat.threearchitecture.SpManager;
import com.flowerfat.threearchitecture.mvp.model.IMvpModel;
import com.flowerfat.threearchitecture.mvp__plus.data.TelInfoMvpPlus;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by 明明大美女 on 2016/7/27.
 */
public class MvpPlusModel {

    List<String> historyList = new ArrayList<>();
    List<String> showList = new ArrayList<>();
    private TelInfoMvpPlus mTelInfoMvpPlus;

    /**
     * 查询号码归属地的网络请求
     *
     * @param phone 电话号码
     */
    
    public void httpQueryPhoneLocation(String phone, final IMvpModel.GetPhoneDetailsCallback callback) {
        OkHttpUtils.get()
                .url(Contracts.Url)
                .addParams("tel", phone)
                .addHeader("apikey", Contracts.Header)
                .build()
                .execute(new StringCallback() {
                    
                    public void onError(Call call, Exception e, int id) {
                        callback.Error(e.toString());
                    }

                    
                    public void onResponse(String response, int id) {
                        showBeautifulResult(response, callback);
                    }
                });
    }

    private void showBeautifulResult(String response, final IMvpModel.GetPhoneDetailsCallback callback) {
        mTelInfoMvpPlus = new Gson().fromJson(response, TelInfoMvpPlus.class);
        String result;
        // 判断查询结果
        if (mTelInfoMvpPlus.getErrNum() == 0) {
            result = mTelInfoMvpPlus.getRetData().getProvince() + "  " + mTelInfoMvpPlus.getRetData().getCarrier();
            String history = doAboutHistory(mTelInfoMvpPlus.getRetData().getTelString() + "  --  " + result);
            callback.Success(result, history);
        } else {
            result = mTelInfoMvpPlus.getErrMsg();
            callback.Error(result);
        }
    }

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

    public List<String> filterHistory(String searchedStr) {
        showList.clear();
        for (String str : historyList) {
            if(str.contains(searchedStr)) {
                showList.add(str);
            }
        }
        return showList ;
    }

    
    public void clearHistory() {
        historyList.clear();
        showList.clear();
        SpManager.get().putStr(SpManager.KEY_HISTORY_LIST, null);
    }


}
