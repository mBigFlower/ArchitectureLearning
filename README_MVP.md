<h3 id=mvp> MVP </h3>

光吹比没用，下面我用MVP模式给之前的代码重构一下，我们来pk一下。为了便于理解，先来个简单的MVP，后面会上一个增强版，

- Model 有的人管它叫Server，我理解：服务器请求、数据的存取等（就是排除View和Presenter的所有）
- View 界面显示，即UI部分（Activity或者Fragment）
- Presenter 逻辑控制部分，某简书作者称之为拉皮条的。

**其实有的人说，Presenter只负责传递，操作应该在Model层去处理**，昨天也看了篇文章讲到到底Presenter层和Model层应该做什么，
如果弄混了会怎样怎样。一开始我一脸懵逼，到底应该怎样？后来琢磨了。哪有那么死板啊，只要你觉得思路清晰了，耦合降低了，
各司其职了，就OK。后面我也会提到Google的MVP是怎么组织的。

#### 码前的话

这里我们跟MVC有些不同，在做每个功能之前，不仅要弄明白这个功能，还要**把M、V、P三层的内容给分离开来**。
我觉得这个比敲代码更重要。下面就根据四个功能来看我们如何拆解

--

第一部分：领导要做一个简单的 手机号归属地查询 的功能，在EditText中输入手机号，点击Button查询，结果返回到下面的TextView

**[View](https://github.com/mBigFlower/ArchitectureLearning/blob/master/app/src/main/java/com/flowerfat/threearchitecture/mvp/view/MvpActivity.java)**

a. Toast显示EditText输入错误

b. 网络请求中，ProgressDialog的显示与取消显示

c. 查询结果的显示

**[Model](https://github.com/mBigFlower/architectureLearning/blob/master/app/src/main/java/com/flowerfat/threearchitecture/mvp/model/MvpModelImpl.java)**

a. 网络请求，并返回查询结果

**[Presenter](https://github.com/mBigFlower/ArchitectureLearning/blob/master/app/src/main/java/com/flowerfat/threearchitecture/mvp/presenter/MvpPresenter.java)**

a. 判断View传过来要查询的手机号格式，手机号错误：调用View的Toast显示；手机号正确则调用Model层的网络请求，并处理回调

--

第二部分：领导说你这个查询结果的显示太丑了啊，把结果内容整理下再显示。

返回结果进行处理，这部分是在Model层做的（仅仅是处理String），其他两层无需更改有木有，所以打开Model层，添加

b. 增加showBeautifulResult函数，处理网络请求的返回结果。并将Beautiful的结果回调给P层

--

第三部分：领导又说了，你加个历史记录嘛，把查过的存起来，然后显示在下面，可以滑动的那种条目。

历史记录大家应该都不陌生。我们分别来看三层都添加了哪些：

**[View](https://github.com/mBigFlower/ArchitectureLearning/blob/master/app/src/main/java/com/flowerfat/threearchitecture/mvp/view/MvpActivity.java)** 改动不大，仅仅是增加了一个ListView

a. Toast显示EditText输入错误

b. 网络请求中，ProgressDialog的显示与取消显示

c. 查询结果的显示

　d. 增加历史的显示（把数据显示到ListView中）; 至于Adapter就不说了

**[Model](https://github.com/mBigFlower/architectureLearning/blob/master/app/src/main/java/com/flowerfat/threearchitecture/mvp/model/MvpModelImpl.java)** 此项增加的相当于数据库的操作：增 和 查

a. 网络请求，并返回查询结果

　b. 增：将网络请求后的数据，判断不是重复的，便保存到本地

　c. 查：获取本地历史（每次打开应用的时候，需要获取）

**[Presenter](https://github.com/mBigFlower/ArchitectureLearning/blob/master/app/src/main/java/com/flowerfat/threearchitecture/mvp/presenter/MvpPresenter.java)** 调度上面View和Model增加的内容

a. 判断View传过来要查询的手机号格式，手机号错误：调用View的Toast显示；手机号正确则调用Model层的网络请求，并处理回调

　b. 初始化的时候，调用Model的查询历史，并在View层显示

--

第四部分：领导又说了，你这个历史记录加一个清空按钮嘛；然后我输入的时候，把下面的记录列表匹配下，万一我以前查过这个号呢？

这部分其实有两个，一个是清空历史（View操作后，由Presenter调用Model的删除数据库），
一个是匹配（监听EditText，并将输入传递给Model，由Model处理后，显示历史）。

其中匹配的时候，我惊喜的发现，匹配的结果显示不需要在View层增加接口，就是之前的View-d，
只不过其显示的内容不再是网络请求返回的结果，而是匹配后的结果

**[View](https://github.com/mBigFlower/ArchitectureLearning/blob/master/app/src/main/java/com/flowerfat/threearchitecture/mvp/view/MvpActivity.java)** 并没有变化，仅仅增加了个按钮，这里我们就不写上来了

**[Model](https://github.com/mBigFlower/architectureLearning/blob/master/app/src/main/java/com/flowerfat/threearchitecture/mvp/model/MvpModelImpl.java)** 此项增加的相当于数据库的操作：删 ； 匹配：过滤List

a. 网络请求，并返回查询结果

b. 增：将网络请求后的数据，判断不是重复的，便保存到本地

c. 查：获取本地历史（每次打开应用的时候，需要获取）

　d. 删：清空本地历史

　e. 匹配：filterHistory。 将本地历史的List进行过滤后，把结果通过Presenter返回给View显示

**[Presenter](https://github.com/mBigFlower/ArchitectureLearning/blob/master/app/src/main/java/com/flowerfat/threearchitecture/mvp/presenter/MvpPresenter.java)** 改动不大

a. 判断View传过来要查询的手机号格式，手机号错误：调用View的Toast显示；手机号正确则调用Model层的网络请求，并处理回调

b. 初始化的时候，调用Model的查询历史，并在View层显示

　 c. 增加了个filter的中介函数，将Model过滤后的结果，返回给View显示




我们再来走一遍那个 手机号归属地 的工程。不多说，看图(其中接口以字母I开头)：

![mvp_mine](https://raw.githubusercontent.com/mBigFlower/ArchitectureLearning/master/img/mvp_mine.png)

#### 第一部分

**[View](https://github.com/mBigFlower/ArchitectureLearning/blob/master/app/src/main/java/com/flowerfat/threearchitecture/mvp/model/MvpModelImpl.java)**:

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
