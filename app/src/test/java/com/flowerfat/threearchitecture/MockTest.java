package com.flowerfat.threearchitecture;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by 明明大美女 on 2016/7/13.
 */
public class MockTest {

    /**
     * 基本的mock测试
     */
    @Test
    public void Test1() {
        //arrange
        Iterator i = mock(Iterator.class);
        when(i.next()).thenReturn("Hello").thenReturn("World");
        //act
        String result = i.next() + " " + i.next();
        //verify
        verify(i, times(2)).next();
        //assert
        assertEquals("Hello World", result);
    }

    /**
     * anyInt()的测试， 类似的还有anyString()
     */
    @Test
    public void argumentMatchersTest() {
        List<String> mock = mock(List.class);
        when(mock.get(anyInt())).thenReturn("Hello").thenReturn("World");
        String result = mock.get(100) + " " + mock.get(200);
        verify(mock, times(2)).get(anyInt());
        assertEquals("Hello World", result);
    }

    @Test
    public void argumentCaptorTest() {
        List mock = mock(List.class);
        List mock2 = mock(List.class);
        mock.add("John");
        mock2.add("Brian");
        mock2.add("Jim");

        ArgumentCaptor argument = ArgumentCaptor.forClass(String.class);

        verify(mock).add(argument.capture());
        assertEquals("John", argument.getValue());

        verify(mock2, times(2)).add(argument.capture());
        assertEquals("Jim", argument.getValue());

    }

}
