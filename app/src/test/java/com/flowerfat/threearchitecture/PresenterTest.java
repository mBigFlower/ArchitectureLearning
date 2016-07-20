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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

/**
 * Created by 明明大美女 on 2016/7/15.
 * <p/>
 * 测试 P层，则需要模拟 V和 M层。
 * 值得一提的是，只需要测试 P层的几个方法就好，不需要断言输入输出数据是否准确（放在其它层去做）
 */
public class PresenterTest {

    @Mock
    private MvpModelImpl modelMock;

    @Mock
    private IMvpView viewMock;

    private MvpPresenter mMvpPresenter;

    @Captor
    private ArgumentCaptor<IMvpModel.GetPhoneDetailsCallback> mNetworkCallbackCaptor;

    @Before
    public void setupTasksPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void inputError_showError() {
        // Get a reference to the class under test
        mMvpPresenter = new MvpPresenter(viewMock, modelMock);

        mMvpPresenter.queryPhone("");
        verify(viewMock).showPhoneError();

    }

    @Test
    public void input_internet_showSuccess() {
        // Get a reference to the class under test
        mMvpPresenter = new MvpPresenter(viewMock, modelMock);
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

}
