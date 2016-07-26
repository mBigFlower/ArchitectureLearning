package com.flowerfat.threearchitecture;

import com.flowerfat.threearchitecture.mvp.model.IMvpModel;
import com.flowerfat.threearchitecture.mvp.model.MvpModelImpl;
import com.flowerfat.threearchitecture.mvp.presenter.MvpPresenter;
import com.flowerfat.threearchitecture.mvp.view.IMvpView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by 明明大美女 on 2016/7/15.
 * <p/>
 * 测试 P层，则需要模拟 V和 M层。
 * 值得一提的是，只需要测试 P层的几个方法就好，不需要断言输入输出数据是否准确（放在其它层去做）
 */
public class PresenterTest {

    List<String> mHistories = new ArrayList<>();

    @Mock
    private MvpModelImpl modelMock;

    @Mock
    private IMvpView viewMock;

    private MvpPresenter mMvpPresenter;

    @Captor
    private ArgumentCaptor<IMvpModel.GetPhoneDetailsCallback> mNetworkCallbackCaptor;

    @Before
    public void setup() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);
        // Get a reference to the class under test
        mMvpPresenter = new MvpPresenter(viewMock, modelMock);
    }

    @Test
    public void inputError_showError() {
        mMvpPresenter.queryPhone("");
        verify(viewMock).showPhoneError();
    }

    @Test
    public void input_internet_showSuccess() {
        // 模拟输入的手机号
        mMvpPresenter.queryPhone("15828433284");
        // 验证是否调用model的网络请求，并且catch到了网络请求的返回interface
        String historyStr = "this is history";
        ArgumentCaptor<String> phoneCaptor = ArgumentCaptor.forClass(String.class);
        verify(modelMock).httpQueryPhoneLocation(phoneCaptor.capture(), mNetworkCallbackCaptor.capture());
        // 通过catch到的interface，我们强行模拟调用了Success函数，并且catch了成功的结果，模拟传入了历史（这个历史应该这么做？）
        mNetworkCallbackCaptor.getValue().Success(phoneCaptor.getValue(), historyStr);
        // 测返回结果
        ArgumentCaptor<String> resultArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(viewMock).showReslut(resultArgumentCaptor.capture());
        assertEquals(phoneCaptor.getValue(), resultArgumentCaptor.getValue());
        // 测历史
        ArgumentCaptor<String> historyArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(viewMock).addHistory(historyArgumentCaptor.capture());
        assertEquals(historyStr, historyArgumentCaptor.getValue());
    }

    /**
     * 这里的获得到的历史 的内容，是否需要验证？
     */
    @Test
    public void initHistoryTest(){
        mMvpPresenter.initHistory();
//        ArgumentCaptor<List> historyArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(modelMock).getHistoryList();
        verify(viewMock).showHistory(anyList());
    }
    /**
     * 这里的获得到的历史 的内容，是否需要验证？
     */
    @Test
    public void clearHistoryTest(){
        mMvpPresenter.clearHistory();

        verify(modelMock).clearHistory();

        ArgumentCaptor<List> historyArgumentCaptor1 = ArgumentCaptor.forClass(List.class);
        verify(viewMock).showHistory(historyArgumentCaptor1.capture());
        assertTrue(historyArgumentCaptor1.getValue() == null);
    }

    @Test
    public void filterHistoryTest(){
        mHistories.add("xixi");
        mHistories.add("haha");
        // 这个when啊，一定要放在前面。 不要说该调用到when里面的内容的时候，我再when。
        when(modelMock.filterHistory(anyString())).thenReturn(mHistories);
        mMvpPresenter.filterHistroy(anyString());

        ArgumentCaptor<List> historyArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(viewMock).showHistory(historyArgumentCaptor.capture());
        assertTrue(historyArgumentCaptor.getValue().size() == 2);// ?????
    }
}
