# Three Architecture

最近在研究MVP+Dagger2，所以把心得写一下。初学者，不知道用什么例子，所以暂时做一个**手机号码归属地的查询**

## 文章结构

- MVC
- MVP
- MVP + Dagger

分别用MVC、MVP和MVP+Dagger2做同一个功能，并通过功能的变更来看三种框架如何更改
（如果这里能带上测试代码就好了，可惜我不会。。。）

![包结构]()

## 功能描述

1. 领导要做一个简单的 手机号归属地查询 的功能，在EditText中输入手机号，点击Button查询，结果返回到下面的TextView
2. 领导说你这个返回的显示太丑了啊，把返回值整理下再显示。
3. 领导又说了，你加个历史记录嘛，把查过的存起来，然后显示在下面，可以滑动的那种条目。
4. 领导又说了，你这个历史记录加一个清空按钮嘛，然后我输入的时候，把下面的记录列表匹配下，万一我以前查过这个号呢？

What a fuc*， 有啥话不能一气儿说完，每次就知道bb。好吧，我承认我在存心黑领导。
功能就暂且这么多吧，不然这篇blog有点长。

下面我们来看，不同的模式，对于上述功能变更时，是如何应对的。

### MVC

MVC的所有代码全都在 MvcActivity 里，第一步的主要代码的如下：

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

第二步来了，这里我们需要创建一个Bean，名为 TelInfo.class，并引入gson来解析服务端返回的Json数据，方便。

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

做第二步的时候，我们在onSuccess周围做了修改

    ...
    @Override
    public void onSuccess(String response) {
        showBeautifulResult(response);
    }
    ...

    /* 第二部分的修改所添加的代码在下面 */
    private TelInfo mTelInfo;
    private void showBeautifulResult(String response) {
        mTelInfo = new Gson().fromJson(response, TelInfo.class);
        String result;
        // 判断查询结果
        if (mTelInfo.getErrNum() != 0) {
            result = mTelInfo.getRetData().getProvince() + "\n" + mTelInfo.getRetData().getCarrier();
        } else {
            result = mTelInfo.getErrMsg();
        }
        mResultTv.setText(result);
    }

第三步，存数据我们就不用数据库了，用SharedPreference来搞，简单易行。

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

    if (mTelInfo.getErrNum() != 0) {
        //    省份后的 \n 变成了 空格。是为了ListView的条目显示。当初加上\n，是因为没有ListView，我们要让
        // Textview显示起来更好看
        result = mTelInfo.getRetData().getProvince() + "  " + mTelInfo.getRetData().getCarrier();
        // 下面这个是新增的哈
        doAboutHistory(mTelInfo.getRetData().getTelString()+"  --  "+result);
    } else {
        result = mTelInfo.getErrMsg();
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

第四部分，清空按钮、输入匹配

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

哎呦我去了，终于给搞完了。不知不觉已经敲了 **230行** 了。
