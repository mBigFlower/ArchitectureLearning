package com.flowerfat.threearchitecture.mvp_dagger2;

import dagger.Component;

@Component(modules = {MvpDaggerPresenterModule.class})
public interface MvpDaggerComponent {

    void inject(MvpDaggerActivity mvpDaggerActivity);
}
