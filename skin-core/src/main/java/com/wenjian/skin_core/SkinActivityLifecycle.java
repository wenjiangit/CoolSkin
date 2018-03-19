package com.wenjian.skin_core;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.view.LayoutInflater;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * @author mac
 * @desc desc
 * @date 2018/3/17
 */

public class SkinActivityLifecycle implements Application.ActivityLifecycleCallbacks{

    HashMap<Activity, SkinLayoutFactory> mHashMap = new HashMap<>();

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);

        try {
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.set(layoutInflater,false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        SkinLayoutFactory factory = new SkinLayoutFactory();
        LayoutInflaterCompat.setFactory2(layoutInflater, factory);
        layoutInflater.setFactory2(new SkinLayoutFactory());

        SkinManager.getInstance().addObserver(factory);
        mHashMap.put(activity, factory);

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

        SkinLayoutFactory factory = mHashMap.remove(activity);
        SkinManager.getInstance().deleteObserver(factory);
    }
}
