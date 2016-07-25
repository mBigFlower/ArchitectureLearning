package com.flowerfat.threearchitecture.mvp.view;

import android.app.Activity;
import android.support.test.espresso.IdlingResource;

/**
 * Created by 明明大美女 on 2016/7/24.
 */
public class EspressoDelay implements IdlingResource {

    private ResourceCallback resourceCallback;
    private Activity mActivity;

    public EspressoDelay(Activity activity) {
        mActivity = activity;
    }

    @Override
    public String getName() {
        return EspressoDelay.class.getName();
    }

    @Override
    public boolean isIdleNow() {
        if(((MvpActivity)mActivity).mResultTv.getText().equals("")) {
            resourceCallback.onTransitionToIdle();
            return false;
        }
        return true ;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        resourceCallback = callback;
    }
}
