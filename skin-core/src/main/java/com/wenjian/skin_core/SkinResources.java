package com.wenjian.skin_core;

import android.app.Application;
import android.content.res.Resources;

/**
 * @author mac
 * @desc desc
 * @date 2018/3/18
 */

public class SkinResources {

    private static SkinResources sInstance;

    private Resources mAppResources;

    private Resources mSkinResources;

    private SkinResources(Application application) {
        this.mAppResources = application.getResources();
    }

    public static void init(Application application){
        if (sInstance == null) {
            synchronized (SkinResources.class) {
                if (sInstance == null) {
                    sInstance = new SkinResources(application);
                }
            }
        }
    }

    public void apply(Resources resources, String packageName) {


    }



}
