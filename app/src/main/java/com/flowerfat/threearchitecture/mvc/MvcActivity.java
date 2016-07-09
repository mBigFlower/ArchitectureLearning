package com.flowerfat.threearchitecture.mvc;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.flowerfat.threearchitecture.Contracts;
import com.flowerfat.threearchitecture.R;
import com.flowerfat.threearchitecture.SpManager;
import com.flowerfat.threearchitecture.Util;
import com.flowerfat.volleyutil.callback.StringCallback;
import com.flowerfat.volleyutil.main.VolleyUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MvcActivity extends AppCompatActivity {

    @BindView(R.id.architecture_et)
    EditText mPhoneEt;
    @BindView(R.id.architecture_tv)
    TextView mResultTv;
    @BindView(R.id.architecture_lv)
    ListView mListView;

    private ArrayAdapter<String> mAdapter;
    private List<String> historyList = new ArrayList();
    private List<String> showList = new ArrayList<>();

    public static void launch(Context context) {
        context.startActivity(new Intent(context, MvcActivity.class));
    }

    // dialog相关的显示
    private ProgressDialog mProgressDialog;

    private void progressShow() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.pls_wait));
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.show();
    }

    private void progressDismiss() {
        mProgressDialog.dismiss();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_architecture);
        ButterKnife.bind(this);
        // 第三部分新增的俩
        init();
        fetchHistory();
        // 第四部分增加一个
        filter();
    }

    @OnClick(R.id.architecture_bt)
    void queryBt() {
        String phone = mPhoneEt.getText().toString().trim();

        if (phone.length() != 11 || !phone.startsWith("1")) {
            Util.sT(this, R.string.phone_error);
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
        progressShow();
        VolleyUtils.getInstance()
                .get(Contracts.Url)
                .addHeader("apikey", Contracts.Header)
                .addParam("tel", phone)
                .Go(new StringCallback() {
                    @Override
                    public void onSuccess(String response) {
                        progressDismiss();
                        showBeautifulResult(response);
                    }

                    @Override
                    public void onError(String e) {
                        progressDismiss();
                        mResultTv.setText(e);
                    }
                });
    }

    /* 第二部分的修改所添加的代码在下面 */
    private TelInfoMvc mTelInfoMvc;

    private void showBeautifulResult(String response) {
        mTelInfoMvc = new Gson().fromJson(response, TelInfoMvc.class);
        String result;
        // 判断查询结果
        if (mTelInfoMvc.getErrNum() == 0) {
            result = mTelInfoMvc.getRetData().getProvince() + "  " + mTelInfoMvc.getRetData().getCarrier();
            doAboutHistory(mTelInfoMvc.getRetData().getTelString() + "  --  " + result);
        } else {
            result = mTelInfoMvc.getErrMsg();
        }
        mResultTv.setText(result);
    }

    /* 第三部分的代码如下 存数据到本地，并显示*/

    /**
     * 初始化
     * 第三部分
     */
    private void init() {
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mListView.setAdapter(mAdapter);
    }

    /**
     * 获取数据，显示
     * 第三部分
     */
    private void fetchHistory() {
        // 获取数据
        String data = SpManager.get().getStr(SpManager.KEY_HISTORY_LIST);
        if (data != null) {
            historyList = new Gson().fromJson(data, new TypeToken<List<String>>() {
            }.getType());
        }
        // 显示
        showList.addAll(historyList);
        mAdapter.addAll(showList);
    }

    public void doAboutHistory(String result) {
        if (!isThisHistoryExist(result)) {
            // 保存
            historyList.add(result);
            showList.add(result);
            SpManager.get().putStr(SpManager.KEY_HISTORY_LIST, new Gson().toJson(historyList));
            // 显示
            mAdapter.add(result);
            mAdapter.notifyDataSetChanged();
        }
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

    /* 第四部分代码如下 清空历史，增加edittext的筛选*/
    @OnClick(R.id.architecture_clearTv)
    void clearHistory() {
        historyList.clear();
        mAdapter.clear();
        SpManager.get().putStr(SpManager.KEY_HISTORY_LIST, null);
    }

    private void filter() {
        mPhoneEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence searchedStr, int i, int i1, int i2) {
                if(searchedStr.length() != 11) {
                    mResultTv.setText("");
                }
                filterDetails(searchedStr.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * 具体筛选细节
     * @param searchedStr
     */
    private void filterDetails(String searchedStr) {
        showList.clear();
        for (String str : historyList) {
            if(str.contains(searchedStr)) {
                showList.add(str);
            }
        }
        mAdapter.clear();
        mAdapter.addAll(showList);
    }

}