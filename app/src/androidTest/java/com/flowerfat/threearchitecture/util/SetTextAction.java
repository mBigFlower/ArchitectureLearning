package com.flowerfat.threearchitecture.util;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by 明明大美女 on 2016/7/25.
 * Replaces view text by setting {@link EditText}s text property to given String.
 */
public final class SetTextAction implements ViewAction {
    private final String stringToBeSet;

    public SetTextAction(String value) {
        if(null == value) {
            throw(new NullPointerException("the SetTextAction's param must not be null"));
        }
        this.stringToBeSet = value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Matcher<View> getConstraints() {
        return allOf(isDisplayed(), isAssignableFrom(TextView.class));
    }

    @Override
    public void perform(UiController uiController, View view) {
        ((TextView) view).setText(stringToBeSet);
    }

    @Override
    public String getDescription() {
        return "set text";
    }
}
