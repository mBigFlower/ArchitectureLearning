package com.flowerfat.threearchitecture;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.flowerfat.threearchitecture.mvc.MvcActivity;
import com.flowerfat.threearchitecture.mvp.view.MvpActivity;
import com.flowerfat.threearchitecture.mvp_dagger2.MvpDaggerActivity;
import com.flowerfat.threearchitecture.mvp__plus.MvpPlusActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void MvcOnclick(View v) {
        MvcActivity.launch(this);
    }

    public void MvpOnclick(View v) {
        MvpActivity.launch(this);
    }

    public void MvpPlusOnclick(View v) {
        MvpPlusActivity.launch(this);
    }

    public void MvpDagger2Onclick(View v) {
        MvpDaggerActivity.launch(this);
    }
}
