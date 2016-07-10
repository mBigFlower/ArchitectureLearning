package com.flowerfat.threearchitecture.mvp.model;

import com.flowerfat.threearchitecture.Contracts;
import com.flowerfat.threearchitecture.SpManager;
import com.flowerfat.threearchitecture.mvc.TelInfoMvc;
import com.flowerfat.volleyutil.callback.StringCallback;
import com.flowerfat.volleyutil.main.VolleyUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 明明大美女 on 2016/5/14.
 * <p>
 * 数据处理，包括 http请求
 * 有的朋友把这个Model叫做Server
 */
public class MvpModelImpl implements IMvpModel {

    List<String> historyList = new ArrayList<>();
    List<String> showList = new ArrayList<>();
    private TelInfoMvc mTelInfoMvc;

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

    private void showBeautifulResult(String response, final GetPhoneDetailsCallback callback) {
        mTelInfoMvc = new Gson().fromJson(response, TelInfoMvc.class);
        String result;
        // 判断查询结果
        if (mTelInfoMvc.getErrNum() == 0) {
            result = mTelInfoMvc.getRetData().getProvince() + "  " + mTelInfoMvc.getRetData().getCarrier();
            String history = doAboutHistory(mTelInfoMvc.getRetData().getTelString() + "  --  " + result);
            callback.Success(result, history);
        } else {
            result = mTelInfoMvc.getErrMsg();
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

    @Override
    public void doResult(String result) {

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
