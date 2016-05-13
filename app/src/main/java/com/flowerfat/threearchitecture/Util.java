package com.flowerfat.threearchitecture;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 明明大美女 on 2016/5/13.
 */
public class Util {

    /**
     * toast
     * @param context
     * @param text
     */
    public static void sT(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
    /**
     * toast
     * @param context
     * @param resId
     */
    public static void sT(Context context, int resId){
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
    }

}
