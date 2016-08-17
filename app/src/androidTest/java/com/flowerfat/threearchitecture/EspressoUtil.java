package com.flowerfat.threearchitecture;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;

import com.flowerfat.threearchitecture.util.SetTextAction;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.actionWithAssertions;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by 明明大美女 on 2016/7/24.
 *
 * TODO 1.intent ; 2.onData for listview ; 3. ...
 */
public class EspressoUtil {
    /**
     * 点击，根据Id找到控件
     * @param id
     */
    public void clickWithId(@IdRes final int id){
        onView(withId(id)).perform(ViewActions.click());
    }
    /**
     * 点击，根据字符串str找到控件
     * @param str
     */
    public void clickWithText(@NonNull String str){
        onView(withText(str)).perform(ViewActions.click());
    }
    /**
     * 验证某text是否显示 非Adapter（非Listview、Gridview、Spinner等）
     * @param text
     */
    public void textDisVerify(String text){
        onView(withText(text)).check(matches(isDisplayed()));
    }
    /**
     * 验证某id对应的text是否显示 非Adapter（非Listview、Gridview、Spinner等）
     * @param text
     */
    public void textWithIdVerify(@IdRes final int id, String text){
        onView(withId(id)).check(matches(withText(text)));
    }
    /**
     * 验证toast
     * @param activity
     * @param textRes
     */
    public void toastTextVerify(@NonNull Activity activity, int textRes){
        toastTextVerify(activity, ArchApplication.get().getString(textRes));
    }
    /**
     * 验证toast
     * @param activity
     * @param text
     */
    public void toastTextVerify(@NonNull Activity activity, String text){
        onView(withText(text))
                .inRoot(withDecorView(not(activity.getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }
    /**
     * 模拟EditText输入
     * @param id
     * @param inputStr
     */
    public void editInput(@IdRes final int id, String inputStr){
        onView(withId(id)).perform(typeText(inputStr), closeSoftKeyboard());
    }
    /**
     * 模拟EditText的输入
     * @param id
     * @param inputStr
     * @param isClearBefore 是否先清空，再输入
     */
    public void editInput(@IdRes final int id, String inputStr, boolean isClearBefore){
        if(isClearBefore)
            onView(withId(id)).perform(clearText(), typeText(inputStr), closeSoftKeyboard());
        else
            onView(withId(id)).perform(typeText(inputStr), closeSoftKeyboard());
    }
    /**
     * 清空EditText的内容
     * @param id
     */
    public void editClear(@IdRes final int id){
        onView(withId(id))
                .perform(ViewActions.click(), clearText(), closeSoftKeyboard());
    }
    /* 给 TextView 设置一个初值，方便后面的验证用
     * 不过貌似，每次验证的时候，都会给清空，不用我们手动做这件事
     * 不过这个SetTextAction是一个自定义ViewAction的例子，哈哈哈 */
    public void setText(@IdRes final int id, @NonNull String text){
        onView(withId(id))
                .perform(actionWithAssertions(new SetTextAction(text)));
    }

    public String getStringRes(@StringRes final int StringResId){
        return InstrumentationRegistry.getTargetContext().getString(StringResId);
    }
}
