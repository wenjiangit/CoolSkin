package com.wenjian.skin_core.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author mac
 * @desc desc
 * @date 2018/3/18
 */

public class SkinPreference {


    private static volatile SkinPreference sInstance;

    private SharedPreferences mPreferences;

    private static final String KEY_SKIN = "skin_path";


    private SkinPreference(Application application) {
        this.mPreferences = application.getSharedPreferences("skin_prefs", Context.MODE_PRIVATE);

    }

    public static void init(Application application) {
        if (sInstance == null) {
            synchronized (SkinPreference.class) {
                if (sInstance == null) {
                    sInstance = new SkinPreference(application);
                }
            }
        }
    }

    public static SkinPreference getInstance() {
        return sInstance;
    }

    public void setSkin(String path) {
        mPreferences.edit().putString(KEY_SKIN, path).apply();
    }

    public String getSkin() {
        return mPreferences.getString(KEY_SKIN,"");
    }

}
