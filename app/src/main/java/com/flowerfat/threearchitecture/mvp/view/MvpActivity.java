package com.flowerfat.threearchitecture.mvp.view;

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

import com.flowerfat.threearchitecture.R;
import com.flowerfat.threearchitecture.Util;
import com.flowerfat.threearchitecture.mvp.presenter.MvpPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MvpActivity extends AppCompatActivity implements IMvpView {

    @BindView(R.id.architecture_et)
    EditText mPhoneEt;
    @BindView(R.id.architecture_tv)
    TextView mResultTv;
    @BindView(R.id.architecture_lv)
    ListView mListView;

    private ArrayAdapter<String> mAdapter;

    private MvpPresenter mPresenter ;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, MvpActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_architecture);
        ButterKnife.bind(this);

        mPresenter = new MvpPresenter(this) ;

        init();
        // 第四部分增加一个
        filter();
    }

    /**
     * 初始化
     * 第三部分
     */
    private void init() {
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mListView.setAdapter(mAdapter);
        mPresenter.initHistory();
    }

    @OnClick(R.id.architecture_bt)
    void queryBt() {
        mPresenter.queryPhone();
    }

    @Override
    public String getPhoneStr() {
        return  mPhoneEt.getText().toString().trim();
    }

    /* 第四部分代码如下 清空历史，增加edittext的筛选*/
    @OnClick(R.id.architecture_clearTv)
    void clearHistory() {
        mPresenter.clearHistory();
    }

    @Override
    public void showProgressDialog() {

    }

    @Override
    public void dismissProgressDialog() {

    }

    @Override
    public void showReslut(String result) {
        mResultTv.setText(result);
    }

    @Override
    public void showPhoneError() {
        Util.sT(this, R.string.phone_error);
    }

    @Override
    public void addHistory(String oneHistory) {
        mAdapter.add(oneHistory);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showHistory(List<String> historyList) {
        mAdapter.clear();
        if(historyList != null)
            mAdapter.addAll(historyList);
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
                mPresenter.filterHistroy(searchedStr.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
