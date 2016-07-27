package com.flowerfat.threearchitecture.mvp__plus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by 明明大美女 on 2016/5/12.
 */
public abstract class BaseActivity extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initLayout());
        ButterKnife.bind(this);

        init();
    }

    public abstract int initLayout();
    public abstract void init();
}
