package com.flowerfat.threearchitecture.model;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.flowerfat.threearchitecture.mvp.model.IMvpModel;
import com.flowerfat.threearchitecture.mvp.model.MvpModelImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Created by 明明大美女 on 2016/7/26.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MvpModelImplTest {

    private MvpModelImpl mvpModel;

    @Before
    public void setup(){
        mvpModel = new MvpModelImpl();
    }

    @Test
    public void httpTest(){
        String phoneStr = "15828433284";
        mvpModel.httpQueryPhoneLocation(phoneStr, new IMvpModel.GetPhoneDetailsCallback() {
            @Override
            public void Success(String response, String history) {
                assertThat(response, is("四川  成都"));
                assertThat(history, is("15828433284  --  四川  成都"));
            }

            @Override
            public void Error(String error) {
                fail("Callback error");
            }
        });
    }

}