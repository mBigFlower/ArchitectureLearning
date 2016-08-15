package com.flowerfat.threearchitecture.mvp_dagger2;

import android.support.annotation.NonNull;

import com.flowerfat.threearchitecture.mvp__plus.queryphone.MvpPlusContract;
import com.flowerfat.threearchitecture.mvp__plus.queryphone.MvpPlusModel;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 明明大美女 on 2016/8/13.
 */

@Module
public class MvpDaggerPresenterModule {
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
//    @Nullable // 之前这里学别人的，copy过来的，有个这个@Nullable ，结果他妈一直有问题，日了狗了
    MvpPlusModel provideMvpDaggerModel() {
        return mMvpPlusModel;
    }
}
