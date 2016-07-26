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
