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

**好吧我承认我写不下去了**

反正MVP写下来后，我的感觉：

1. 一开始写mvp你会不知道某些代码该写在哪里，View很简单。但Model和Presenter容易迷茫，我的建议：怎么爽怎么写（但要有条理）
2. 突然多了很多类，写一个新功能时或许会手足无措，这时要把思路捋清楚。把M、V、P分开考虑，而不是像在mvc中直接上手写




下面我们来看 [官方的demo]()是这样的工程结构：按照功能划分（这样貌似几个人开发一个项目的时候，很明确，鲜有交叉），下面来看一张图：

![](https://raw.githubusercontent.com/mBigFlower/ArchitectureLearning/master/img/mvp_google.png)

其中，末尾是Contract的接口里面有两个接口：View的接口和Presenter的接口
