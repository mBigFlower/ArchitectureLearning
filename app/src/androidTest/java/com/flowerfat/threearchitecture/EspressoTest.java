package com.flowerfat.threearchitecture;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
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
 */
public class EspressoTest {

    public void click(@IdRes final int id){
        onView(withId(id)).perform(ViewActions.click());
    }

    public void textDisVerify(String text){
        onView(withText(text)).check(matches(isDisplayed()));
    }
    public void textWithIdVerify(@IdRes final int id, String text){
        onView(withId(id)).check(matches(withText(text)));
    }

    public void toastTextVerify(@NonNull Activity activity, int textRes){
        toastTextVerify(activity, ArchApplication.get().getString(textRes));
    }

    public void toastTextVerify(@NonNull Activity activity, String text){
        onView(withText(text))
                .inRoot(withDecorView(not(activity.getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    public void editInput(@IdRes final int id, String inputStr){
        onView(withId(id)).perform(typeText(inputStr), closeSoftKeyboard());
    }

    public void editInput(@IdRes final int id, String inputStr, boolean isClearBefore){
        if(isClearBefore)
            onView(withId(id)).perform(clearText(), typeText(inputStr), closeSoftKeyboard());
        else
            onView(withId(id)).perform(typeText(inputStr), closeSoftKeyboard());
    }

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
}
