# Three Architecture

最近在研究MVP+Dagger2，所以把心得写一下。初学者，不知道用什么例子，所以暂时做一个**手机号码归属地的查询**

## 序

现在风风火火的出了那么多架构，我感觉无非就是要我们代码敲起来更顺，如何更顺？调理清晰。如何调理清晰？各司其职，解耦。

什么是耦合？我举个例子：俩人出去旅游（男女或男男不限），都没带手机，这就尴尬了。
想自己逛逛吧，怕走散，一起逛吧，还要时刻关注对方是否在自己的视线内。众口难调，是不是很累？

这时候如果带了手机，哼哼。“我去那边逛逛哈，一会手机联系”，多方便，自己便可以去做自己的事情，玩自己的。
有了手机后，两个人就不用耦合在一起了。
当然这里我不是要强调手机的重要性，而是要提到接口。两个类如果互相持有对方的接口，就好比有了对方的手机号，
有需要的时候联系一下，没需要的时候各玩各的。

我想上面的例子会有助于你对接下来的MVP的理解，ok，go

## 文章结构

- [MVC](https://github.com/mBigFlower/ArchitectureLearning/blob/master/README_MVC.md)
- [MVP](https://github.com/mBigFlower/ArchitectureLearning/blob/master/README_MVP.md)
- [MVP + Dagger](https://github.com/mBigFlower/ArchitectureLearning/blob/master/README_MVP-Dagger2.md)
- [Test](https://github.com/mBigFlower/ArchitectureLearning/blob/master/README_TEST.md)

分别用MVC、MVP、MVP+Dagger2做同一个功能，并通过功能的变更来看三种框架如何更改
（PS：MVP会添加测试，目前View和Presenter层都加了测试代码，初学者那种。至于Model层，还在研究）

![包结构](https://raw.githubusercontent.com/mBigFlower/ArchitectureLearning/master/img/package.png)

## 功能描述

1. 第一部分：领导要做一个简单的 手机号归属地查询 的功能，在EditText中输入手机号，点击Button查询，结果返回到下面的TextView
2. 第二部分：领导说你这个查询结果的显示太丑了啊，把结果内容整理下再显示。
3. 第三部分：领导又说了，你加个历史记录嘛，把查过的存起来，然后显示在下面，可以滑动的那种条目。
4. 第四部分：领导又说了，你这个历史记录加一个清空按钮嘛；然后我输入的时候，把下面的记录列表匹配下，万一我以前查过这个号呢？

What a fuc*， 有啥话不能一气儿说完，每次就知道bb。好吧，我承认我在存心黑领导。
功能就暂且这么多吧，不然这篇blog有点长。

下面我们来看，不同的模式，对于上述功能变更时，是如何应对的。

- [MVC](https://github.com/mBigFlower/ArchitectureLearning/blob/master/README_MVC.md)
- [MVP](https://github.com/mBigFlower/ArchitectureLearning/blob/master/README_MVP.md)
- [MVP + Dagger](#mvpdagger2)

