package com.flowerfat.threearchitecture.mvp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.flowerfat.threearchitecture.R;

public class MvpActivity extends AppCompatActivity {


    public static void launch(Context context) {
        context.startActivity(new Intent(context, MvpActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp);
    }
}
