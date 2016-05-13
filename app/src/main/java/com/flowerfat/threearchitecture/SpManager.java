package com.flowerfat.threearchitecture;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 明明大美女 on 2016/5/12.
 */
public class SpManager {

    private static final String SP_NAME_HISTORY = "history";
    public static final String KEY_HISTORY_LIST = "list";

    private static SpManager sInstance ;

    private SharedPreferences mHistorySP ;

    public SpManager() {
        mHistorySP = ArchApplication.get().getSharedPreferences(SP_NAME_HISTORY, Context.MODE_PRIVATE);
    }

    public static SpManager get(){
        if (sInstance == null) {
            synchronized (SpManager.class) {
                if (sInstance == null) {
                    sInstance = new SpManager();
                }
            }
        }
        return sInstance;
    }

    /////////////////////////////////////////////
    public void putStr(String key, String value){
        mHistorySP.edit().putString(key, value).apply();
    }
    public String getStr(String key){
        return mHistorySP.getString(key, null);
    }
}
