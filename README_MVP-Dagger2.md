<h3 id=mvp> MVP Dagger2</h3>

关于MVP的东西，这里就不赘述了。主要来说说Dagger2吧。

## 说点啥

Dagger2从入门到放弃到再捡起，其实我到现在还没发现依赖注入有啥用，毕竟菜鸟。

也看了不少文章，直观的说：将Dagger2加入到MVP中，在创建Presenter实例的时候，不再是在onCreate里用new Presenter()
的方法了，而是使用 @Inject方法，对比图如下：

图一

图二

这样做的好处是啥？某博主说：当Presenter的构造函数发生改变时，不用挨个更改使用Presenter的地方了。。。但是，这个优点我靠，
真的有那么给力吗？我能有多少地方用到Presenter？至于因此引入Dagger2吗？如果你也这么像，那我估计你跟我一样是小白。

既然大家都说好，那就先用着玩吧，慢慢去体会，别那么浅显

## Dagger2使用，纯干货，撒哈拉那么干

### Step1

明白需要注入什么，即Presenter层，我们为Presenter整一个Module

    @Module
    public class MvpDaggerPresenterModule {
        // Presenter持有的View和Model两个层
        private final MvpPlusContract.View mMvpPlusView;
        private final MvpPlusModel mMvpPlusModel;

        public MvpDaggerPresenterModule(MvpPlusContract.View mMvpPlusView, @NonNull MvpPlusModel mMvpPlusModel) {
            this.mMvpPlusView = mMvpPlusView;
            this.mMvpPlusModel = mMvpPlusModel;
        }

        @Provides
        MvpPlusContract.View provideMvpDaggerContractView() {
            return mMvpPlusView;
        }

        @Provides
        MvpPlusModel provideMvpDaggerModel() {
            return mMvpPlusModel;
        }
    }

### Step2

明白哪里需要注入，即Presenter使用的地方（我们要在Activity中创建Presenter实例），为它创建一个Component

    // 这里把step1中创建的Module放进来
    @Component(modules = {MvpDaggerPresenterModule.class})
    public interface MvpDaggerComponent {
        void inject(MvpDaggerActivity mvpDaggerActivity);
    }

### Step3

准备好上面两步，Ctrl+F9 Make Project一下，会生成一个我们需要的类，Dagger开头的，我们在需要注入的地方

        // 例如本例中在MvpDaggerActivity 的 onCreate中，如下“初始化”
        DaggerMvpDaggerComponent.builder()
                .mvpDaggerPresenterModule(new MvpDaggerPresenterModule(this, new MvpPlusModel()))
                .build().inject(this);

然后，我们需要用到的Presenter实例，这样创建

    @Inject
    MvpDaggerPresenter mPresenter;

### 没有Step4，你已经成功了