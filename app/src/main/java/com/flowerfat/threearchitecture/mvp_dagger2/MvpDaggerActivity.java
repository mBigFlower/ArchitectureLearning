package com.flowerfat.threearchitecture.mvp_dagger2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.flowerfat.threearchitecture.R;
import com.flowerfat.threearchitecture.Util;
import com.flowerfat.threearchitecture.mvp__plus.BaseActivity;
import com.flowerfat.threearchitecture.mvp__plus.queryphone.MvpPlusContract;
import com.flowerfat.threearchitecture.mvp__plus.queryphone.MvpPlusModel;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class MvpDaggerActivity extends BaseActivity implements MvpPlusContract.View{

    @Inject
    MvpDaggerPresenter mPresenter;
    // dialog相关的显示
    private ProgressDialog mProgressDialog;

    @BindView(R.id.architecture_et)
    EditText mPhoneEt;
    @BindView(R.id.architecture_tv)
    TextView mResultTv;
    @BindView(R.id.architecture_lv)
    ListView mListView;

    private ArrayAdapter<String> mAdapter;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, MvpDaggerActivity.class));
    }

    @Override
    public int initLayout() {
        return R.layout.activity_architecture;
    }

    @Override
    public void init() {
        // Create the presenter
        DaggerMvpDaggerComponent.builder()
                .mvpDaggerPresenterModule(new MvpDaggerPresenterModule(this, new MvpPlusModel()))
                .build().inject(this);

        // 因为有个dagger ， 这里不需要创建实例了
//        mPresenter = new MvpPlusPresenter(this, new MvpPlusModel());

        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mListView.setAdapter(mAdapter);

        filter();

        mPresenter.start();
    }

    @OnClick(R.id.architecture_bt)
    void queryBt() {
        if (mPresenter != null)
            mPresenter.queryPhone(mPhoneEt.getText().toString().trim());
    }

    @OnClick(R.id.architecture_clearTv)
    void clearHistory() {
        mPresenter.clearHistory();
    }

    @Override
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.pls_wait));
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
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
        if (historyList != null)
            mAdapter.addAll(historyList);
    }

    @Override
    public void setPresenter(MvpPlusContract.Presenter presenter) {

    }


    private void filter() {
        mPhoneEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence searchedStr, int i, int i1, int i2) {
                if (searchedStr.length() != 11) {
                    mResultTv.setText("");
                }
                mPresenter.filterHistory(searchedStr.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
