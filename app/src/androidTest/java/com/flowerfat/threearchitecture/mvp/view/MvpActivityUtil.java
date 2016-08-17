package com.flowerfat.threearchitecture.mvp.view;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.flowerfat.threearchitecture.EspressoUtil;
import com.flowerfat.threearchitecture.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by 明明大美女 on 2016/7/20.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MvpActivityUtil extends EspressoUtil {

    private MvpActivity mvpActivity;

    @Rule
    public ActivityTestRule<MvpActivity> mActivityTestRule =
            new ActivityTestRule<>(MvpActivity.class);

    @Before
    public void init() {
//        IdlingPolicies.setMasterPolicyTimeout(
//                1000 * 10, TimeUnit.MILLISECONDS);
//        IdlingPolicies.setIdlingResourceTimeout(
//                1000 * 10, TimeUnit.MILLISECONDS);
        mvpActivity = mActivityTestRule.getActivity();
        setText(R.id.architecture_tv, "");
    }

    @Test
    public void inputError() throws Exception {
        editInput(R.id.architecture_et, "1");
        clickWithId(R.id.architecture_bt);
        // 这里用到了检测Toast的内容。里面需要用到activity。 这个acitivyt是从rule中获取的，在@before里
        toastTextVerify(mvpActivity, "手机号格式错误");

        editInput(R.id.architecture_et, "15828433284");
        clickWithId(R.id.architecture_bt);
        toastTextVerify(mvpActivity, "手机号格式错误");
        // 这里用到了 clearText() -- 清空edittext
        editClear(R.id.architecture_et);
    }

    @Test
    public void inputSuccess() throws Exception {
        editInput(R.id.architecture_et, "15828433284");
        clickWithId(R.id.architecture_bt);

        IdlingResource idlingResource = new EspressoDelay(mvpActivity);
        Espresso.registerIdlingResources(idlingResource);
        //等待后台ListView加载完数据后执行后面的代码
        Espresso.unregisterIdlingResources(idlingResource);

        textDisVerify("15828433284  --  四川  四川移动");
        textDisVerify("四川  四川移动");
    }

}