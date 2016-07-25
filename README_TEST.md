# 测试篇

因为是初学测试，这里仅仅是一个方法的汇总。其实当你在摸清mockito的几种方法时，你会有疑问：咋写测试啊？

### mockito

**创建对象**，就是把new的对象，变成了mock(...)
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

![](http://img.blog.csdn.net/20160203090228944)

相关文章：

- [https://testerhome.com/topics/4314](https://testerhome.com/topics/4314)

