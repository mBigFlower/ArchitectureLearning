package com.flowerfat.threearchitecture.mvp_dagger2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.flowerfat.threearchitecture.R;

public class MvpDaggerActivity extends AppCompatActivity {

    public static void launch(Context context) {
        context.startActivity(new Intent(context, MvpDaggerActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp_dagger);
    }
}
