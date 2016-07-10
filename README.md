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

- [MVC](#mvc)
- [MVP](#mvp)
- [MVP + Dagger](#mvpdagger2)

分别用MVC、MVP、MVP+Dagger2做同一个功能，并通过功能的变更来看三种框架如何更改
（PS：如果这里能带上测试代码就好了，可惜我不会。但下一步绝对是研究测试code）

![包结构](https://raw.githubusercontent.com/mBigFlower/ArchitectureLearning/master/img/package.png)

## 功能描述

1. 第一部分：领导要做一个简单的 手机号归属地查询 的功能，在EditText中输入手机号，点击Button查询，结果返回到下面的TextView
2. 第二部分：领导说你这个查询结果的显示太丑了啊，把结果内容整理下再显示。
3. 第三部分：领导又说了，你加个历史记录嘛，把查过的存起来，然后显示在下面，可以滑动的那种条目。
4. 第四部分：领导又说了，你这个历史记录加一个清空按钮嘛；然后我输入的时候，把下面的记录列表匹配下，万一我以前查过这个号呢？

What a fuc*， 有啥话不能一气儿说完，每次就知道bb。好吧，我承认我在存心黑领导。
功能就暂且这么多吧，不然这篇blog有点长。

下面我们来看，不同的模式，对于上述功能变更时，是如何应对的。

<h3 id=mvc> MVC </h3>

MVC的所有代码全都在 MvcActivity 里

#### 第一部分 查询

主要代码的如下：

    @OnClick(R.id.architecture_bt)
    void queryBt(){
        String phone = mPhoneEt.getText().toString().trim();

        if(phone.length() != 11 || !phone.startsWith("1")) {
            Util.sT(this, R.string.phone_error);
            return ;
        }

        httpQueryPhoneLocation(phone);
    }

    /**
     * 查询号码归属地的网络请求
     * @param phone 电话号码
     */
    private void httpQueryPhoneLocation(String phone){
        progressShow();
        VolleyUtils.getInstance()
                .get(Contracts.Url)
                .addHeader("apikey", Contracts.Header)
                .addParam("tel", phone)
                .Go(new StringCallback() {
                    @Override
                    public void onSuccess(String response) {
                        progressDismiss();
                        mResultTv.setText(response);
                    }
                    @Override
                    public void onError(String e) {
                        progressDismiss();
                        mResultTv.setText(e);
                    }
                });
    }

#### 第二部分 让结果更好看

这里我们需要创建一个Bean，名为 TelInfoMvc.class，并引入gson来解析服务端返回的Json数据，方便。

    public class TelInfo {
        int errNum;
        String errMsg;
        RetData retData;
        // 省略setter和getter

        public class RetData {
            String telString;
            String province;
            String carrier;
            // 省略setter和getter
        }
    }

做第二部分的时候，我们找到网络请求，在onSuccess周围做了修改

    ...
    @Override
    public void onSuccess(String response) {
        showBeautifulResult(response);
    }
    ...

    /* 第二部分的修改所添加的代码在下面 */
    private TelInfoMvc mTelInfoMvc;
    private void showBeautifulResult(String response) {
        mTelInfoMvc = new Gson().fromJson(response, TelInfo.class);
        String result;
        // 判断查询结果
        if (mTelInfoMvc.getErrNum() != 0) {
            result = mTelInfoMvc.getRetData().getProvince() + "\n" + mTelInfoMvc.getRetData().getCarrier();
        } else {
            result = mTelInfoMvc.getErrMsg();
        }
        mResultTv.setText(result);
    }

#### 第三部分 存数据

我们就不用数据库了，用SharedPreference来搞，简单易行。

思路：每次打开该页面，就要获得历史记录，并显示；每次查询后，把数据保存到SP中。

**打开页面后显示历史记录**

这里我们简单的来，ListView + ArrayAdatper，添加如下代码：


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvc);
        ButterKnife.bind(this);
        // 第三部分新增的俩
        init();
        fetchHistory();
    }

    /**
     * 初始化
     * 第三部分
     */
    private void init() {
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mListView.setAdapter(mAdapter);
    }

    /**
     * 获取数据，显示
     * 第三部分
     */
    private void fetchHistory() {
        // 获取数据
        String data = SpManager.get().getStr(SpManager.KEY_HISTORY_LIST);
        if (data != null) {
            historyList = new Gson().fromJson(data, new TypeToken<List<String>>() {
            }.getType());
        }
        // 显示
        mAdapter.addAll(historyList);
    }

再来处理网络请求之后的部分。我们要在得到正确归属地后，添加存储的部分，然后更新Adapter。更改的地方如下：

    if (mTelInfoMvc.getErrNum() != 0) {
        //    省份后的 \n 变成了 空格。是为了ListView的条目显示。当初加上\n，是因为没有ListView，我们要让
        // Textview显示起来更好看
        result = mTelInfoMvc.getRetData().getProvince() + "  " + mTelInfoMvc.getRetData().getCarrier();
        // 下面这个是新增的哈
        doAboutHistory(mTelInfoMvc.getRetData().getTelString()+"  --  "+result);
    } else {
        result = mTelInfoMvc.getErrMsg();
    }

    public void doAboutHistory(String result) {
        if (!isThisHistoryExist(result)) {
            // 保存
            historyList.add(result);
            SpManager.get().putStr(SpManager.KEY_HISTORY_LIST, new Gson().toJson(historyList));
            // 显示
            mAdapter.add(result);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 判断是否添加过
     * @param result
     * @return
     */
    private boolean isThisHistoryExist(String result) {
        int size = historyList.size();
        for (int i = 0; i < size; i++) {
            if (result.equals(historyList.get(i))) {
                return true;
            }
        }
        return false;
    }

#### 第四部分 清空按钮、输入匹配

    @OnClick(R.id.architecture_clearTv)
    void clearHistory() {
        historyList.clear();
        mAdapter.clear();
        SpManager.get().putStr(SpManager.KEY_HISTORY_LIST, null);
    }

我们为了这个匹配功能，加入了一个showList，之前的historyList用来存所有的记录，这个showList是我们要显示的。
所有，所有设计到数据的地方，我们都会添加上showList，historyList的功能变成了类似于缓存。

    private void filter() {
        mPhoneEt.addTextChangedListener(new TextWatcher() {
            ...
            @Override
            public void onTextChanged(CharSequence searchedStr, int i, int i1, int i2) {
                filterDetails(searchedStr.toString());
            }
            ...
        });
    }

    /**
     * 具体筛选细节
     * @param searchedStr
     */
    private void filterDetails(String searchedStr) {
        showList.clear();
        for (String str : historyList) {
            if(str.contains(searchedStr)) {
                showList.add(str);
            }
        }
        mAdapter.clear();
        mAdapter.addAll(showList);
    }

哎呦我去了，终于给搞完了。不知不觉已经敲了 **230行**、**14个函数**、**9个变量+控件**。如果你看到这了，
我表示哥们你还挺有耐心的。感谢感谢~

没错这里该总结了。我想大家看了那么多没用的code应该已经凌乱且厌烦了吧，当我的MVC架构中，这个Activity里的代码逐渐增多以后，
即使我加了注释，同时自认为每一块都写的很简单明了，但当我回顾一下，还是感觉很挺乱的，数据与显示冗余的揉在一起，
我不只一次问过自己，每个函数是按照功能来放，还是按照流的形式来放，才会让code更清晰呢？如果无良领导继续要你更改，添加，
代码超过了500行，又会是啥样？
这并不是我们想要的。也许刚写完思路很清晰，但过后呢？别人来看呢？

<h3 id=mvp> MVP </h3>

光吹比没用，下面我用MVP模式给之前的代码重构一下，我们来pk一下。为了便于理解，先来个简单的MVP，后面会上一个增强版，

- Model 数据的存取：存在一个接口
- View 界面显示，即UI部分（Activity或者Fragment）：存在有一个接口
- Presenter 逻辑控制部分

**其实有的人说，Presenter只负责传递，操作应该在Model层去处理**，昨天也看了篇文章讲到到底Presenter层和Model层应该做什么，
如果弄混了会怎样怎样。一开始我一脸懵逼，到底应该怎样？后来琢磨了。哪有那么死板啊，只要你觉得思路清晰了，耦合降低了，
各司其职了，就OK。后面我也会提到Google的MVP是怎么组织的。

我们再来走一遍那个 手机号归属地 的工程。不多说，看图(其中接口以字母I开头)：

![mvp_mine](https://raw.githubusercontent.com/mBigFlower/ArchitectureLearning/master/img/mvp_mine.png)

#### 第一部分

**View**:

    private MvpPresenter mPresenter ;

    ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...
        // 看这里 很重要~
        mPresenter = new MvpPresenter(this) ;
    }

    @OnClick(R.id.architecture_bt)
    void queryBt() {
        mPresenter.queryPhone();
    }

    @Override
    public String getPhoneStr() {
        return mPhoneEt.getText().toString().trim();
    }

    @Override
    public void showReslut(String result) {
        mResultTv.setText(result);
    }

    @Override
    public void showPhoneError() {
        Util.sT(this, R.string.phone_error);
    }

不难发现，几乎所有的内容都和UI有关：显示结果，提示Toast等。手机号判断与网络请求的部分没有了，
因为逻辑层面的都放到了Presenter。我们在初始化的时候new了一个MvpPresenter，并把IMvpView的接口传递了过去
（这个的目的是Presenter可以控制Activity做界面操作）。看下MvpPresenter类，一开始我们还是把code多贴点：

** Presenter **

    public class MvpPresenter {

        // 持有View的接口，有啥事儿就通过这个接口通知View做事情
        private IMvpView mIMvpView;

        /**
         * 这个构造方法
         */
        public MvpPresenter(IMvpView mIMvpView) {
            this.mIMvpView = mIMvpView;
        }
        // 查询手机号，Activity中调用的
        public void queryPhone(String phone){
            String phone = mIMvpView.getPhoneStr();
            if (phone.length() != 11 || !phone.startsWith("1")) {
                // 通过接口，控制Activity显示错误的Toast
                mIMvpView.showPhoneError();
                return;
            }
            httpQueryPhoneLocation(phone);
        }
        /**
         * 查询号码归属地的网络请求
         *
         * @param phone 电话号码
         */
        private void httpQueryPhoneLocation(String phone) {
            VolleyUtils.getInstance()
                    .get(Contracts.Url)
                    .addHeader("apikey", Contracts.Header)
                    .addParam("tel", phone)
                    .Go(new StringCallback() {
                        @Override
                        public void onSuccess(String response) {
                            // 通过接口，控制主界面更新
                            mIMvpView.showReslut(response);
                        }

                        @Override
                        public void onError(String e) {
                            // 通过接口，控制主界面更新
                            mIMvpView.showReslut(e);
                        }
                    });
        }
    }

代码很简单，总之就是把一部分 分成了各司其职的两部分，因为我们还没涉及到数据层，所以没用上Model。

#### 第二部分 让结果更好看

因为UI没有变化，所以只需要修改Model即可，修改如下：

    未完待续 。。。



下面我们来看 [官方的demo]()是这样的工程结构：按照功能划分（这样貌似几个人开发一个项目的时候，很明确，鲜有交叉），下面来看一张图：

![](https://raw.githubusercontent.com/mBigFlower/ArchitectureLearning/master/img/mvp_google.png)

其中，末尾是Contract的接口里面有两个接口：View的接口和Presenter的接口