# 测试篇

因为是初学测试，这里仅仅是一个方法的汇总。其实当你在摸清mockito的几种方法时，你会有疑问：咋写测试啊？

### mockito

**创建对象**，就是把new的对象，变成了mock(...)，例如：

    Iterator<String> i = mock(Iterator.class);

**对方法设定返回值** （使用在调用前，一种模拟，当我们调用i.next时，模拟返回Hello，再调用则返回World）

    when(i.next()).thenReturn("Hello").thenReturn("World")
    when(i.next()).thenReturn("Hello", "World")
    doReturn("Hello").doReturn("World").when(i).next(); // 第二种方式

**对方法设定返回异常**

    when(i.next()).thenThrow(new RuntimeException())

**对void的操作**
。。。

**ArgumentCaptor** 参数捕获，对处理异步回调有用


### Espresso

我到现在也没明白Espresso和Robolectric的区别，看过别人评论的，但是没懂。
说白了，Espresso就是模拟你手在屏幕上的操作，需要app跑起来。而Robolectric不需要跑app（速度就更快了）

具体Espresso的内容的pdf版本请 [点击这里](https://github.com/mBigFlower/ArchitectureLearning/blob/master/img/espresso-cheat-sheet-2.1.0.pdf)

相关文章：

- [https://testerhome.com/topics/4314](https://testerhome.com/topics/4314)
- [https://testerhome.com/canty](https://testerhome.com/canty)

看完相关文章后，定会有所感悟。然后我写了个类，方便更好的使用Espresso。[详见这里](https://github.com/mBigFlower/ArchitectureLearning/blob/master/app/src/androidTest/java/com/flowerfat/threearchitecture/EspressoTest.java)

