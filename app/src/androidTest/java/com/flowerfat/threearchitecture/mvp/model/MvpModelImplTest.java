package com.flowerfat.threearchitecture.mvp.model;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Created by 明明大美女 on 2016/7/27.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MvpModelImplTest {

    private final String HISTORY_ONE = "history 1";
    private final String HISTORY_TWO = "history 2";
    private final String HISTORY_THREE = "history 3";

    private MvpModelImpl mvpModel ;

    @Before
    public void setup(){
        mvpModel = new MvpModelImpl();
    }

    @Test
    public void testSaveAndClearHistory() throws Exception {
        mvpModel.doAboutHistory(HISTORY_ONE);
        mvpModel.doAboutHistory(HISTORY_TWO);
        mvpModel.doAboutHistory(HISTORY_THREE);

        List<String> histories = mvpModel.getHistoryList();
        assertThat(histories.size(), is(3));
        assertThat(histories.get(0), is(HISTORY_ONE));
        assertThat(histories.get(1), is(HISTORY_TWO));
        assertThat(histories.get(2), is(HISTORY_THREE));

        mvpModel.clearHistory();
        histories = mvpModel.getHistoryList();
        assertThat(histories.size(), is(0));
    }

    @Test
    public void testFilterHistory() throws Exception {
        mvpModel.doAboutHistory(HISTORY_ONE);
        mvpModel.doAboutHistory(HISTORY_TWO);
        mvpModel.doAboutHistory(HISTORY_THREE);

        List<String> filterHistories = mvpModel.filterHistory("history");
        assertThat(filterHistories.size(), is(3));
        filterHistories = mvpModel.filterHistory("1");
        assertThat(filterHistories.size(), is(1));
    }


    /**
     * 这个没用啊。 是因为网络请求的延时吗？
     */
    @Test
    public void httpTest(){
        String phoneStr = "15828433284";
        mvpModel.httpQueryPhoneLocation(phoneStr, new IMvpModel.GetPhoneDetailsCallback() {
            @Override
            public void Success(String response, String history) {
                assertThat(" fuck ", is("四川  成都"));
                assertThat(history, is("15828433284  --  四川  成都"));
            }

            @Override
            public void Error(String error) {
                assertThat(" fuck ", is("四川  成都"));
                fail("Callback error");
            }
        });
    }
}